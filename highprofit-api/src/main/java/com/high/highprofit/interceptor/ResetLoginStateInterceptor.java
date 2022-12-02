package com.high.highprofit.interceptor;

import com.high.highprofit.bean.User;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 重置登录状态拦截器
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Component
public class ResetLoginStateInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, Object> redisTemplate;

    public ResetLoginStateInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token != null) {
            User user = (User) redisTemplate.opsForValue().get(token);
            if (user != null) {
                redisTemplate.expire(token, 30, TimeUnit.MINUTES);
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
