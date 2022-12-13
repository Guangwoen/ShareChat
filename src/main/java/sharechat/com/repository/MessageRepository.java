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
     * 查询所有聊天记录
     *
     * @return 消息列表
     * */
    @Query("select * from message")
    List<Message> findMyALl();
}
