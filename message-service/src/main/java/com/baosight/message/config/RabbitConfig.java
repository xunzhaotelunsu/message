package com.baosight.message.config;

import com.baosight.message.core.MessageConstants;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * Created by yang on 2018/6/19.
 */
@Configuration
public class RabbitConfig {

    @Value("${collector.concurrency.limit}")
    int collectorConcurrencyLimit;

    @Value("${sender.retry.limit}")
    int senderRetryLimit;

    @Bean
    public RabbitAdmin getRabbitAdmin(ConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareQueue(new Queue(MessageConstants.MESSAGE_QUEUE));//声明队列
        rabbitAdmin.declareQueue(new Queue(MessageConstants.MANUAL_QUEUE));
        rabbitAdmin.declareQueue(new Queue(MessageConstants.EMAIL));
        rabbitAdmin.declareQueue(new Queue(MessageConstants.SMS));
        rabbitAdmin.declareQueue(new Queue(MessageConstants.IM));
        rabbitAdmin.declareQueue(new Queue(MessageConstants.MOBILE));
        rabbitAdmin.declareQueue(new Queue(MessageConstants.WECHAT));
        return rabbitAdmin;
    }

    @Bean(name = "messageCollectorContainer")
    public SimpleRabbitListenerContainerFactory getMsgSimpleRabbitListenerContainerFactory(
            @Qualifier("messageCollectorExecutor") SimpleAsyncTaskExecutor executor,
            SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setTaskExecutor(executor);
        factory.setMaxConcurrentConsumers(4);
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean(name = "messageCollectorExecutor")
    public SimpleAsyncTaskExecutor getMsgSimpleAsyncTaskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor("messageCollectorExecutor");
        executor.setConcurrencyLimit(collectorConcurrencyLimit);
        return executor;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    @Bean(name = "messageSendContainer")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAdviceChain(RetryInterceptorBuilder
                .stateless()
                .maxAttempts(senderRetryLimit)
                .backOffOptions(1000,2,4000)
        .build());
        return factory;
    }
}
