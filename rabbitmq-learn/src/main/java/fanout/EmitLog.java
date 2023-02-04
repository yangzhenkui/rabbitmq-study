package fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送日志信息
 * 交换机模式为fanout：即是不需要指定路由key
 */
public class EmitLog {
    private static final String EXCAHNEGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("1.14.48.38");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCAHNEGE_NAME, BuiltinExchangeType.FANOUT);

        String message = "info: Hello World";

        channel.basicPublish(EXCAHNEGE_NAME, "", null, message.getBytes("UTF-8"));
        System.out.println("发送了消息：" + message);
        channel.close();
        connection.close();
    }
}
