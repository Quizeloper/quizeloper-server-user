package com.cs.quizeloper.global.resolver;

import com.cs.quizeloper.global.exception.BaseException;
import com.cs.quizeloper.global.exception.BaseResponseStatus;
import com.cs.quizeloper.global.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static com.cs.quizeloper.global.constant.Constant.Jwt.AUTHORIZATION_HEADER;
import static com.cs.quizeloper.global.constant.Constant.Jwt.BEARER_PREFIX;
import static com.cs.quizeloper.global.exception.BaseResponseStatus.*;

@RequiredArgsConstructor
@Component
public class LoginResolver implements HandlerMethodArgumentResolver {
    private final JwtService jwtService;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Account.class) && String.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // annotation 확인
        Auth auth = parameter.getParameterAnnotation(Auth.class);
        if (auth == null) throw new BaseException(BaseResponseStatus.REQUIRED_AUTH_ANNOTATION);

        // servletRequest 확인
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if(httpServletRequest == null) throw new BaseException(NOT_ACCESS_HEADER);
        // header 안에 값이 있는지 확인
        String token = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(token)){
            throw new BaseException(NULL_TOKEN);
        }

        // user 값 추출
        token = token.replace(BEARER_PREFIX, "");
        if(jwtService.validateToken(token)){
            return UserInfo.toDto(jwtService.getUserIdFromJWT(token));
        } else{
            throw new BaseException(USER_NOT_FOUND);
        }
    }


}
