package sharechat.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sharechat.com.entity.Message;
import sharechat.com.repository.MessageRepository;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    /**
     * 保存客户端发送的信息(POST)
     *
     * @param message 发送的消息
     * @return 是否成功
     * */
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public Message saveMessage(@RequestBody Message message) {
        String id = message.getChannelId();
        if(id == null || id.length() == 0) {
            System.out.println("<<报错>>: 空channelId");
            return null;
        }
        String sender = message.getSenderId();
        if(sender == null || sender.length() == 0) {
            System.out.println("<<报错>>: 空senderId");
            return null;
        }
        Message msg = new Message(id, sender,
                Instant.now(), message.getMsgBody());
        messageRepository.save(msg);
        return msg;
    }
    @GetMapping("/all")
    public List<Message> getMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/id/{id}")
    public List<Message> getBySenderId(@PathVariable String id) {
        return messageRepository.findBySenderId(id);
    }

    @GetMapping("/all/my")
    public List<Message> getMyAll() {
        return messageRepository.findMyALl();
    }
}
