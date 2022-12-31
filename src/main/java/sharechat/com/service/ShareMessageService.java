package sharechat.com.service;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sharechat.com.entity.LinkNode;
import sharechat.com.entity.Message;
import sharechat.com.entity.ShareMessage;
import sharechat.com.entity.UserInfo;
import sharechat.com.repository.LinkNodeRepository;
import sharechat.com.repository.MessageRepository;
import sharechat.com.repository.ShareMessageRepository;
import sharechat.com.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShareMessageService {

    private final String prefix = "SHARE_";

    private final String suffix = "_TARGET";
    private final MessageRepository messageRepository;
    private final ShareMessageRepository shareMessageRepository;

    private final UserRepository userRepository;

    private final MessageService messageService;

    private final LinkNodeRepository linkNodeRepository;

    private final RedisTemplate<String, String> redisTemplate;

    public ShareMessageService(MessageRepository messageRepository,
                               ShareMessageRepository shareMessageRepository,
                               MessageService messageService,
                               UserRepository userRepository,
                               LinkNodeRepository linkNodeRepository,
                               RedisTemplate<String, String> redisTemplate){
        this.messageRepository=messageRepository;
        this.shareMessageRepository=shareMessageRepository;
        this.linkNodeRepository = linkNodeRepository;
        this.redisTemplate = redisTemplate;
        this.messageService=messageService;
        this.userRepository=userRepository;
    }

    public boolean sendShareRequest(String sender, String receiver, String target) {
        String val = redisTemplate.opsForValue().get(prefix+receiver);
        if(val == null || val.length() == 0) {
            redisTemplate.opsForValue().set(prefix+receiver, sender);
            redisTemplate.opsForValue().set(receiver+"_"+sender+suffix, target);
            return false;
        }
        return true;
    }

    public boolean getShareRequest(String userId) {
        String val = redisTemplate.opsForValue().get(prefix+userId);
        return val != null && val.length() != 0;
    }

    public LinkNode getAllShareRequest(String userId) {
        return linkNodeRepository.getLinkNodeByUserId(redisTemplate.opsForValue().get(prefix+userId));
    }

    public List<Map<String,String>> getChannelMessages(String userId){
        String sender=redisTemplate.opsForValue().get(prefix+userId);
        String target=redisTemplate.opsForValue().get(userId+"_"+sender+suffix);
        List<Map<String,String>> returnList=new ArrayList<>();
        Map<String,String> firstMap=new HashMap<>();
        firstMap.put("userId",target);
        if(userRepository.findById(target).isPresent()){
            UserInfo targetInfo=userRepository.findById(target).get();
            firstMap.put("userName",targetInfo.getName());
            firstMap.put("avatar",targetInfo.getHeadPicture());
        }
        List<Map<String,String>> content=messageService.getRecentMessages(sender,target);
        for(Map<String,String> m:content){
            if(userRepository.findById(m.get("userId")).isPresent()){
                UserInfo userInfo=userRepository.findById(m.get("userId")).get();
                m.put("userName",userInfo.getName());
                m.put("avatar",userInfo.getHeadPicture());
            }
        }
        returnList.add(firstMap);
        returnList.addAll(content);
        return returnList;
    }

    public boolean endShare(String userId){
        String sender=redisTemplate.opsForValue().get(prefix+userId);
        String target=redisTemplate.opsForValue().get(userId+"_"+sender+suffix);
        List<ShareMessage> allUnloadedMessages1=shareMessageRepository.findAllUnloadedMessages(userId+"_"+target);
        List<ShareMessage> allUnloadedMessages2=shareMessageRepository.findAllUnloadedMessages(target+"_"+userId);
        for(ShareMessage m:allUnloadedMessages1){
            Message newMsg=new Message(m.getMsgId(),sender+"_"+target,userId,m.getMsgsendTime(),m.getMsgBody(),true);
            messageRepository.save(newMsg);
        }
        for(ShareMessage m:allUnloadedMessages2){
            Message newMsg=new Message(m.getMsgId(),target+"_"+sender,target,m.getMsgsendTime(),m.getMsgBody(),true);
            messageRepository.save(newMsg);
        }
        return true;
    }
}
