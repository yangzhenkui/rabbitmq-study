package workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者：接收发送的消息
 */
public class Worker {
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
        final Channel channel = connection.createChannel();
        // 声明队列
        channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
        // 接受消息并消费，第二个参数autoACK即是进行反馈
        System.out.println("开始接收消息....");
        // 最希望处理的消息数量，表示在处理完这个数量的消息之前，不会接受下一个任务的
        channel.basicQos(1);
        channel.basicConsume(TASK_QUEUE_NAME, false, new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("收到了消息：" + message);
                try {
                    doWork(message);
                }finally {
                    System.out.println("消息队列已处理完成...");
                    // 如果取消autoACK机制，则一定需要手动进行消息的确认
                    // 在处理完之后把消息进行确认，第一个参数是模版，第二个参数表示是否需要一次性确认多个
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        });
    }

    private static void doWork(String task){
        char[] chars = task.toCharArray();
        for (char ch : chars) {
            if(ch == '.'){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
