package sharechat.com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("sharechatting")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShareMessage {

    @PrimaryKey
    private String msgId;

    private String channelId;

    private String senderId;

    private String msgsendTime;

    private String msgBody;

    private Boolean isReceived;
}
