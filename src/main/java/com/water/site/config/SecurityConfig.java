package com.water.site.config;

import com.water.site.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/","/home","/verify","/join","/email/**","/login","/css/**","/js/**").permitAll()
                            .requestMatchers("/board/**").authenticated()
                    ).formLogin(login -> login
                            .loginPage("/login")
                            .defaultSuccessUrl("/home",true)
                            .permitAll()
                    )
                    .oauth2Login(oauth2 -> oauth2
                            .loginPage("/login")
                            .userInfoEndpoint(userInfo ->
                                    userInfo.userService(customOAuth2UserService)
                            )
                    )
                    .logout(logout -> logout
                            .logoutSuccessUrl("/home")
                            .permitAll()
                    );

            return http.build();
    }
}