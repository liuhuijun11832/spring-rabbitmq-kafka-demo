import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description:
 * @Author: Joy
 * @Date: 2019-05-22 10:16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class KafkaTest {

    @Autowired
    private KafkaTemplate<Integer,String> kafkaTemplate;

    @Test
    public void test(){
        kafkaTemplate.send("default-topic", "hello world");
        kafkaTemplate.send("log-topic", "hello log");
    }

}
