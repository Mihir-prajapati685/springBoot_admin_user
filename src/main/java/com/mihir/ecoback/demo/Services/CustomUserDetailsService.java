package com.mihir.ecoback.demo.Services;

import com.mihir.ecoback.demo.CustomUserDetails;
import com.mihir.ecoback.demo.Models.UserModel;
import com.mihir.ecoback.demo.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new CustomUserDetails(user);
    }

//    public String loginasAdmin(String email, String password, PasswordEncoder passwordEncoder) {
//        UserModel userModel=userRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("user not found"));
//        if(!userModel.getRole().equals("ROLE_ADMIN")){
//            return "Access denied: Not an admin";
//        }
//        if(!passwordEncoder.matches(password, userModel.getPassword())){
//            return "Invalid password";
//        }
//        return "admin login sucessfully";
//    }
//
//    public String loginasUser(String email, String password, PasswordEncoder passwordEncoder) {
//        UserModel userModel=userRepo.findByEmail(email).orElse(null);
//        if(userModel==null){
//            return "Access denied: Email not match";
//        } if(!userModel.getRole().equals("ROLE_USER")){
//            return "Access denied: Not an USER";
//        }
//        if(!passwordEncoder.matches(password, userModel.getPassword())){
//            return "Invalid password";
//        }
//        return "user login sucessfully";
//    }

    public String login(String email, String password, PasswordEncoder passwordEncoder) {
        UserModel userModel = userRepo.findByEmail(email).orElse(null);

        if(userModel==null){
            return "Email not Match";
        }

        if (!passwordEncoder.matches(password, userModel.getPassword())) {
            return "Invalid password";
        }

        // Optionally return role too if needed
        return "login success:" + userModel.getRole();
    }
}
