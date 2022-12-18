package sharechat.com.controller;


import com.baomidou.mybatisplus.extension.api.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sharechat.com.entity.GroupNode;
import sharechat.com.entity.LinkNode;
import sharechat.com.entity.UserInfo;
import sharechat.com.repository.UserRepository;
import sharechat.com.service.FriendService;
import sharechat.com.service.TokenService;
import sharechat.com.util.oss.AliyunOSSUtil;
import sharechat.com.util.result.Result;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
                userRepository.goOnline(userid);
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
    public boolean logout(@RequestBody String id, HttpServletRequest request){
        userRepository.noOnline(id);
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

    @PostMapping("/updateUserInfo")
    public Result<Map<String,Object>> updateUserInfo(@RequestBody Map info){
        String id=(String) info.get("userId");
        String name=(String) info.get("username");
        String workplace=(String) info.get("organization");
        String region=(String) info.get("address");
        int age=(int) info.get("age");
        String gender=(String) info.get("gender");
        String signature=(String) info.get("description");
        Map<String,Object>returnInfo=new HashMap<>();
        int updateRows=userRepository.updateInfo(id,name,workplace,region,age,gender,signature);
        if(updateRows==1){
            returnInfo.put("result",true);
        }else{
            returnInfo.put("result",false);
        }
        return Result.success(returnInfo);
    }

    @PostMapping("/updateUserPassword")
    public Result<Map<String,Object>> updateUserPassword(@RequestBody Map info){
        String id=(String) info.get("userId");
        String password=(String) info.get("password");
        Map<String,Object>returnInfo=new HashMap<>();
        int updateRows=userRepository.updatePassword(id,password);
        if(updateRows==1){
            returnInfo.put("result",true);
        }else{
            returnInfo.put("result",false);
        }
        return Result.success(returnInfo);
    }

    @GetMapping("/getAllOnlineUsers")
    public Result<Map<String,Object>> getAllOnlineUsers(@RequestParam("userId") String id){
        List<LinkNode> friends = friendService.getAllFriend(id);
        int num=friends.size();
        ArrayList<Map> onlineFriends=new ArrayList<>();
        Map<String,Object> returnInfo=new HashMap<>();
        for(int i=0;i<num;i++){
            String friendId=friends.get(i).getUserId();
            if(userRepository.findById(friendId).isPresent()){
                UserInfo userInfo=userRepository.findById(friendId).get();
                if(userInfo.getOnline()){
                    Map<String,Object>friend=new HashMap<>();
                    friend.put("id",userInfo.getId());
                    friend.put("name",userInfo.getName());
                    onlineFriends.add(friend);
                }
            }
        }
        returnInfo.put("onlineFriends",onlineFriends);
        return Result.success(returnInfo);
    }
}
