package com.dairy.take12.config;

import com.dairy.take12.service.AdminDetailsService;
import com.dairy.take12.service.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.net.http.HttpRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Autowired
   private AdminDetailsService adminDetailsService;
    @Autowired
    JwtFilter jwtFilter;
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .csrf(customizer -> customizer.disable())
                        .authorizeHttpRequests(request -> request
                                .requestMatchers("/admin/**","/admin/search/{text}",
                                        "/admin/login","/advanceOrganization/**","/cattleFeedPurchase/**",
                                        "/cattleFeedSell/**","/cattleFeedSupplier/**","/customerAdvance/**",
                                        "/ledger/**","/loanEntry/**","/localSale/**","/sms/**","/customer/**","/customerBalance/**",
                                        "/deduction/**","/collection/**","/matrix/**"
                                        )
//                                .requestMatchers("/admin/refresh-token","/admin/search/{text}","/admin/add",
//                                        "/admin/login"
//                                )
                                .permitAll()
                                .anyRequest().authenticated())
                        .httpBasic(Customizer.withDefaults())
                        .sessionManagement(session ->
                                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                        .build();
        }

        @Bean
    public AuthenticationProvider authenticationProvider(){
            DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
            provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
            provider.setUserDetailsService(adminDetailsService);
            return provider;
        }
        @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
            return config.getAuthenticationManager();
        }
}