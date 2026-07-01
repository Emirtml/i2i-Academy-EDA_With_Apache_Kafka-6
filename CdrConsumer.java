import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

// Basic Java application to consume and parse messages from a Kafka topic
public class CdrConsumer {
    public static void main(String[] args) {

        // 1. Configure Kafka Consumer connection settings
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "51.21.254.24:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "i2i-cdr-group");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 2. Initialize the Kafka Consumer instance
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        // Define the target Kafka topic name to subscribe
        String topicName = "i2i-cdr-topic";

        // 3. Subscribe the consumer to our specific topic
        consumer.subscribe(Collections.singletonList(topicName));
        System.out.println("Consumer successfully started and listening to: " + topicName);

        // 4. Start an infinite loop to poll new data continuously
        try {
            while (true) {
                // Poll Kafka broker every 1000 milliseconds for new messages
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                // Process each message received from the topic
                for (ConsumerRecord<String, String> record : records) {
                    String jsonValue = record.value();

                    // 5. Use Jackson ObjectMapper to convert JSON String back to Java object
                    ObjectMapper objectMapper = new ObjectMapper();
                    CdrModel cdrData = objectMapper.readValue(jsonValue, CdrModel.class);

                    // 6. Print the reconstructed custom Java object cleanly to the console
                    System.out.println("Received Custom Object: " + cdrData.toString());
                }
            }
        } catch (Exception e) {
            // Print error stack trace if deserialization fails
            e.printStackTrace();
        } finally {
            // 7. Close the consumer securely if the loop breaks
            consumer.close();
        }
    }
}