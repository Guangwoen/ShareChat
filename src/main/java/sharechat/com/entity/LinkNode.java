package sharechat.com.entity;


import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

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

    @Property(name = "avatar")
    private String avatar;

    public LinkNode(String userId, String name, String avatar) {
        this.userId = userId;
        this.name = name;
        this.avatar = avatar;
    }
}
