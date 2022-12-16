package sharechat.com.entity;

import lombok.*;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@Table("chatting")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @PrimaryKey
    private String msgId;

    private String channelId;

    private String senderId;

    private String msgsendTime;

    private String msgBody;

    private Boolean isReceived;
}
