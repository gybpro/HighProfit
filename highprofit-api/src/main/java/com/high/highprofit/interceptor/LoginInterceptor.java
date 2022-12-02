package com.high.highprofit.interceptor;

import com.high.highprofit.bean.User;
import com.high.highprofit.util.Assert;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 登录拦截器
 *
 * @author high
 * @version 1.0
 * @since 1.0
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, Object> redisTemplate;

    public LoginInterceptor(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 验证token
        String token = request.getHeader("token");
        Assert.isEmpty(token, "用户未登录，请前往登录");

        // 验证user
        User user = (User) redisTemplate.opsForValue().get(token);
        Assert.isFlag(user != null, "登录超时，请重新登录");

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
