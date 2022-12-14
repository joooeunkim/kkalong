package com.ssafy.kkalong.handler;

import com.ssafy.kkalong.common.ApiResponse;
import com.ssafy.kkalong.common.ApiResponseType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ApiResponse.error(response, ApiResponseType.UNAUTHORIZED_RESPONSE, "인증 실패");
    }

}