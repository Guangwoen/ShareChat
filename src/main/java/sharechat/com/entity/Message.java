package sharechat.com.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import org.springframework.data.neo4j.core.schema.GeneratedValue;

import java.time.Instant;


@Table("chat_message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @PrimaryKey
    private String msgId;

    private String channelId;

    private String senderId;

    private Instant sendTime;

    private String msgBody;

    private Boolean isReceived;
}
