package sharechat.com.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import sharechat.com.entity.UserInfo;

import java.util.Date;

public class TokenService {
    public String getToken(UserInfo userInfo){
        Date start=new Date();
        long currentTime=System.currentTimeMillis()+1000*60*60*24;
        Date end=new Date(currentTime);
        String token="";
        token= JWT.create().withAudience(userInfo.getId()).withIssuedAt(start).withExpiresAt(end).sign(Algorithm.HMAC256(userInfo.getPassword()));
        return token;
    }
}
