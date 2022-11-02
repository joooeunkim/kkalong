package com.ssafy.kkalong.handler;

import com.ssafy.kkalong.api.dto.UserInfoDto;
import com.ssafy.kkalong.api.entity.User;
import com.ssafy.kkalong.api.repository.UserRepository;
import com.ssafy.kkalong.api.service.UserService;
import com.ssafy.kkalong.common.ApiResponse;
import com.ssafy.kkalong.jwt.JwtProvider;
import com.ssafy.kkalong.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationSuccessHandlerImpl extends SimpleUrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        System.out.println("AuthenticationSuccessHandlerImpl-onAuthenticationSuccess: authentication="+authentication.toString());
        // 전달받은 인증정보 SecurityContextHolder에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // JWT Token 발급
        final String token = jwtProvider.generateJwtToken(authentication);
        // Response
        if(authentication instanceof OAuth2AuthenticationToken){
            String url = makeRedirectUrl(token);
            getRedirectStrategy().sendRedirect(request, response, url);
        } else{
            Map<String, Object> result = new HashMap<>();
            User user = extractUserInfos(authentication);
            UserInfoDto userInfoDto = UserInfoDto.builder()
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .nickname(user.getNickname())
                    .gender(user.getGender())
                    .age(user.getAge())
                    .height(user.getHeight())
                    .weight(user.getWeight())
                    .provider(user.getProvider())
                    .followers(userService.getFollowerListByReceiverId(user.getId()))
                    .followings(userService.getFollowingListBySenderId(user.getId()))
                    .build();
            result.put("token", token);
            result.put("user", userInfoDto);

            ApiResponse.success(response, result);
        }

    }

    private String makeRedirectUrl(String token) {
        return UriComponentsBuilder.fromUriString("http://k7b302.p.ssafy.io/oauth2/redirect?token="+token).build().toUriString();
    }

    private User extractUserInfos(Authentication authentication) {

        String email;
        if(authentication instanceof OAuth2AuthenticationToken) {//oauth2 로그인
            System.out.println("extractUserInfos: 소셜 로그인: authentication"+authentication.toString());
            email = String.valueOf(((UserDetailsImpl) authentication.getPrincipal()).getUsername());
        } else{ //kkalong 로그인
            System.out.println("extractUserInfos: 일반로그인: authentication"+authentication.toString());
            email = String.valueOf(authentication.getPrincipal());
        }

        User user = userRepository.findByEmail(email);
        return user;
    }

}
