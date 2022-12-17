package sharechat.com.entity;


import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

@Node("LinkNode")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkNode {
    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "userId")
    private String userId;

    @Property(name = "name")
    private String name;

    public LinkNode(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }
}
