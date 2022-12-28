package sharechat.com.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import sharechat.com.entity.Message;

public interface ShareMessageRepository extends CassandraRepository<Message, String> {

}
