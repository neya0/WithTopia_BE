package com.four.withtopia.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.four.withtopia.db.domain.Member;
import com.four.withtopia.db.domain.ProfileImage;
import com.four.withtopia.db.repository.MemberRepository;
import com.four.withtopia.db.repository.ProfileImageRepository;
import com.four.withtopia.dto.request.SocialUserInfoDto;
import com.four.withtopia.util.SocialMemberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}") private String CLIENT_ID; // rest APi 키
    @Value("${spring.security.oauth2.client.registration.google.client-secret}") private String CLIENT_SECRET ; // 시크릿 키
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}") private String REDIRECT_URI; // 리다이렉트 uri

    // 카카오에서 엑세스 토큰 받아오기
    String getGoogleAccessToken(String code) throws JsonProcessingException {
        // Http 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        // Http 바디
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", CLIENT_ID);
        body.add("redirect_uri", REDIRECT_URI);
        body.add("client_secret", CLIENT_SECRET);
        body.add("code", code);
        // Http 요청
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(body,headers);
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );
        // kakao Access Token
        String responseBody = response.getBody();
        System.out.println("responseBody = " + responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    // 카카오에서 유저 인포 받아오기
    SocialUserInfoDto getGoogleUserInfo(String googleAccessToken) throws JsonProcessingException {
        // Http 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + googleAccessToken);
        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        System.out.println("googleAccessToken = " + googleAccessToken);
        // Http 요청
        HttpEntity<MultiValueMap<String, String>> googleUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(
                "https://www.googleapis.com/oauth2/v1/userinfo",
                HttpMethod.GET,
                googleUserInfoRequest,
                String.class
        );
        // kakao user info
        String responseBody = response.getBody();
        System.out.println("responseBody = " + responseBody);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return SocialUserInfoDto.createGoogleUserInfoDto(jsonNode);
    }

}
