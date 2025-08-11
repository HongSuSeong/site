package com.water.site.service;

import com.water.site.entity.UserEntity;
import com.water.site.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> response = (Map<String, Object>) oAuth2User.getAttributes().get("response");
        String providerId = (String) response.get("id");
        String username = provider + "_" + providerId;
        String name = (String) response.get("name");
        String email = (String) response.get("email");

        UserEntity user = userRepository.findByUsername(username)
                .orElse(UserEntity.builder()
                        .username(username)
                        .password(passwordEncoder.encode("temp")) // 더미 패스워드
                        .name(name)
                        .email(email)
                        .role("ROLE_USER")
                        .provider(provider)
                        .providerId(providerId)
                        .build());

        userRepository.save(user);

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole())),
                oAuth2User.getAttributes(),
                "response");
    }
}
