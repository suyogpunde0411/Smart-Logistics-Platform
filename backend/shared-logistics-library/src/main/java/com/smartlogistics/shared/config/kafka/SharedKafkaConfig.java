package com.smartlogistics.shared.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.ExponentialBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class SharedKafkaConfig {

    @Bean
    @ConditionalOnMissingBean(name = "sharedProducerFactory")
    public ProducerFactory<String, Object> sharedProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    @ConditionalOnMissingBean(name = "sharedKafkaTemplate")
    public KafkaTemplate<String, Object> sharedKafkaTemplate() {
        return new KafkaTemplate<>(sharedProducerFactory());
    }

    @Bean
    @ConditionalOnMissingBean(name = "sharedConsumerFactory")
    public ConsumerFactory<String, Object> sharedConsumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.smartlogistics.*");
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    @ConditionalOnMissingBean(name = "sharedKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, Object> sharedKafkaListenerContainerFactory(
            CommonErrorHandler sharedKafkaErrorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(sharedConsumerFactory());
        factory.setCommonErrorHandler(sharedKafkaErrorHandler);
        return factory;
    }

    @Bean
    @ConditionalOnMissingBean(name = "sharedKafkaErrorHandler")
    public CommonErrorHandler sharedKafkaErrorHandler(KafkaTemplate<String, Object> sharedKafkaTemplate) {
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(sharedKafkaTemplate);
        
        ExponentialBackOff backOff = new ExponentialBackOff();
        backOff.setInitialInterval(1000);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(10000);
        
        return new DefaultErrorHandler(recoverer, backOff);
    }
}
