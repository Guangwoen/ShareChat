package sharechat.com.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
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
    @Query("select * from chat_message where channelid = ?0 and isreceived = false ALLOW FILTERING")
    List<Message> findOffLineMessage(String channelId);

    /**
     * 根据接收者id获取离线消息
     *
     * @param channelId 聊天框id
     * */
    @Query("select * from chat_message where channelid like ?0 and isreceived = false ALLOW FILTERING")
    List<Message> findOffLineMessageByReceiver(String channelId);

    /**
     * 更新isReceived值
     *
     * @param msgId 消息id
     * @param value 目标布尔值
     * */
    @Query("update chat_message set isreceived = ?1 where msgid = ?0")
    void updateIsReceived(String msgId, boolean value);
}
