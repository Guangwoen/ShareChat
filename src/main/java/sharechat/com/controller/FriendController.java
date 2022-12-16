package sharechat.com.controller;

import cn.hutool.core.annotation.Link;
import org.springframework.web.bind.annotation.*;
import sharechat.com.entity.LinkNode;
import sharechat.com.service.FriendService;
import sharechat.com.util.result.Result;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    /* TODO 1. 发送好友申请
    *       2. 删除好友
    *       3. 接受好友申请
    *       4. 获取推荐用户*/

    @GetMapping("/match")
    public Result<List<LinkNode>> searchUser(@RequestParam String info) {
        return null;
    }

    /**
     * 存储一个用户
     *
     * @param linkNode 请求体
     * */
    @PostMapping("/node")
    public Result<LinkNode> saveThis(@RequestBody LinkNode linkNode) {
        /* TODO 向MySQL添加更详细的用户数据 */
        return Result.success(friendService.save(linkNode));
    }

    @PostMapping("/new")
    public Result<String> makeLink(@RequestBody Map params) {
        return Result.success(friendService.makeLink(
                (String) params.get("n1"),
                (String) params.get("n2")));
    }

    @PostMapping("/friends")
    public Result<List<LinkNode>> getMyFriends(@RequestBody Map name) {
        return Result.success(friendService.getAllFriend((String)name.get("name")));
    }
}
