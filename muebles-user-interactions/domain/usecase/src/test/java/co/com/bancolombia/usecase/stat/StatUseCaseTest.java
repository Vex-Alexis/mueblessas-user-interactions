package co.com.bancolombia.usecase.stat;

import co.com.bancolombia.model.exceptions.InvalidHashException;
import co.com.bancolombia.model.stat.Stat;
import co.com.bancolombia.model.stat.gateways.StatEventPublisher;
import co.com.bancolombia.model.stat.gateways.StatPersistenceGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.DigestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // activa las anotaciones de Mockito
public class StatUseCaseTest {

    @Mock
    StatPersistenceGateway statPersistenceGateway;

    @Mock
    StatEventPublisher statEventPublisher;

    @InjectMocks
    StatUseCase statUseCase;


    @Test
    void shouldSaveStatWhenHashIsValid() {
        // Arrange
        Stat stat = createStat(); // usa el helper

        when(statPersistenceGateway.statSave(any())).thenReturn(Mono.just(stat));
        when(statEventPublisher.publishValidatedStat(any())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(statUseCase.createStat(stat))
                .expectNextMatches(result -> result.getTimestamp() != null)
                .verifyComplete();

        verify(statPersistenceGateway).statSave(any());
        verify(statEventPublisher).publishValidatedStat(any());
    }

    @Test
    void shouldReturnErrorWhenHashIsInvalid() {
        Stat stat = createStat();
        stat.setHash("hash_invalido");

        // Act & Assert
        StepVerifier.create(statUseCase.createStat(stat))
                .expectError(InvalidHashException.class)
                .verify();
    }


    private Stat createStat() {
        String raw = "250,25,10,100,100,7,8";
        String hash = DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));

        Stat stat = new Stat();
        stat.setTotalContactoClientes(250);
        stat.setMotivoReclamo(25);
        stat.setMotivoGarantia(10);
        stat.setMotivoDuda(100);
        stat.setMotivoCompra(100);
        stat.setMotivoFelicitaciones(7);
        stat.setMotivoCambio(8);
        stat.setHash(hash);

        return stat;
    }

}
