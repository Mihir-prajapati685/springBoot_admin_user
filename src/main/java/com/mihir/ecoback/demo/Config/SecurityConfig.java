package com.mihir.ecoback.demo.Config;//package com.mihir.ecoback.demo.Config;
//
//import com.mihir.ecoback.demo.Services.CustomUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class SecurityConfig {
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new CustomUserDetailsService();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
/// /    @Bean
/// /    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
/// /        http.csrf(csrf -> csrf.disable())
/// /                .authorizeHttpRequests(auth -> auth
/// /                        .requestMatchers("/auth/**").permitAll()
/// /                        .requestMatchers("/admin/**").hasRole("ADMIN")
/// /                        .requestMatchers("/user/**").hasRole("USER")
/// /                        .anyRequest().authenticated()
/// /                )
/// /                .formLogin(fo->fo.permitAll())
/// /                .sessionManagement(session -> session
/// /                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
/// /                );
/// /        return http.build();
/// /    }
//@Bean
//public  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//    return http
//            .csrf(customizer->customizer.disable())
//            .authorizeHttpRequests(request->request
//                    .requestMatchers("/auth/**").permitAll()
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/user/**").hasRole("USER")
//                    .anyRequest().authenticated())
//            .httpBasic(Customizer.withDefaults())
//            .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//            .build();
//}
//
//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//
//        DaoAuthenticationProvider provider=new  DaoAuthenticationProvider();
//        provider.setPasswordEncoder(passwordEncoder());//this line say now no any passwrod Encoder set
//        provider.setUserDetailsService(userDetailsService());
//        return provider;
//    }
//

import com.mihir.ecoback.demo.JwtToken.JwtFilter;
import com.mihir.ecoback.demo.Services.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(cs -> cs.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()  // Allow unauthenticated access to /auth endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")  // Only allow admins to access /admin endpoints
                        .requestMatchers("/user/**").hasRole("USER")  // Only allow users to access /user endpoints
                        .anyRequest().authenticated()  // Protect other endpoints
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless sessions for JWT
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  // Add JWT filter
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
}
