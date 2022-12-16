package sharechat.com.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

@Data
@NoArgsConstructor
@RelationshipProperties
public class FriendRelation {
    @RelationshipId
    @GeneratedValue
    private Long Id;

    private String name = "FRIEND_OF";

    @TargetNode
    private LinkNode linkNode1;

    @TargetNode
    private LinkNode linkNode2;

    public FriendRelation(Long Id, String name, LinkNode linkNode1, LinkNode linkNode2) {
        this.Id = Id;
        this.name = name;
        this.linkNode1 = linkNode1;
        this.linkNode2 = linkNode2;
    }
}
