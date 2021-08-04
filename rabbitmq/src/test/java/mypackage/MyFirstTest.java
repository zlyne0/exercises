package mypackage;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runners.MethodSorters;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

//@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyFirstTest {

    public static final String MY_FIRST_EXCHANGE = "pl.nn.gacek.gap.exchange";
    public static final String MY_FIRST_QUEUE = "pl.nn.gacek.gap.queue";
    private static RabbitMQContainer container = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));

    static String host = "localhost";
    static int port = 5672;

//    @BeforeAll
//    public static void beforeAll() {
//        container.withExchange(MY_FIRST_EXCHANGE, "direct")
//            .withQueue(MY_FIRST_QUEUE)
//            //.withBinding(MY_FIRST_EXCHANGE, MY_FIRST_QUEUE, new HashMap<>(), "#", "EXCHANGE");
//            .withBinding(MY_FIRST_EXCHANGE, MY_FIRST_QUEUE)
//        ;
//
//        container.start();
//        container.followOutput(outputFrame -> {
//            System.out.print(outputFrame.getUtf8String());
//        });
//        host = container.getHost();
//        port = container.getAmqpPort();
//
//        System.out.println("rabbitmq httpurl: " + container.getHttpUrl());
//    }

    @AfterAll
    public static void afterAll() {
        container.stop();
    }

    @Test
    @Order(1)
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
            //channel.queueDeclare(MY_R_QUEUE_NAME, true, false, false, null);

            String message = "Hello World!";
            channel.basicPublish(MY_FIRST_EXCHANGE, "#", null, message.getBytes());

            System.out.println(" [x] Sent '" + message + "'");
        }
    }

    @Test
    @Order(2)
    void receiveMessage() throws IOException, TimeoutException, InterruptedException {
        System.out.println("receiving ...");
        System.out.println("connect to " + host + ":" + port);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);

        Semaphore semaphore = new Semaphore(0);

        try (
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()
        ) {

            //channel.queueDeclare(MY_FIRST_QUEUE, true, false, false, null);


//            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//                String message = new String(delivery.getBody(), "UTF-8");
//                System.out.println(" [x] consumerTag: " + consumerTag);
//                System.out.println(" [x] Received '" + message + "'");
//                semaphore.release();
//            };
//            channel.basicConsume(MY_FIRST_QUEUE, true, deliverCallback, consumerTag -> {
//                System.out.println("cancelCallback: " + consumerTag);
//            });
            channel.basicConsume(MY_FIRST_QUEUE, true, "ccTtaagg", new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery delivery) throws IOException {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] consumerTag: " + consumerTag);
                    System.out.println(" [x] Received '" + message + "'");
                    semaphore.release();
                }
            }, new CancelCallback() {
                @Override
                public void handle(String consumerTag) throws IOException {
                    System.out.println("cancelcallback: " + consumerTag);
                }
            });

        }
        System.out.println(" [*] Waiting for messages.");
        semaphore.acquire(2);
        System.out.println(" [*] get message and finish program");
    }
}
