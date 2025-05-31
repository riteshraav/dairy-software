package com.dairy.take12.service;

import com.dairy.take12.model.Admin;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    ApplicationContext context;
    @Autowired
    JwtService jwtService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("in filter authorizing request with endpoint : "+ request.getRequestURI());
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String id=null;
        if(authHeader != null && authHeader.startsWith("Bearer "))
        {
            System.out.println("auth header is not null and starts with Bearer");
            token = authHeader.substring(7);
            try{
                id  = jwtService.extractId(token);
            }
            catch (ExpiredJwtException e) {
                System.out.println("exception at 43 "+ e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Access token expired\"}");
                return;
            } catch (JwtException e) {
                System.out.println("exception at 48 "+ e.getMessage());

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid access token\"}");
                return;
            }
        }
        if(id != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            System.out.println("id is not null now checking userdetails");
            UserDetails adminDetails = context.getBean(AdminDetailsService.class).loadUserByUsername(id);
            if(jwtService.validateToken(token,adminDetails))
            {
                System.out.println("token is  validate");
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(adminDetails,null,adminDetails.getAuthorities() );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            else{
                System.out.println("token is not validated");
            }

        }
        System.out.println();
        filterChain.doFilter(request,response);
    }
}
