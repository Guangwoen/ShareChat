package sharechat.com.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import sharechat.com.entity.GroupNode;

@Repository
public interface GroupNodeRepository extends Neo4jRepository<GroupNode, Long> {
    @Override
    <S extends GroupNode> S save(S entity);

    @Query("match (n1: LinkNode), (n2: GroupNode) " +
            "where n1.userId = $userId and n2.name = $name " +
            "merge (n1)-[:BELONG]->(n2)")
    void linkWithLinkNode(String name, String userId);

    boolean existsGroupNodeByName(String name);
}
