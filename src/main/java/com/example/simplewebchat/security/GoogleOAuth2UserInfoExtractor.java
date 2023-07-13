package com.example.simplewebchat.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {

    @Override
    public CustomUserDetails extractUserInfo(OAuth2User oAuth2User) {
        CustomUserDetails customUserDetails = new CustomUserDetails();
        customUserDetails.setUsername(retrieveAttr("given_name", oAuth2User) + retrieveAttr("family_name", oAuth2User));
        customUserDetails.setName(retrieveAttr("name", oAuth2User));
        customUserDetails.setAttributes(oAuth2User.getAttributes());
        customUserDetails.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(WebSecurityConfig.CHAT_USER)));
        return customUserDetails;
    }

    @Override
    public boolean accepts(OAuth2UserRequest userRequest) {
        return "google".equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId());
    }
}