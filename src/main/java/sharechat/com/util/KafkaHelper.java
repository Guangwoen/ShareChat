package sharechat.com.util;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;



public class KafkaHelper extends Thread {
    private KafkaConsumer<String, String> consumer;

    private final String topic = "test.topic";

    public KafkaHelper() {}

    @Override
    public void run() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "ytna");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "15000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        // 创建消费者对象
        consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Collections.singletonList(this.topic));

    }
}
