package sharechat.com.service;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sharechat.com.entity.LinkNode;
import sharechat.com.entity.Message;
import sharechat.com.repository.MessageRepository;
import sharechat.com.util.SnowflakeHelper;

import java.util.*;

@Service
public class MessageService {

    private final SnowflakeHelper idGenerator;

    private final MessageRepository messageRepository;

    private final RedisTemplate<String, ?> redisTemplate;

    private final FriendService friendService;

    public MessageService(SnowflakeHelper snowflakeHelper,
                          MessageRepository messageRepository,
                          RedisTemplate<String, ?> redisTemplate,
                          FriendService friendService) {
        this.idGenerator = snowflakeHelper;
        this.messageRepository = messageRepository;
        this.redisTemplate = redisTemplate;
        this.friendService = friendService;
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

    public List<Map<String, ?>> getLatestMessageList(String id) {
        List<Map<String, ?>> returnLst = new ArrayList<>();
        List<LinkNode> lst = friendService.getAllFriend(id);
        for(LinkNode e: lst) {
            Map<String, Object> mp = new HashMap<>();
            Message latest = getLastFull(id, e.getName());
            if(latest != null) {
                mp.put("msg", latest.getMsgBody());
                mp.put("time", latest.getMsgsendTime());
            }
            else {
                mp.put("msg", null);
                mp.put("time", null);
            }
            mp.put("username", e.getName());
            mp.put("userId", e.getName());
            mp.put("unread", isMissingInDBFull(e.getName()+"_"+id)
                    || isMissingInRedisFull(e.getName() +"_"+id));

            returnLst.add(mp);
        }

        Collections.sort(returnLst, (map1, map2) ->{
            String s1 = (String) map1.get("time");
            String s2 = (String) map2.get("time");
            System.out.println(map1.get("userId")+"_"+map2.get("userId"));
            if(s1 != null && s2 != null) return s2.compareTo(s1);
            if(s1 == null) return 1;
            return -1;
        });
        return returnLst;
    }

    /**
     * 根据id获取最新的消息（包括未读消息）（通配符）
     * */
    public Message getLast(String id) {
        ListOperations<String, Message> listOperations = (ListOperations<String, Message>) redisTemplate.opsForList();
        if(isMissingInRedis(id)) {
            Message m1 = listOperations.leftPop("*"+id);
            Message m2 = listOperations.leftPop(id+"*");
            return m1.getMsgsendTime().compareTo(m2.getMsgsendTime()) < 0 ? m1 : m2;
        }
        return messageRepository.findMessageByChannelIdLatest("%"+id+"%");
    }

    public Message getLastFull(String myId, String friendId) {
        ListOperations<String, Message> listOperations = (ListOperations<String, Message>) redisTemplate.opsForList();
        if(isMissingInRedis(friendId)) {
            Message m1 = listOperations.leftPop(friendId+"_"+myId);
            Message m2 = listOperations.leftPop(myId+"_"+friendId);
            return m1.getMsgsendTime().compareTo(m2.getMsgsendTime()) < 0 ? m2 : m1;
        }
        Message m1 = messageRepository.findMessagesByChannelIdLatestFull(myId+"_"+friendId);
        Message m2 = messageRepository.findMessagesByChannelIdLatestFull(friendId+"_"+myId);
        if(m1 != null && m2 != null) {
            return m1.getMsgsendTime().compareTo(m2.getMsgsendTime()) < 0 ? m2 : m1;
        }
        if(m1 != null)
            return m1;
        return m2;
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
     * 查询redis是否存在未读消息（通配符）
     *
     * @param id 查询者id
     * */
    public boolean isMissingInRedis(String id) {
        return redisTemplate.keys("*"+id).size() != 0;
    }

    /**
     * 查询redis是否存在未读消息
     *
     * @param channelId 聊天框id
     * */
    public boolean isMissingInRedisFull(String channelId) {
        return redisTemplate.keys(channelId).size() != 0;
    }

    /**
     * 查询数据库中是否存在未读消息（通配符）
     *
     * @param id 查询者id
     * */
    public boolean isMissingInDB(String id) {
        List<Message> list = messageRepository.findOffLineMessagesByChannelId("%"+id);
        return list.size() != 0;
    }

    /**
     * 查询数据库中是否存在未读消息
     *
     * @param channelId 聊天框id
     * */
    public boolean isMissingInDBFull(String channelId) {
        List<Message> list = messageRepository.findOffLineMessagesByChannelIdFull(channelId);
        return list.size() != 0;
    }
}
