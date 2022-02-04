package mypackage;

import com.rabbitmq.client.*;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

/**
 * One consumer, one producer.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MySecondTest {

    public static final String MY_EXCHANGE = "secondExchange";
    public static final String MY_QUEUE = "secondQueue";
    private static RabbitMQContainer container = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));

    static String host = "localhost";
    static int port = 5672;

    @BeforeAll
    public static void beforeAll() {
        container.withExchange(MY_EXCHANGE, "direct")
            .withQueue(MY_QUEUE)
            //.withBinding(MY_FIRST_EXCHANGE, MY_FIRST_QUEUE, new HashMap<>(), "#", "EXCHANGE");
            .withBinding(MY_EXCHANGE, MY_QUEUE)
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
            boolean durable = true;
            channel.queueDeclare(MY_QUEUE, durable, false, false, null);

            {
                String message = "Hello World! 1111111";
                channel.basicPublish(MY_EXCHANGE, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();

                System.out.println("selectOk: " + selectOk);
                System.out.println(" [x] Sent '" + message + "'");
            }
            {
                String message = "Hello World! 22222222";
                channel.basicPublish(MY_EXCHANGE, "", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
                AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();

                System.out.println("selectOk: " + selectOk);
                System.out.println(" [x] Sent '" + message + "'");
            }
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

        Semaphore semaphore1 = new Semaphore(0);
        Semaphore semaphore2 = new Semaphore(0);

        try (
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            Channel channel2 = connection.createChannel()
        ) {
            channel.basicQos(1);
            channel2.basicQos(1);

            boolean autoAck = false;
            channel.basicConsume(MY_QUEUE, autoAck, deliveryCallback(semaphore1, channel), cancelCallback());
            channel2.basicConsume(MY_QUEUE, autoAck, deliveryCallback(semaphore2, channel2), cancelCallback());

            System.out.println(" [*] Waiting for messages. Sem1");
            semaphore1.acquire(1);
            System.out.println(" [*] Waiting for messages. Sem2");
            semaphore2.acquire(1);
            System.out.println(" [*] get message and finish program");
        }

    }

    private DeliverCallback deliveryCallback(Semaphore semaphore, Channel channel) {
        return new DeliverCallback() {
            @Override
            public void handle(String consumerTag, Delivery delivery) throws IOException {
                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [x] consumerTag: " + consumerTag);
                    System.out.println(" [x] sleep " + Thread.currentThread());
                    sleep(2000);
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

    private static void sleep(long milisec) {
        try {
            Thread.sleep(milisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
