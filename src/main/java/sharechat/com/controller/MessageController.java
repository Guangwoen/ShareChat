package sharechat.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sharechat.com.entity.Message;
import sharechat.com.repository.MessageRepository;
import sharechat.com.util.SnowflakeHelper;
import sharechat.com.util.result.Result;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final SnowflakeHelper idGenerator;

    private final MessageRepository messageRepository;

    public MessageController(MessageRepository messageRepository, SnowflakeHelper snowflakeHelper) {
        this.messageRepository = messageRepository;
        this.idGenerator = snowflakeHelper;
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
        Message msg = new Message(String.valueOf(idGenerator.snowflakeId()),
                id, sender, Instant.now(), message.getMsgBody(), false);
        messageRepository.save(msg);
        return Result.success(msg);
    }
    @GetMapping("/all")
    public Result<List<Message>> getMessages() {
        return Result.success(messageRepository.findAll());
    }

    @GetMapping("/filter")
    public Result<List<Message>> getFiltered() {
        return Result.success(messageRepository.findOffLineMessage("test2|test1"));
    }

    @GetMapping("/message/{id}")
    public Result<List<Message>> getBySenderId(@PathVariable String id) {
        return Result.success(messageRepository.findBySenderId(id));
    }

    @GetMapping("/all/my")
    public Result<List<Message>> getMyAll() {
        return Result.success(messageRepository.findMyALl());
    }
}
