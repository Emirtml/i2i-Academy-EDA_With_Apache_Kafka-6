import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Properties;

// Basic Java application to publish messages to a Kafka topic
public class CdrProducer {
    public static void main(String[] args) {
        
        // 1. Configure Kafka Producer connection settings
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "51.21.254.24:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 2. Initialize the Kafka Producer instance
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        // Define the target Kafka topic name
        String topicName = "i2i-cdr-topic";

        // 3. Create a sample custom object using our CdrModel
        CdrModel cdrData = new CdrModel("cdr-id-001", "5559876543", 150);

        try {
            // 4. Use Jackson ObjectMapper to convert Java object to JSON String (Serialization)
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonString = objectMapper.writeValueAsString(cdrData);

            // 5. Wrap the data inside a ProducerRecord (Topic, Key, Value)
            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, cdrData.getId(), jsonString);

            // 6. Send the serialized data packet to Apache Kafka broker
            producer.send(record);
            System.out.println("Producer successfully sent data: " + jsonString);

        } catch (Exception e) {
            // Print error stack trace if serialization or transmission fails
            e.printStackTrace();
        } finally {
            // 7. Flush and close the producer to prevent memory leaks
            producer.flush();
            producer.close();
        }
    }
}