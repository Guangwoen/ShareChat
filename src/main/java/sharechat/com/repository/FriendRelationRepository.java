package sharechat.com.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;
import sharechat.com.entity.User;

@Repository
public interface FriendRelationRepository extends Neo4jRepository<User, String> {

}
