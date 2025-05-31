package com.dairy.take12.service;

import com.dairy.take12.AdminPrincipal;
import com.dairy.take12.model.Admin;
import com.dairy.take12.repository.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class AdminDetailsService implements UserDetailsService {
    @Autowired
    AdminRepo adminRepo;
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Optional<Admin> admin = adminRepo.findById(id);
        if(!admin.isPresent())
        {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User name not found");
        }
        else{
            return  new AdminPrincipal(admin.get());
        }
    }
}
