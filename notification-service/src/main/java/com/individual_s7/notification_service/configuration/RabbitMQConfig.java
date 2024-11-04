package com.individual_s7.notification_service.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    // Friendship Request Configuration
    public static final String FRIEND_REQUEST_EXCHANGE = "friendRequestExchange";
    public static final String FRIEND_REQUEST_QUEUE = "friendRequestQueue";
    public static final String FRIEND_REQUEST_ROUTING_KEY = "friendRequestKey";

    @Bean
    public DirectExchange friendRequestExchange() {
        return new DirectExchange(FRIEND_REQUEST_EXCHANGE);
    }

    @Bean
    public Queue friendRequestQueue() {
        return QueueBuilder.durable(FRIEND_REQUEST_QUEUE).build();
    }

    @Bean
    public Binding bindingRequest(Queue friendRequestQueue, DirectExchange friendRequestExchange) {
        return BindingBuilder.bind(friendRequestQueue).to(friendRequestExchange).with(FRIEND_REQUEST_ROUTING_KEY);
    }

    // Friendship Event Configuration
    public static final String FRIENDSHIP_RESPONSE_EXCHANGE = "friendshipResponseExchange";
    public static final String NOTIFICATION_FRIENDSHIP_RESPONSE_QUEUE = "notificationFriendshipResponseQueue";
    public static final String FRIENDSHIP_RESPONSE_ROUTING_KEY = "friendshipResponseKey";

    @Bean
    public DirectExchange friendshipEventExchange() {
        return new DirectExchange(FRIENDSHIP_RESPONSE_EXCHANGE);
    }

    @Bean
    public Queue notificationFriendshipEventQueue() {
        return QueueBuilder.durable(NOTIFICATION_FRIENDSHIP_RESPONSE_QUEUE).build();
    }

    @Bean
    public Binding bindingEvent(Queue notificationFriendshipEventQueue, DirectExchange friendshipEventExchange) {
        return BindingBuilder.bind(notificationFriendshipEventQueue).to(friendshipEventExchange).with(FRIENDSHIP_RESPONSE_ROUTING_KEY);
    }
}
