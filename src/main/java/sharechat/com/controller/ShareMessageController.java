package sharechat.com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sharechat.com.entity.LinkNode;
import sharechat.com.service.ShareMessageService;
import sharechat.com.util.result.Result;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/share")
public class ShareMessageController {
    private final ShareMessageService shareMessageService;

    public ShareMessageController(ShareMessageService shareMessageService) {
        this.shareMessageService = shareMessageService;
    }

    @GetMapping("/shareReq")
    public void sendShareRequest(@PathParam("senderId") String sender,
                                 @PathParam("receiverId") String receiver,
                                 @PathParam("target") String target) {
        shareMessageService.sendShareRequest(sender, receiver, target);
    }

    @GetMapping("/isShareReq")
    public Result<Boolean> getShareRequest(@PathParam("userId") String userId) {
        return Result.success(shareMessageService.getShareRequest(userId));
    }

    @GetMapping("/getAllReq")
    public Result<List<LinkNode>> getAllShareRequest(@PathParam("userId") String userId) {
        return Result.success(shareMessageService.getAllShareRequest(userId));
    }
}
