package com.v02.minback.config;

import com.v02.minback.filter.SecurityJwtFilter;
import com.v02.minback.filter.SecurityLoginFilter;
import com.v02.minback.filter.SecurityLogoutFilter;
import com.v02.minback.service.front.JwtFrontService;
import com.v02.minback.util.FilterUtil;
import com.v02.minback.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final FilterUtil filterUtil;
    private final JwtFrontService jwtFrontService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000","https://localhost:8080"));
                configuration.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
                        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
                                configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/bank/**").permitAll()
                .requestMatchers("/user/signup/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/reissue").permitAll()
                .anyRequest().authenticated() );

        http.logout((logout) -> logout
                .logoutSuccessUrl("/logout")
        );
        http.addFilterBefore(new SecurityJwtFilter(jwtUtil,filterUtil,jwtFrontService), SecurityLoginFilter.class);

        http.addFilterAt(new SecurityLoginFilter(
                authenticationManager(authenticationConfiguration), jwtFrontService),
                UsernamePasswordAuthenticationFilter.class);

        http.addFilterBefore(new SecurityLogoutFilter(jwtUtil,jwtFrontService,filterUtil), LogoutFilter.class);

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
