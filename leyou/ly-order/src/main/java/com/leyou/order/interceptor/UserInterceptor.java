package com.leyou.order.interceptor;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.order.config.JwtProperties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class UserInterceptor implements HandlerInterceptor {

    private static final ThreadLocal<UserInfo> tl = new ThreadLocal<>();

    private JwtProperties prop;

    public UserInterceptor(JwtProperties prop) {
        this.prop = prop;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) {
        // 获取cookie
        String token = CookieUtils.getCookieValue(request, prop.getCookieName());
        try {
            UserInfo info = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
            tl.set(info);
            return true;
        } catch (Exception e) {
            log.error("[订单服务] 解析用户身份失败", e);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) {
        // 最后用完数据, 一定要清空
        tl.remove();
    }

    public static UserInfo getUser() {
        return tl.get();
    }
}
