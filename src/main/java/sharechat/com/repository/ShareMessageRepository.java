package sharechat.com.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import sharechat.com.entity.Message;

@Repository
public interface ShareMessageRepository extends CassandraRepository<Message, String> {

}
