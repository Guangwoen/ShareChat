package sharechat.com.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.annotation.Generated;
import java.time.Instant;


@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    @PrimaryKey
    private String channelId;

    private String senderId;

    @CreatedDate
    private Instant sendTime;

    private String msgBody;
}
