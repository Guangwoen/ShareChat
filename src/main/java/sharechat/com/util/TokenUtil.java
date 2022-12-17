package sharechat.com.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sharechat.com.entity.UserInfo;
import sharechat.com.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Service
public class TokenUtil {

    public static String getTokenUserId(){
        String token = getRequest().getHeader("token");
        String userId= JWT.decode(token).getAudience().get(0);
        return userId;
    }
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes requestAttributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes==null?null : requestAttributes.getRequest();
    }
}
