package sharechat.com.repository;

import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import sharechat.com.entity.LinkNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

@Repository
public interface LinkNodeRepository extends Neo4jRepository<LinkNode, Long> {
    @Query("match (n1: LinkNode), (n2: LinkNode) " +
            "where n1.name = $a and n2.name = $b " +
            "merge (n1)-[:FRIEND]->(n2)")
    void makeLink(String a, String b);

    @Query("match (a)-[:FRIEND]-(b) where a.name = $name " +
            "return b ")
    List<LinkNode> getFriendsByName(String name);

    @Query("match (a)-[:FRIEND]-(b) where a.userId = $id " +
            "return b")
    List<LinkNode> getFriendsById(String id);

    @Query("match (n1: LinkNode), (n2: LinkNode), p=(n1)-[]-(n2) " +
            "where n1.userId = $userId and n2.userId = $friendId " +
            "return count(p)")
    Long isLinked(String userId, String friendId);

    List<LinkNode> getLinkNodesByUserId(String userId);

    List<LinkNode> getLinkNodesByName(String name);
}