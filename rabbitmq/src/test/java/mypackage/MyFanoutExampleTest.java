package mypackage;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

/**
 *  fanout just broadcasts all the messages it receives to all the queues it knows.
 *  One sender send one message. Two receivers, every receive one message.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyFanoutExampleTest {

    public static final String MY_EXCHANGE = "FanoutExampleExchange";
    public static final String MY_QUEUE = "FanoutExampleQueue";
    private static RabbitMQContainer container = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));

    static String host = "localhost";
    static int port = 5672;

    @BeforeAll
    public static void beforeAll() {
        container.start();
//        container.followOutput(outputFrame -> {
//            System.out.print(outputFrame.getUtf8String());
//        });

        host = container.getHost();
        port = container.getAmqpPort();

        System.out.println("host: " + host);
        System.out.println("port: " + port);
        System.out.println("rabbitmq http url: " + container.getHttpUrl());
    }

    @AfterAll
    public static void afterAll() {
        container.stop();
    }

    void sendMessage() throws IOException, TimeoutException {
        System.out.println("sending ...");
        System.out.println("connect to " + host + ":" + port);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);

        try (
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()
        ) {
            boolean durable = true;
            channel.exchangeDeclare(MY_EXCHANGE, BuiltinExchangeType.FANOUT);

            {
                String message = "Hello World! 1111111";
                channel.basicPublish(MY_EXCHANGE, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();

                System.out.println("selectOk: " + selectOk);
                System.out.println(" [x] Sent '" + message + "'");
            }

        }
    }

    @Test
    void receiveMessage() throws IOException, TimeoutException, InterruptedException {
        System.out.println("receiving ...");
        System.out.println("connect to " + host + ":" + port);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);

        Semaphore semaphore1 = new Semaphore(0);
        Semaphore semaphore2 = new Semaphore(0);

        try (
            Connection connection1 = factory.newConnection();
            Connection connection2 = factory.newConnection();
            Channel channel1 = connection1.createChannel();
            Channel channel2 = connection2.createChannel();
        ) {

            receive(channel1, semaphore1);
            receive(channel2, semaphore2);

            sendMessage();

            System.out.println(" [*] Waiting for messages. Sem1");
            semaphore1.acquire(1);
            System.out.println(" [*] Waiting for messages. Sem2");
            semaphore2.acquire(1);
            System.out.println(" [*] get message and finish program");
        }

    }

    private void receive(Channel channel, Semaphore semaphore) throws IOException {
        channel.basicQos(1);
        channel.exchangeDeclare(MY_EXCHANGE, BuiltinExchangeType.FANOUT);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, MY_EXCHANGE, "");

        System.out.println(" [*] queuename: " + queueName);

        boolean autoAck = false;
        channel.basicConsume(queueName, autoAck, deliveryCallback(semaphore, channel), cancelCallback());
    }

    private DeliverCallback deliveryCallback(Semaphore semaphore, Channel channel) {
        return new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery delivery) throws IOException {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] consumerTag: " + consumerTag);
                    System.out.println(" [x] sleep " + Thread.currentThread());
                    System.out.println(" [x] Received '" + message + "'");
                } finally {
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                    semaphore.release();
                }
            }
        };
    }

    private CancelCallback cancelCallback() {
        return new CancelCallback() {
            @Override
            public void handle(String consumerTag) throws IOException {
                System.out.println("cancelcallback: " + consumerTag);
            }
        };
    }

}
