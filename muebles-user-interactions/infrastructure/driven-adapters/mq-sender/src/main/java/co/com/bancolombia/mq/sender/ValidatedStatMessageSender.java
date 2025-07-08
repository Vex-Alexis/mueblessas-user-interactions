package co.com.bancolombia.mq.sender;

import co.com.bancolombia.model.exceptions.MessagePublishingException;
import co.com.bancolombia.model.stat.Stat;
import co.com.bancolombia.model.stat.gateways.StatEventPublisher;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
//@RequiredArgsConstructor
public class ValidatedStatMessageSender implements StatEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper mapper;

    private final String queueName;

    public ValidatedStatMessageSender(RabbitTemplate rabbitTemplate, ObjectMapper mapper, @Value("${queue.name}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.mapper = mapper;
        this.queueName = queueName;
    }

    @Override
    public Mono<Void> publishValidatedStat(Stat stat) {
        return Mono.fromRunnable(() -> {
            try {
                String json = mapper.writeValueAsString(stat);
                log.info("Publicando mensaje a RabbitMQ. Cola: {}, Payload: {}", queueName, json);
                rabbitTemplate.convertAndSend(queueName, json);
            } catch (AmqpConnectException e) {
                log.error("Error de conexión con RabbitMQ. Message: {}", e.getMessage());
                throw new MessagePublishingException("Error de conexión con RabbitMQ", e);
            } catch (Exception e) {
                log.error("Error inesperado publicando mensaje a RabbitMQ. Ex: {}", e.getMessage());
                throw new MessagePublishingException("Error inesperado publicando mensaje", e);
            }
        });
    }

}
