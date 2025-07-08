package co.com.bancolombia.mq.sender;

import co.com.bancolombia.model.exceptions.MessagePublishingException;
import co.com.bancolombia.model.stat.Stat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;

public class ValidatedStatMessageSenderTest {
    private final RabbitTemplate rabbitTemplate = Mockito.mock(RabbitTemplate.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final String queueName = "cola";

    private final ValidatedStatMessageSender sender = new ValidatedStatMessageSender(rabbitTemplate, mapper, queueName);

    private Stat createStatWithValidHash() {
        return Stat.builder()
                .totalContactoClientes(250)
                .motivoReclamo(25)
                .motivoGarantia(10)
                .motivoDuda(100)
                .motivoCompra(100)
                .motivoFelicitaciones(7)
                .motivoCambio(8)
                .hash("02946f262f2eb0d8d5c8e76c50433ed8")
                .build();
    }

    @Test
    void shouldPublishStatSuccessfully() {
        Stat stat = createStatWithValidHash();
        Assertions.assertThatCode(() -> sender.publishValidatedStat(stat).block()).doesNotThrowAnyException();
    }

    @Test
    void shouldThrowWhenRabbitFails() {
        Mockito.doThrow(new AmqpConnectException(new Exception("fallo")))
                .when(rabbitTemplate).convertAndSend(anyString(), anyString());

        Stat stat = createStatWithValidHash();
        StepVerifier.create(sender.publishValidatedStat(stat))
                .expectError(MessagePublishingException.class)
                .verify();
    }
}
