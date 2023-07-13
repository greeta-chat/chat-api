package com.example.simplewebchat.security;

import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

    public CustomOAuth2UserService(List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors) {
        this.oAuth2UserInfoExtractors = oAuth2UserInfoExtractors;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional = oAuth2UserInfoExtractors.stream()
                .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
                .findFirst();

        if (oAuth2UserInfoExtractorOptional.isEmpty()) {
            throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
        }
        return oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
    }
}