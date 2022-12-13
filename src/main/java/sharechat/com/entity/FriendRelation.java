package sharechat.com.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@NoArgsConstructor
@RelationshipProperties
public class FriendRelation {
    @Id
    @GeneratedValue
    private Long Id;

    private String name = "has friend of";

    @TargetNode
    private User user;

    public FriendRelation(Long Id, String name, User user) {
        this.Id = Id;
        this.name = name;
        this.user = user;
    }
}
