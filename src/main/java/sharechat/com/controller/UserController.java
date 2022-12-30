package sharechat.com.controller;


import cn.hutool.core.lang.hash.Hash;
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
    public boolean logout(@RequestBody Map info, HttpServletRequest request){
        String id=(String)info.get("userId");
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
            friendService.saveNewUser(new LinkNode(userInfo.getId(), userInfo.getName(), userInfo.getHeadPicture()),
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

    @PutMapping("/updateUserInfo")
    public Result<Map<String, Boolean>> updateUserInfo(@RequestBody UserInfo info){
        System.out.println(info);
        if(info.getId() == null || info.getId().length() == 0) return Result.error("请求错误");
        String password = userRepository.findPasswordById(info.getId());
        Map<String, Boolean> resultMap = new HashMap<>();
        if(info.getName() == null || info.getName().length() == 0
                || (info.getAge() < 0 || info.getAge() > 100)
                || info.getGender() == null || info.getGender().length() != 1) {
            resultMap.put("result", false);
        }
        else {
            info.setPassword(password);
            UserInfo k = userRepository.saveAndFlush(info);
            friendService.setAvatar(info.getHeadPicture(), info.getId());
            resultMap.put("result", true);
        }
        return Result.success(resultMap);
    }

    @PutMapping("/updateUserPassword")
    public Result<Map<String,Object>> updateUserPassword(@RequestParam("userId") String id,
                                                         @RequestParam("password") String password){
        // 假设id和password都不是空
        Map<String,Object>returnInfo=new HashMap<>();
        UserInfo modifiedUser = userRepository.getReferenceById(id);
        modifiedUser.setPassword(password);
        userRepository.saveAndFlush(modifiedUser);
        returnInfo.put("result", true);
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
                    friend.put("avatar",userInfo.getHeadPicture());
                    onlineFriends.add(friend);
                }
            }
        }
        returnInfo.put("onlineFriends",onlineFriends);
        return Result.success(returnInfo);
    }

    @GetMapping("/showUserInfo")
    public Result<Map<String,Object>> showUserInfo(@RequestParam("userId") String id){
        System.out.println(id+"========");
        Map<String,Object> returnInfo=new HashMap<>();
        if(userRepository.findById(id).isPresent()) {
            UserInfo userInfo = userRepository.findById(id).get();
            returnInfo.put("userId",userInfo.getId());
            returnInfo.put("username",userInfo.getName());
            returnInfo.put("organization",userInfo.getWorkplace());
            returnInfo.put("age",userInfo.getAge());
            returnInfo.put("gender",userInfo.getGender());
            returnInfo.put("avatar",userInfo.getHeadPicture());
            returnInfo.put("description",userInfo.getSignature());
            returnInfo.put("address",userInfo.getRegion());
            returnInfo.put("result",true);
        }else{
            returnInfo.put("result",false);
        }
        return Result.success(returnInfo);
    }

    @PostMapping("/media/{type}")
    public Result<String> uploadMedia(@RequestPart("file") MultipartFile file,
                                      @PathVariable("type") String type) {
        String uploadUrl = "";
        System.out.println("文件上传");
        try {
            if(null != file) {
                String fileName = file.getOriginalFilename();
                if(!"".equals(fileName.trim())) {
                    File newFile = new File(fileName);
                    FileOutputStream os = new FileOutputStream(newFile);
                    os.write(file.getBytes());
                    os.close();
                    file.transferTo(newFile);
                    uploadUrl = AliyunOSSUtil.upload(newFile, type);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        if(null != uploadUrl)
            return Result.success(uploadUrl);
        return Result.error("上传失败");
    }
}
