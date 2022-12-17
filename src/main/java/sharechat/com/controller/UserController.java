package sharechat.com.controller;


import cn.hutool.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sharechat.com.annotation.LoginToken;
import sharechat.com.entity.GroupNode;
import sharechat.com.entity.LinkNode;
import sharechat.com.entity.UserInfo;
import sharechat.com.repository.UserRepository;
import sharechat.com.service.FriendService;
import sharechat.com.service.TokenService;
import sharechat.com.util.result.Result;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;

    private final FriendService friendService;

    private TokenService tokenService;
    public UserController(UserRepository u,TokenService t, FriendService friendService){
        tokenService=t;
        userRepository=u;
        this.friendService = friendService;
    }
    //@LoginToken
    @PostMapping( "/login")
    public Result<Map<String, Object>> login(@RequestBody Map info,
                        HttpServletRequest request,
                        HttpServletResponse response){
        // System.out.println(info);
        String userid = (String) info.get("id");
        String password = (String) info.get("password");
        Map<String,Object> returnInfo=new HashMap<>();
        boolean result;
        if(userRepository.findById(userid).isPresent()) {
            String realPassword = "";
            UserInfo userInfo = userRepository.findById(userid).get();
            realPassword = userInfo.getPassword();
            if (realPassword.equals(password)) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", userInfo);
                String token=tokenService.getToken(userInfo);
                returnInfo.put("token", token);

                Cookie cookie = new Cookie("token", token);
                cookie.setPath("/");
                response.addCookie(cookie);
                result = true;
            } else {
                result = false;
            }
        }else {
            result=false;
        }
        returnInfo.put("result",result);
        return Result.success(returnInfo);
    }

    @PostMapping("/logout")
    public boolean logout(HttpServletRequest request){
        HttpSession session=request.getSession();
        session.removeAttribute("user");
        return true;
    }

    @PostMapping("/register")
    public Result<Map<String,Object>> register(@RequestBody UserInfo userInfo){
        System.out.println(userInfo);
        String userId=userInfo.getId();
        String name=userInfo.getName();
        if(!userRepository.findById(userId).isPresent()) {
            userRepository.saveAndFlush(userInfo); // MySQL保存信息
            friendService.saveNewUser(new LinkNode(userInfo.getId(), userInfo.getName()),
                    new GroupNode(userInfo.getWorkplace())); // Neo4j保存信息
            boolean result;
            Map<String, Object> returnInfo = new HashMap<>();
            if (userRepository.findById(userId).isPresent()) {
                result = true;
            } else {
                result = false;
            }
            returnInfo.put("result", result);
            return Result.success(returnInfo);
        }else{
            return Result.error("该邮箱已经注册过");
        }
    }


}
