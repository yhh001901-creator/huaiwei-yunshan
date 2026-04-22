package com.restaurant.interceptor;

import com.restaurant.enums.UserRole;
import com.restaurant.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 跳过登录和注册请求的认证
        String requestPath = request.getRequestURI();
        if (requestPath.equals("/api/auth/login") || requestPath.equals("/api/auth/register")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        if (token == null || !jwtUtil.validateToken(token)) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录或登录已过期\",\"data\":null}");
            return false;
        }

        Integer roleCode = jwtUtil.getRole(token);
        if (roleCode == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"无效的token\",\"data\":null}");
            return false;
        }

        UserRole userRole = UserRole.getByCode(roleCode);
        
        if (!hasPermission(userRole, requestPath)) {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"无权限访问\",\"data\":null}");
            return false;
        }

        request.setAttribute("userId", jwtUtil.getUserId(token));
        request.setAttribute("username", jwtUtil.getUsername(token));
        request.setAttribute("role", roleCode);
        
        return true;
    }

    private boolean hasPermission(UserRole role, String path) {
        System.out.println("检查权限 - 角色: " + role.getName() + ", 路径: " + path);
        for (String permission : role.getPermissions()) {
            System.out.println("  权限模式: " + permission + ", 匹配: " + matchPath(permission, path));
            if (matchPath(permission, path)) {
                return true;
            }
        }
        System.out.println("  无匹配权限");
        return false;
    }

    private boolean matchPath(String pattern, String path) {
        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);
            return path.startsWith(prefix);
        }
        if (pattern.endsWith("/*")) {
            String prefix = pattern.substring(0, pattern.length() - 2);
            if (!path.startsWith(prefix)) {
                return false;
            }
            String suffix = path.substring(prefix.length());
            return !suffix.contains("/");
        }
        return pattern.equals(path);
    }
}
