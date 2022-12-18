package sharechat.com.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sharechat.com.entity.Message;
import sharechat.com.service.FriendService;
import sharechat.com.service.MessageService;
import sharechat.com.util.oss.AliyunOSSUtil;
import sharechat.com.util.result.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private final MessageService messageService;

    private final FriendService friendService;

    public MessageController(MessageService messageService,
                             FriendService friendService) {
        this.messageService = messageService;
        this.friendService = friendService;
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

    @PostMapping("/media")
    public Result<String> uploadMedia(@RequestPart("file") MultipartFile file,
                                      @RequestParam("type") String type) {
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

    /**
     * 根据id查询是否存在未读消息
     * */
    @GetMapping(value = "/alarm")
    public Result<Boolean> hasAlarm(@RequestParam("userId") String id) {
        System.out.println(id);
        return Result.success(messageService.isMissingInRedis(id) || messageService.isMissingInDB(id));
    }

    @GetMapping(value = "/list")
    public Result<List<Map<String, ?>>> getListById(@RequestParam("userId") String id) {
        System.out.println(id);
        return Result.success(messageService.getLatestMessageList(id));
    }

    @GetMapping(value = "/recent")
    public Result<List<Map<String, String>>> getRecent(@RequestParam("from") String id,
                                           @RequestParam("to") String friendId) {
        return Result.success(messageService.getRecentMessages(id, friendId));
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
