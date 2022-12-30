package sharechat.com.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;
import sharechat.com.entity.Message;
import sharechat.com.entity.ShareMessage;

import java.util.List;

@Repository
public interface ShareMessageRepository extends CassandraRepository<ShareMessage, String> {
    @Query("select * from sharechatting where channelid = ?0 and isrecived = false ALLOW FILTRING")
    List<ShareMessage> findAllUnloadedMessages(String channelId);
}
