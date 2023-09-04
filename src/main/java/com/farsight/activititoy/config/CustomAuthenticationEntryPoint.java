package com.farsight.activititoy.config;

import com.farsight.activititoy.exception.BusinessException;
import com.farsight.activititoy.exception.CodeMsg;
import com.farsight.activititoy.uitl.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 检查异常类型是否为身份验证异常
        if (authException instanceof AuthenticationException) {
            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(response.getWriter(), Result.error(CodeMsg.TOKEN_ERROR,"请先登录"));
        } else {
        }
    }

}
