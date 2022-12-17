package sharechat.com.controller;


import cn.hutool.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sharechat.com.annotation.LoginToken;
import sharechat.com.entity.UserInfo;
import sharechat.com.repository.UserRepository;
import sharechat.com.service.TokenService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private TokenService tokenService;
    public UserController(UserRepository u){
        userRepository=u;
    }
    //@LoginToken
    @PostMapping( "/login")
    public Object login(@RequestBody String userid,
                         @RequestBody String password,
                         HttpServletRequest request,
                         HttpServletResponse response){
        String realPassword=userRepository.findPasswordById(userid);
        JSONObject jsonObject=new JSONObject();
        boolean result;
        UserInfo userInfo=new UserInfo(userid,password,"","",0,"","");
        if(userRepository.findById(userid).isPresent()) {
            userInfo = userRepository.findById(userid).get();
        }
        if(realPassword.equals(password)){
            HttpSession session=request.getSession(true);
            session.setAttribute("user",userInfo);
            String token=tokenService.getToken(userInfo);
            jsonObject.put("token",token);

            Cookie cookie=new Cookie("token",token);
            cookie.setPath("/");
            response.addCookie(cookie);

            result=true;
        }else{
            result=false;
        }
        jsonObject.put("result",result);
        return jsonObject;
    }

    @PostMapping("/logout")
    public boolean logout(HttpServletRequest request){
        HttpSession session=request.getSession();
        session.removeAttribute("user");
        return true;
    }

    @PostMapping("/register")
    public void register(@RequestBody String userid,
                         @RequestBody String password,
                         @RequestBody String workplace,
                         @RequestBody String region,
                         @RequestBody int age,
                         @RequestBody String gender,
                         @RequestBody String signature){
        UserInfo userInfo=new UserInfo(userid,password,workplace,region,age,gender,signature);
        userRepository.saveAndFlush(userInfo);
    }


}
