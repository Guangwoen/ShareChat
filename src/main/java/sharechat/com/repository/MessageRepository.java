package sharechat.com.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sharechat.com.entity.Message;

import java.util.List;

@Repository
public interface MessageRepository extends CassandraRepository<Message, String> {

    /**
     * 根据发送者id查询聊天记录
     *
     * @param id 发送者id
     * @return 消息列表
     * */
    @AllowFiltering
    List<Message> findBySenderId(String id);

    /**
     * 获取离线消息
     *
     * @param channelId 消息框Id
     * @return 消息列表
     * */
    @Query("select * from chatting where channelid = ?0 and isreceived = false ALLOW FILTERING")
    List<Message> findOffLineMessage(String channelId);

    /**
     * 根据channelid获取前100条信息
     *
     * @param channelId 聊天框id
     * */
    @Query("select * from chatting where channelid = ?0 limit 100 ALLOW FILTERING")
    List<Message> findMessagesByChannelId(String channelId);

    /**
     * 根据channelid获取最新的一条信息
     *
     * @param channelId 聊天框id
     * */
    @Query("select * from chatting where channelid = ?0 limit 1 ALLOW FILTERING")
    Message findMessagesByChannelIdLatestFull(String channelId);

    /**
     * 根据channelid获取最新的一条信息（通配符）
     *
     * @param channeldId 聊天框id
     * */
    @Query("select * from chatting where channelid like ?0 limit 1")
    Message findMessageByChannelIdLatest(String channeldId);

    /**
     * 根据channelid获取离线消息（通配符）
     *
     * @param channelId 聊天框id
     * */
    @Query("select * from chatting where channelid like ?0 and isreceived = false ALLOW FILTERING")
    List<Message> findOffLineMessagesByChannelId(String channelId);

    /**
     * 根据channelid获取离线消息
     *
     * @param channelId 聊天框id
     * */
    @Query("select * from chatting where channelid = ?0 and isreceived = false ALLOW FILTERING")
    List<Message> findOffLineMessagesByChannelIdFull(String channelId);

    /**
     * 更新isReceived值
     *
     * @param msgId 消息id
     * @param value 目标布尔值
     * */
    @Query("update chatting set isreceived = ?3 where msgid = ?0 and msgsendtime = ?1 and senderid = ?2")
    void updateIsReceived(String msgId, String msgsendtime, String senderid,  boolean value);


}
