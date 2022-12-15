package sharechat.com.repository;

import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import sharechat.com.entity.User;

import java.util.List;

@Repository
public interface FriendRelationRepository extends Neo4jRepository<User, String> {
    Long countUserByName(String name);
}
