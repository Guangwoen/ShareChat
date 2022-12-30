package sharechat.com.service;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sharechat.com.entity.LinkNode;
import sharechat.com.entity.Message;
import sharechat.com.repository.LinkNodeRepository;
import sharechat.com.repository.MessageRepository;
import sharechat.com.repository.ShareMessageRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShareMessageService {

    private final String prefix = "SHARE_";

    private final String suffix = "_TARGET";
    private final MessageRepository messageRepository;
    private final ShareMessageRepository shareMessageRepository;

    private final LinkNodeRepository linkNodeRepository;

    private final RedisTemplate<String, String> redisTemplate;

    public ShareMessageService(MessageRepository messageRepository,
                               ShareMessageRepository shareMessageRepository,
                               LinkNodeRepository linkNodeRepository,
                               RedisTemplate<String, String> redisTemplate){
        this.messageRepository=messageRepository;
        this.shareMessageRepository=shareMessageRepository;
        this.linkNodeRepository = linkNodeRepository;
        this.redisTemplate = redisTemplate;
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
}
