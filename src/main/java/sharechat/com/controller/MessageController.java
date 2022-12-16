package sharechat.com.controller;

import org.springframework.web.bind.annotation.*;
import sharechat.com.entity.Message;
import sharechat.com.service.MessageService;
import sharechat.com.util.result.Result;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 保存客户端发送的信息(POST)
     *
     * @param message 发送的消息
     * @return 是否成功
     * */
    @PostMapping(value = "/message")
    public Result<Message> saveMessage(@RequestBody Message message) {
        String id = message.getChannelId();
        if(id == null || id.length() == 0) {
            System.out.println("<<报错>>: 空channelId");
            return Result.error("空channelId");
        }
        String sender = message.getSenderId();
        if(sender == null || sender.length() == 0) {
            System.out.println("<<报错>>: 空senderId");
            return Result.error("空senderId");
        }
        Message msg = new Message(String.valueOf(messageService.getSnowflakeId()),
                id, sender, Instant.now().toString(), message.getMsgBody(), false);
        messageService.saveMsg(msg);
        return Result.success(msg);
    }

    @GetMapping(value = "/alarm")
    public Result<Boolean> hasAlarm(String id) {
        return Result.success(messageService.isMissingInRedis(id) || messageService.isMissingInDB(id));
    }

    @GetMapping(value = "/list")
    public Result<Message> getListById(String id) {
        return null;
    }

    @GetMapping("/all")
    public Result<List<Message>> getMessages() {
        return Result.success(messageService.findAllMsg());
    }

    @GetMapping("/message")
    public Result<List<Message>> getBySenderId(String id) {
        return Result.success(messageService.findBySenderId(id));
    }

    @GetMapping("/all/my")
    public Result<List<Message>> getMyAll() {
        return Result.success(messageService.findAllMsg());
    }
}
