package fr.miage.demorabbitmqspring;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoRabbitMqSpringApplication {

    static final String topicExchangeName = "topic_logs";

    static final String queueName = "spring-boot";
    static final String queueNameSpecific = "spring-boot-specific";

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    Queue appQueueSpecific() {
        return new Queue(queueNameSpecific, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
    }

    @Bean
    public Binding declareBindingSpecific(Queue appQueueSpecific, TopicExchange exchange) {
        return BindingBuilder.bind(appQueueSpecific).to(exchange).with("foo.bar.#");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoRabbitMqSpringApplication.class, args);
    }

}
