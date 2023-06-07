package fr.miage.demorabbitmqspring;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Service
public class Receiver {

    @RabbitListener(queues = DemoRabbitMqSpringApplication.queueName)
    public void receiveMessage(final Message message) {
        try {
            String chaine = new String(message.getBody(), "UTF-8");
            System.out.println("Received message as a generic AMQP 'Message' wrapper: " + chaine);
            System.out.println("Header bar = "+message.getMessageProperties().getHeaders().get("bar"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Received message as a generic AMQP 'Message' wrapper: " + message);
            System.out.println("Header bar = "+message.getMessageProperties().getHeaders().get("bar"));
        }

    }

    @RabbitListener(queues = DemoRabbitMqSpringApplication.queueNameSpecific)
    public void receiveMessage(final CustomMessage customMessage, @Headers Map headersMap) {
        System.out.println("Received message and deserialized to 'CustomMessage': " + customMessage.toString());
        System.out.println("Header bar = "+headersMap.get("bar"));
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "auto.headers", autoDelete = "true",
                    arguments = @Argument(name = "x-message-ttl", value = "10000",
                            type = "java.lang.Integer")),
            exchange = @Exchange(value = "auto.headers", type = ExchangeTypes.HEADERS, autoDelete = "true"),
            arguments = {
                    @Argument(name = "x-match", value = "all"),
                    @Argument(name = "thing1", value = "somevalue"),
                    @Argument(name = "thing2")
            })
    )
    public void handleWithHeadersExchange(final CustomMessage customMessage, @Headers Map headersMap) {
        System.out.println("Received message on auto.headers and deserialized to 'CustomMessage': " + customMessage.toString());
        System.out.println("Header thing2 = "+headersMap.get("thing2"));
    }


}