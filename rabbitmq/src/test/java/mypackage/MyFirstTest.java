package mypackage;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

<<<<<<< HEAD
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
=======
import com.rabbitmq.client.*;
>>>>>>> a908f76109701951d1c70e7e2bb8fd53d21e7ae5
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

/**
 * One consumer, one producer.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MyFirstTest {

<<<<<<< HEAD
    public static final String MY_FIRST_EXCHANGE = "first.exchange";
    public static final String MY_FIRST_QUEUE = "first.queue";
=======
    public static final String MY_FIRST_EXCHANGE = "firstExchange";
    public static final String MY_FIRST_QUEUE = "firstQueue";
>>>>>>> a908f76109701951d1c70e7e2bb8fd53d21e7ae5
    private static RabbitMQContainer container = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));

    static String host = "localhost";
    static int port = 5672;

    @BeforeAll
    public static void beforeAll() {
        container.withExchange(MY_FIRST_EXCHANGE, "direct")
            .withQueue(MY_FIRST_QUEUE)
            //.withBinding(MY_FIRST_EXCHANGE, MY_FIRST_QUEUE, new HashMap<>(), "#", "EXCHANGE");
            .withBinding(MY_FIRST_EXCHANGE, MY_FIRST_QUEUE)
        ;

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
            channel.queueDeclare(MY_FIRST_QUEUE, true, false, false, null);

            channel.queueDeclare("XXX", false, false, false, null);

            String message = "Hello World!";
<<<<<<< HEAD
            channel.basicPublish("", "XXX", null, message.getBytes());
            AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
            System.out.println("selectOk: " + selectOk);

=======
            channel.basicPublish(MY_FIRST_EXCHANGE, "", null, message.getBytes());
            AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
            System.out.println("selectOk: " + selectOk);
>>>>>>> a908f76109701951d1c70e7e2bb8fd53d21e7ae5
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
            channel.queueDeclare("XXX", false, false, false, null);

<<<<<<< HEAD
            channel.basicConsume("XXX", false, new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery delivery) throws IOException {
                    String message = new String(delivery.getBody(), "UTF-8");
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                    System.out.println(" [x] consumerTag: " + consumerTag);
                    System.out.println(" [x] Received '" + message + "'");
                    semaphore.release();
=======
            channel.basicConsume(MY_FIRST_QUEUE, false, new DeliverCallback() {
                @Override
                public void handle(String consumerTag, Delivery delivery) throws IOException {
                    try {
                        String message = new String(delivery.getBody(), "UTF-8");
                        System.out.println(" [x] consumerTag: " + consumerTag);
                        System.out.println(" [x] Received '" + message + "'");
                    } finally {
                        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                        semaphore.release();
                    }
>>>>>>> a908f76109701951d1c70e7e2bb8fd53d21e7ae5
                }
            }, new CancelCallback() {
                @Override
                public void handle(String consumerTag) throws IOException {
                    System.out.println("cancelcallback: " + consumerTag);
                }
            });

            System.out.println(" [*] Waiting for messages.");
            semaphore.acquire(1);
            System.out.println(" [*] get message and finish program");
        }
<<<<<<< HEAD
        System.out.println(" [*] Waiting for messages.");
        semaphore.acquire(1);
        System.out.println(" [*] get message and finish program");
=======

>>>>>>> a908f76109701951d1c70e7e2bb8fd53d21e7ae5
    }
}
