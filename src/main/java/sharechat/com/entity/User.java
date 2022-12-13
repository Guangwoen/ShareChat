package sharechat.com.entity;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node("User")
@Data
@NoArgsConstructor
public class User {
    @Id
    private String Id;

    @Property
    private String name;

    @CreatedDate
    private Long createdAt;

    @Relationship(type = "DIRECTED", direction = Relationship.Direction.INCOMING)
    private Set<FriendRelation> sets = new HashSet<>();

    public User(String Id, String name) {
        this.Id = Id;
        this.name = name;
    }
}
