package sharechat.com.interceptor;

import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sharechat.com.util.exception.BizException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        // 在此判断是否登陆
        String token = request.getHeader("token");
        // 验证token的正确性
        /*if(!StringUtils.hasLength(token)){
            throw new BizException(9001, "token不能为空");
        }*/
        return true;
    }

    @Override
    public void postHandle(@NonNull HttpServletRequest request,
                           @NonNull HttpServletResponse response,
                           @NonNull Object handler,
                              ModelAndView modelAndView) {
        System.out.println("Controller 执行完毕");
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception ex) {
        System.out.println("获取到了一个返回的结果: " + response);
        System.out.println("请求结束");
    }
}
