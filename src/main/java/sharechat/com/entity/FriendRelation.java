package sharechat.com.entity;

import lombok.Builder;
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
    private User user1;

    @TargetNode
    private User user2;

    public FriendRelation(Long Id, String name, User user1, User user2) {
        this.Id = Id;
        this.name = name;
        this.user1 = user1;
        this.user2 = user2;
    }
}
