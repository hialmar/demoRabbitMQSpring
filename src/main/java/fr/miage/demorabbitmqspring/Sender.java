package fr.miage.demorabbitmqspring;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Sender implements CommandLineRunner {

    private final AmqpTemplate template;

    public Sender(AmqpTemplate template) {
        this.template = template;
    }

    @Override
    public void run(String... args) throws Exception {
        final var message = new CustomMessage("Hello there!", 50, false);
        System.out.println("Sending message...");
        template.convertAndSend(DemoRabbitMqSpringApplication.topicExchangeName, "foo.bar.baz", message);

        System.out.println("Sending message...");
        template.convertAndSend(DemoRabbitMqSpringApplication.topicExchangeName, "foo.bar.baz", message, m -> {
            m.getMessageProperties().setHeader("bar", "baz");
            return m;
        });

    }

    public void sendMessage(CustomMessage customMessage, String routingKey, String bar) {
        template.convertAndSend(DemoRabbitMqSpringApplication.topicExchangeName, routingKey, customMessage, m -> {
            m.getMessageProperties().setHeader("bar", bar);
            System.out.println("Message : "+m);
            return m;
        });
    }

}