package topic;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 描述： 接收三个等级的日志
 */
public class ReceiveLogsTopic1 {
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

        // 接收是和队列绑定的，和交换机不绑定
        // 创建临时队列
        String queueName = channel.queueDeclare().getQueue();
        // 同时绑定3个queue
        channel.queueBind(queueName, EXCAHNEGE_NAME, "*.orange.*");

        System.out.println("开始接收消息....");

        DefaultConsumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("收到消息：" + message +  " rountingKey为" + envelope.getRoutingKey());

            }
        };

        // 进行消费
        channel.basicConsume(queueName, true, consumer);

    }
}
