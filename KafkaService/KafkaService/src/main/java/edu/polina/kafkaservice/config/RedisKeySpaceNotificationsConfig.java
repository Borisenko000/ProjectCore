package edu.polina.kafkaservice.config;


import edu.polina.kafkaservice.keyspace.KeySpaceNotificationsProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisKeySpaceNotificationsConfig {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter messageListenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, new ChannelTopic("__keyevent@0__:expired"));
        return container;

    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(KeySpaceNotificationsProcessor processor) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(processor);
        messageListenerAdapter.setDefaultListenerMethod("handleKeySpaceNotification");
        messageListenerAdapter.setStringSerializer(new StringRedisSerializer());
        return messageListenerAdapter;
    }
}
