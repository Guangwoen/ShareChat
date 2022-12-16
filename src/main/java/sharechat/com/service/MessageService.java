package sharechat.com.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sharechat.com.entity.Message;
import sharechat.com.repository.MessageRepository;
import sharechat.com.util.SnowflakeHelper;

import java.util.List;

@Service
public class MessageService {

    private final SnowflakeHelper idGenerator;

    private final MessageRepository messageRepository;

    private final RedisTemplate<String, ?> redisTemplate;

    public MessageService(SnowflakeHelper snowflakeHelper,
                          MessageRepository messageRepository,
                          RedisTemplate<String, ?> redisTemplate) {
        this.idGenerator = snowflakeHelper;
        this.messageRepository = messageRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 随机生成ID
     * */
    public long getSnowflakeId() {
        return idGenerator.snowflakeId();
    }

    /**
     * 保存Message
     *
     * @param msg Message
     * */
    public void saveMsg(Message msg) {
        messageRepository.save(msg);
    }

    /**
     * 根据
     * */
    public Message getLast() {
        return null;
    }

    /**
     * 根据发送者获取信息
     *
     * @param id 发送者id
     * */
    public List<Message> findBySenderId(String id) {
        return messageRepository.findBySenderId(id);
    }

    /**
     * 获取所有信息
     * */
    public List<Message> findAllMsg() {
        return messageRepository.findAll();
    }

    /**
     * 查询redis是否存在未读消息
     *
     * @param id 查询者id
     * */
    public boolean isMissingInRedis(String id) {
        return redisTemplate.keys("*"+id).size() != 0;
    }

    /**
     * 查询数据库中是否存在未读消息
     *
     * @param id 查询者id
     * */
    public boolean isMissingInDB(String id) {
        List<Message> list = messageRepository.findOffLineMessageByReceiver("%"+id);
        return list.size() != 0;
    }
}
