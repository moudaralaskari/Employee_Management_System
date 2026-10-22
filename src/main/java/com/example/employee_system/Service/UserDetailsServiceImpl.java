package com.example.employee_system.Service;

import com.example.employee_system.Entity.UserAccount;
import com.example.employee_system.Repository.UserAccountRepo;
import com.example.employee_system.Shared.CustomResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserAccountRepo userAccountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount userAccount = userAccountRepo.findOneByUsername
                (username).orElseThrow(CustomResponseException::BadCredentials);

        return User.builder()
                .username(userAccount.getUsername())
                .password(userAccount.getPassword())
                .roles(userAccount.getRole())
                .build();
    }

}
