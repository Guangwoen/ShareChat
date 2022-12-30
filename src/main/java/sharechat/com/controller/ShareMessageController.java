package sharechat.com.controller;

import org.springframework.web.bind.annotation.*;
import sharechat.com.entity.LinkNode;
import sharechat.com.service.ShareMessageService;
import sharechat.com.util.result.Result;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/share")
public class ShareMessageController {
    private final ShareMessageService shareMessageService;

    public ShareMessageController(ShareMessageService shareMessageService) {
        this.shareMessageService = shareMessageService;
    }

    @GetMapping("/shareReq")
    public Result<Boolean> sendShareRequest(@RequestParam("senderId") String sender,
                                 @RequestParam("receiverId") String receiver,
                                 @RequestParam("target") String target) {
        return Result.success(shareMessageService.sendShareRequest(sender, receiver, target));
    }

    @GetMapping("/isShareReq")
    public Result<Boolean> getShareRequest(@RequestParam("userId") String userId) {
        return Result.success(shareMessageService.getShareRequest(userId));
    }

    @GetMapping("/getAllReq")
    public Result<LinkNode> getAllShareRequest(@RequestParam("userId") String userId) {
        return Result.success(shareMessageService.getAllShareRequest(userId));
    }

    @GetMapping("/getShareMessage")
    public Result<List<Map<String,String>>> getShareMessage(@RequestParam("userId") String userId){
        return Result.success(shareMessageService.getChannelMessages(userId));
    }

    @PostMapping("/endShare")
    public Result<Boolean> endShare(@RequestParam("userId") String userId){
        return Result.success(shareMessageService.endShare(userId));
    }
}
