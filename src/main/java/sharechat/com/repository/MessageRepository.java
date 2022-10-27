package sharechat.com.repository;

import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Service;
import sharechat.com.entity.Message;

import java.util.List;

@Service
public interface MessageRepository extends CassandraRepository<Message, Integer> {

    @AllowFiltering
    List<Message> findBySenderId(String id);

    @Query("select * from message")
    List<Message> findMyALl();
}
