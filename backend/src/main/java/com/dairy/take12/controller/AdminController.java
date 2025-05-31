package com.dairy.take12.controller;
import com.dairy.take12.model.Admin;
import com.dairy.take12.model.DatabaseSequence;
import com.dairy.take12.model.RefreshToken;
import com.dairy.take12.repository.AdminRepo;
import com.dairy.take12.repository.RefreshTokenRepo;
import com.dairy.take12.repository.SearchRepo;
import com.dairy.take12.service.AdminDetailsService;
import com.dairy.take12.service.JwtService;
import com.twilio.http.Response;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Ref;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    SearchRepo searchRepo;
    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtService jwtService;
    @Autowired
    ApplicationContext context;
    @Autowired
    RefreshTokenRepo refreshTokenRepo;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    public Long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOperations.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq",1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
    @GetMapping("/getAll")
    public List<Admin> getAllAdmins()
    {
        return adminRepo.findAll();
    }
    @PostMapping("/add")
    public void addAdmin(@RequestBody Admin admin)
    {
        long  code = generateSequence(Admin.admin_code_seq);
        admin.setCode(code);
        admin.setCustomerSequence(0);
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
         adminRepo.save(admin);
    }
    @PostMapping("/update")
    public void updateAdmin (@RequestBody Admin admin){

        System.out.println("Admin updated");
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        adminRepo.save(admin);
    }
    @GetMapping("/search/{text}")
    public Admin findByPhone(@PathVariable String text)
    {
        return searchRepo.findByPhone(text);
    }

    @GetMapping("/search")
    public ResponseEntity<?> findAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestHeader = request.getHeader("Authorization");
        String accessToken = null;
        String id = null;

        if(requestHeader == null || !requestHeader.startsWith("Bearer "))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out header in null");
        }

        accessToken = requestHeader.substring(7);
        if(accessToken.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out refresh token is empty");
        }
        try{
            id  = jwtService.extractId(accessToken);
        }
        catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Access token expired\"}");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out expired token");
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid access token\"}");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out jwt exception");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(id != null && authentication.isAuthenticated())
        {
            System.out.println("token is authorised in ");
            UserDetails adminDetails = context.getBean(AdminDetailsService.class).loadUserByUsername(id);
            if(jwtService.validateToken(accessToken,adminDetails))
            {
                return  ResponseEntity.ok(adminRepo.findById(id));
            }
        }

      return   ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user is unauthorised");

    }
    @PostMapping("/login")
    public ResponseEntity<?> verifyAdmin(@RequestBody Admin admin, HttpServletResponse response)
    {
        System.out.println("admin is verifying");
        try{
//            Authentication authentication =   authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(admin.getId(),admin.getPassword()));
            Admin checkAdmin =    adminRepo.findById(admin.getId()).orElse(new Admin());
            boolean matched = Objects.equals(checkAdmin.getPassword(), admin.getPassword());
            System.out.println(matched+" matched passwords");
            if(checkAdmin.getId() != null && matched )
            {
                System.out.println("is authenticated");
                String accessToken = jwtService.generateAccessToken(admin.getId());
                String refreshToken = generateAndStoreRefreshToken(admin.getId());
                response.setHeader("access-token",accessToken);
                response.setHeader("refresh-token",refreshToken);
                System.out.println( response.getHeaderNames());
                return ResponseEntity.ok(admin);
            }
        }catch (BadCredentialsException e)
        {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: Invalid credentials");
        }
        catch (UsernameNotFoundException e)
        {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Login failed: User do not exist");
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: Error");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: Unknown Error");

    }
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = null;

        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            token = authHeader.substring(7);
            String id = jwtService.extractId(token);
            UserDetails adminDetails = context.getBean(AdminDetailsService.class).loadUserByUsername(id);
            if (!jwtService.validateToken(token,adminDetails)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
            refreshTokenRepo.deleteByToken(token);
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("bad request");
    }
    @GetMapping("/refresh-token")
    public ResponseEntity<?> updateAccessToken(HttpServletRequest request,  HttpServletResponse response) throws IOException {
        System.out.println("token is refreshing in controller");
        String requestHeader = request.getHeader("Authorization");
        String refreshToken = null;
        String id = null;

        if(requestHeader == null || !requestHeader.startsWith("Bearer "))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out header in null");
        }

        refreshToken = requestHeader.substring(7);
        if(refreshToken.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out refresh token is empty");
        }
        RefreshToken doesRefreshTokenExist = refreshTokenRepo.findByToken(refreshToken).orElse(new RefreshToken());
//        if(doesRefreshTokenExist.getToken() == null)
//        {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh token do not exist");
//        }
        try{
            id  = jwtService.extractId(refreshToken);
        }
        catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Access token expired\"}");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out expired token");
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid access token\"}");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out jwt exception");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(id != null && authentication.isAuthenticated())
        {
            System.out.println("token is authorised");
            UserDetails adminDetails = context.getBean(AdminDetailsService.class).loadUserByUsername(id);
            if(jwtService.validateToken(refreshToken,adminDetails))
            {
                System.out.println("token is valid by time");
                    String accessToken = jwtService.generateAccessToken(id);
                    if(jwtService.doesNeedUpdation(refreshToken))
                    {
                        refreshToken = updateRefreshToken(refreshToken,id);
                    }
                    response.setHeader("access-token",accessToken);
                    response.setHeader("refresh-token",refreshToken);


            }
            else{
                System.out.println("validity of token is expired");
            }
           return ResponseEntity.ok("access token updated successfully");

        }
        System.out.println(id+"here is id");
        System.out.println(authentication.isAuthenticated()+"get authentication");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid credentials-Logged out unknown error");
    }

    public String generateAndStoreRefreshToken(String userId) {
        String refreshToken = jwtService.generateRefreshToken(userId); // Your method to generate token
        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setToken(refreshToken);
        tokenEntity.setUserId(userId);
        tokenEntity.setCreatedAt(new Date());
        tokenEntity.setExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60*24*7));

        // Save to MongoDB
        refreshTokenRepo.save(tokenEntity);
        return refreshToken;
    }

    public String updateRefreshToken(String token,String id) {

        Optional<RefreshToken> oldToken = refreshTokenRepo.findByToken(token);
        if(oldToken.isPresent())
        {
            String newRefreshTokenValue = jwtService.generateRefreshToken(id);
            RefreshToken newRefreshToken = oldToken.get();
            newRefreshToken.setToken(newRefreshTokenValue);
            newRefreshToken.setUserId(id);
            newRefreshToken.setCreatedAt(new Date());
            newRefreshToken.setExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60*24*7));
            refreshTokenRepo.save(newRefreshToken);
            return  newRefreshTokenValue;
        }

        return generateAndStoreRefreshToken(id);
    }
}