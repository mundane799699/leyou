package com.leyou.gateway.filters;

import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.CookieUtils;
import com.leyou.common.utils.JwtUtils;
import com.leyou.gateway.config.FilterProperties;
import com.leyou.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class AuthFilter extends ZuulFilter {
    @Autowired
    private JwtProperties prop;

    @Autowired
    private FilterProperties filterProp;

    @Override
    public String filterType() {
        // 过滤器类型,选择前置过滤
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        // 过滤器顺序
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String path = request.getRequestURI();
        // 是否允许放行
        boolean isAllowPath = isAllowPath(path);
        // 如果应该放行, 就不应该再过滤
        return !isAllowPath;
    }

    private boolean isAllowPath(String path) {
        List<String> allowPaths = filterProp.getAllowPaths();
        for (String allowPath : allowPaths) {
            if (path.startsWith(allowPath)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String token = CookieUtils.getCookieValue(request, prop.getCookieName());
        try {
            UserInfo info = JwtUtils.getInfoFromToken(token, prop.getPublicKey());
        } catch (Exception e) {
            // token解析失败, 未登录, 拦截
            ctx.setSendZuulResponse(false);
            // 返回状态码
            ctx.setResponseStatusCode(403);
        }
        // 校验权限
        return null;
    }
}
