package com.taskflow.security.resolver;

import com.taskflow.user.entity.User;
import com.taskflow.security.annotation.AuthenticatedUser;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticatedUserArgumentResolver implements HandlerMethodArgumentResolver 
{
    @Override 
	public boolean supportsParameter(MethodParameter p) 
	{
        return p.hasParameterAnnotation(AuthenticatedUser.class) && User.class.isAssignableFrom(p.getParameterType());
    }
    @Override 
	public Object resolveArgument(MethodParameter p, ModelAndViewContainer mc, NativeWebRequest req, WebDataBinderFactory bf) 
	{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User u)) return null;
        return u;
    }
}
