package topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 描述： topic类型的交换机进行发送消息
 * direct即是相当于对路由key值进行精确匹配到对应的queue中去
 * topic相当于针对路由键进行模糊匹配，其中*代表一个单词，#表示0个或者多个
 */
public class EmitLogTopic {
    private static final String EXCAHNEGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("1.14.48.38");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCAHNEGE_NAME, BuiltinExchangeType.TOPIC);

        String message = "Animal World";

        String[] routingKeys = new String[9];
        routingKeys[0] = "quick.orange.rabbit";
        routingKeys[1] = "lazy.orange.elephant";
        routingKeys[2] = "quick.orange.fox";
        routingKeys[3] = "lazy.brown.fox";
        routingKeys[4] = "lazy.pink.rabbit";
        routingKeys[5] = "quick.brown.fox";
        routingKeys[6] = "orange";
        routingKeys[7] = "quick.orange.male.rabbit";
        routingKeys[8] = "lazy.orange.male.rabbit";
        for (String routingKey : routingKeys) {
            // 发送消息,指定routingKey，接收消息时也是通过routingKey来进行接收，交换机与队列绑定时指定routingKey
            // topic主要通过发送端时的指定的routingKey与接收端的指定的模糊routingKey进行匹配
            // 其中发送端的routingKey是为精确的模糊的均可
            // 接收端接收信息的routingKey一般是采用模糊匹配的方式，如*表示一个单词，#表示0个或者多个单词
            channel.basicPublish(EXCAHNEGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println("发送了" + message + ", routingKey为：" + routingKey);
        }
        channel.basicPublish(EXCAHNEGE_NAME, "*.orange.*", null, message.getBytes("UTF-8"));
        channel.close();
        connection.close();
    }
}
