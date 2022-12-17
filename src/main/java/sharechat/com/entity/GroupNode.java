package sharechat.com.entity;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Node("GroupNode")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupNode {
    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")
    private String name;

    public GroupNode(String name) {
        this.name = name;
    }
}
