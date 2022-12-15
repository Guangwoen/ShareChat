package sharechat.com.controller;

import org.springframework.web.bind.annotation.*;
import sharechat.com.entity.User;
import sharechat.com.repository.FriendRelationRepository;
import sharechat.com.util.result.Result;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendRelationRepository friendRelationRepository;

    public FriendController(FriendRelationRepository friendRelationRepository) {
        this.friendRelationRepository = friendRelationRepository;
    }

    /* TODO 1. 发送好友申请
    *       2. 删除好友
    *       3. 接受好友申请
    *       4. 获取推荐用户*/

    /**
     * 存储一个用户
     *
     * @param user 请求体
     * */
    @PostMapping("/user")
    public Result<User> saveThis(@RequestBody User user) {
        /* TODO 向MySQL添加更详细的用户数据 */
        return Result.success(friendRelationRepository.save(user));
    }
}
