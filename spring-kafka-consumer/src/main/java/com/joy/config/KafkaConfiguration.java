package com.joy.config;

import com.alibaba.ttl.threadpool.TtlExecutors;
import io.jaegertracing.internal.MDCScopeManager;
import io.opentracing.*;
import io.opentracing.contrib.java.spring.jaeger.starter.TracerBuilderCustomizer;
import io.opentracing.contrib.kafka.TracingKafkaUtils;
import io.opentracing.contrib.kafka.spring.TracingConsumerFactory;
import io.opentracing.contrib.kafka.spring.TracingKafkaAspect;
import io.opentracing.contrib.kafka.spring.TracingProducerFactory;
import io.opentracing.tag.Tags;
import io.opentracing.util.GlobalTracer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Description: kafka配置
 * @Author: Joy
 * @Date: 2019-05-22 10:06
 */
@Slf4j
@Configuration
public class KafkaConfiguration {

    @Value("${bootstrap.servers}")
    private String brokerAddress;

    @Value("${enable.auto.commit}")
    private boolean enableAutoCommit;

    @Value("${auto.commit.interval.ms}")
    private String autoCommitIntervalMs;

    @Value("${session.timeout}")
    private String sessionTimeout;


    @Bean
    public KafkaAdmin admin(){
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> kafkaListenerContainerFactory(Tracer tracer){
        ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory();
        concurrentKafkaListenerContainerFactory.setConcurrency(3);
//        concurrentKafkaListenerContainerFactory.getContainerProperties().setConsumerTaskExecutor();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory(tracer));
        concurrentKafkaListenerContainerFactory.setRecordInterceptor(record -> {
            Tracer.SpanBuilder spanBuilder = tracer.buildSpan("interceptor" + record.topic())
                    .withTag(Tags.SPAN_KIND.getKey(), Tags.SPAN_KIND_CONSUMER);

            SpanContext parentContext = TracingKafkaUtils.extractSpanContext(record.headers(), tracer);
            if (parentContext != null) {
                spanBuilder.addReference(References.FOLLOWS_FROM, parentContext);
            }
            Span span = spanBuilder.start();
            try (Scope ignored = tracer.activateSpan(span)) {
                log.info("message:{}", record.value());
            } catch (Exception e) {
                Tags.ERROR.set(span, Boolean.TRUE);
                throw e;
            } finally {
                span.finish();
            }
            return record;
        });
        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    public ConsumerFactory<Integer,String> consumerFactory(Tracer tracer){
        return new TracingConsumerFactory<>(new DefaultKafkaConsumerFactory<>(consumerConfigs()), tracer);
    }

    @Bean
    public Map<String,Object> consumerConfigs(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddress);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, autoCommitIntervalMs);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "86400000");
        return props;
    }

    @Bean
    public Map<String, Object> producerConfigs(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerAddress);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<Integer, String> producerFactory(Tracer tracer){
        return new TracingProducerFactory<>(new DefaultKafkaProducerFactory<>(producerConfigs()), tracer);
    }

    @Bean
    public KafkaTemplate<Integer, String> kafkaTemplate(Tracer tracer){
        return new KafkaTemplate<>(producerFactory(tracer));
    }

    @Bean
    public TracingKafkaAspect tracingKafkaAspect(Tracer tracer){
        return new TracingKafkaAspect(tracer);
    }



    @Bean
    public TracerBuilderCustomizer mdcBuilder(){
        return builder -> builder.withScopeManager(new MDCScopeManager.Builder().build());
    }


    @Bean
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(100);
        threadPoolTaskExecutor.setMaxPoolSize(200);
        threadPoolTaskExecutor.setQueueCapacity(1024);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(60);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return TtlExecutors.getTtlExecutor(threadPoolTaskExecutor);
    }
}
