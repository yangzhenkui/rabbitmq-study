package direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 描述： direct类型的交换机进行发送消息
 * direct即是相当于对路由key值进行精确匹配到对应的queue中去
 */
public class EmitLogDirect {
    private static final String EXCAHNEGE_NAME = "direct_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("1.14.48.38");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCAHNEGE_NAME, BuiltinExchangeType.DIRECT);

        String message = "info：Hello World";

        channel.basicPublish(EXCAHNEGE_NAME, "info", null, message.getBytes("UTF-8"));
        System.out.println("发送了消息, 等级为info,消息内容为" + message);

        message = "error：Hello World";
        channel.basicPublish(EXCAHNEGE_NAME, "error", null, message.getBytes("UTF-8"));
        System.out.println("发送了消息, 等级为error,消息内容为" + message);

        message = "warning：Hello World";
        channel.basicPublish(EXCAHNEGE_NAME, "warning", null, message.getBytes("UTF-8"));
        System.out.println("发送了消息, 等级为warning,消息内容为" + message);
        channel.close();
        connection.close();
    }
}
