package sharechat.com.entity;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

@Node("User")
@Data
@NoArgsConstructor
public class User {
    @Id
    private String Id;

    @Property(name = "name")
    private String name;

    @Relationship(type = "DIRECTED", direction = Relationship.Direction.INCOMING)
    private Set<User> sets = new HashSet<>();

    public User(String Id, String name) {
        this.Id = Id;
        this.name = name;
    }
}
