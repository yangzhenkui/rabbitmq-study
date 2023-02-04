package workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *  描述： 存在多个任务，并且每个任务都比较耗时，生产者只管发送消息，不进行后续操作
 */
public class newTask {
    private final static String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置RabbitMQ的地址
        factory.setHost("1.14.48.38");
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setPort(5672);
        // 建立连接
        Connection connection = factory.newConnection();
        // 获取信道
        Channel channel = connection.createChannel();
        // 声明队列
        // 多个消费者时需要进行公平派遣
        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
        for (int i = 0; i < 10; i++) {
            String message;
            if(i%2==0){
                message = i + "....";
            }else{
                message = i + "";
            }
            channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println("已发送消息：" + message);
        }
        channel.close();
        connection.close();
    }
}
