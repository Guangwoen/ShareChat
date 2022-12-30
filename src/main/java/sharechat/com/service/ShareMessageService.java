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

    public void sendShareRequest(String sender, String receiver, String target) {
        ListOperations<String, String> listOperations = (ListOperations<String, String>) redisTemplate.opsForList();
        listOperations.rightPush(prefix+sender, receiver);
        listOperations.rightPush(receiver+suffix, target);
    }

    public boolean getShareRequest(String userId) {
        return redisTemplate.opsForList().size(prefix+userId) != 0;
    }

    public List<LinkNode> getAllShareRequest(String userId) {
        List<LinkNode> returnList = new ArrayList<>();
        for(int i = 0; i < redisTemplate.opsForList().size(prefix+userId); i++) {
            returnList.add(linkNodeRepository.getLinkNodeByUserId(
                    redisTemplate.opsForList().leftPop(prefix+userId))
            );
        }
        return returnList;
    }
}
