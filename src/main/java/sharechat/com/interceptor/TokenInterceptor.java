package sharechat.com.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import sharechat.com.entity.UserInfo;
import sharechat.com.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    public TokenInterceptor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        // 在此判断是否登陆
        String token = request.getHeader("token");
        if(token==null){
            throw new RuntimeException("无token，请重新登录");
        }
        String userid;
        try{
            userid=JWT.decode(token).getAudience().get(0);
        }catch (JWTDecodeException jwtDecodeException){
            throw new RuntimeException("401");
        }
        UserInfo userInfo;
        if(userRepository.findById(userid).isPresent()) {
            userInfo = userRepository.findById(userid).get();
        }else{
            throw new RuntimeException("账号不存在，请重新登录");
        }
        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(userInfo.getPassword())).build();
        try{
            jwtVerifier.verify(token);
        }catch (JWTVerificationException jwtVerificationException){
            throw new RuntimeException("401");
        }
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
