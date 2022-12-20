package sharechat.com.controller;

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
    *       3. 接受好友申请*/

    @GetMapping("/match")
    public Result<List<Map<String, ?>>> searchUser(@RequestParam("userId") String id,
                                                   @RequestParam("info") String info) {
        return Result.success(friendService.searchUser(id, info));
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

    @GetMapping("/new")
    public Result<String> makeLink(@RequestParam("userId") String userid,
                                   @RequestParam("friendId") String friendId) {
        if(userid.equals(friendId)) return Result.error("Id不能相同");
        friendService.makeLink(userid, friendId);
        return Result.success("success");
    }

    @DeleteMapping("/del")
    public Result<String> deleteLink(@RequestParam("from") String userid,
                                     @RequestParam("to") String friendid) {
        if(userid.equals(friendid)) return Result.error("Id不能相同");
        friendService.deleteLink(userid, friendid);
        return Result.success("删除成功");
    }

    @PostMapping("/friends")
    public Result<List<LinkNode>> getMyFriends(@RequestBody Map name) {
        return Result.success(friendService.getAllFriend((String) name.get("name")));
    }

    @GetMapping("/recommand")
    public Result<List<LinkNode>> getRecommand(@RequestParam("userId") String id) {
        return Result.success(friendService.getRecommand(id));
    }
}
