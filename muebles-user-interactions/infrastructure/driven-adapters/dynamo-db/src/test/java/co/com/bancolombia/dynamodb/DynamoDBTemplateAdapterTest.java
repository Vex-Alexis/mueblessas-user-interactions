package co.com.bancolombia.dynamodb;

import co.com.bancolombia.model.exceptions.DataPersistenceException;
import co.com.bancolombia.model.stat.Stat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@ExtendWith(MockitoExtension.class)
class DynamoDBTemplateAdapterTest {

    @Mock
    private DynamoDbEnhancedAsyncClient connectionFactory;

    @Mock
    private ObjectMapper mapper;

    private DynamoDBTemplateAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = Mockito.spy(new DynamoDBTemplateAdapter(connectionFactory, mapper));
    }

    @Test
    void shouldSaveStatSuccessfully() {
        // Arrange
        Stat stat = Stat.builder().timestamp("123456789").build();
        Mockito.doReturn(Mono.just(stat)).when(adapter).save(stat);

        // Act & Assert
        StepVerifier.create(adapter.statSave(stat))
                .expectNextMatches(saved -> saved.getTimestamp().equals("123456789"))
                .verifyComplete();

        Mockito.verify(adapter).save(stat);
    }

    @Test
    void shouldMapErrorWhenSavingFails() {
        // Arrange
        Stat stat = Stat.builder().timestamp("123456789").build();
        RuntimeException originalException = new RuntimeException("Failure in DynamoDB");

        Mockito.doReturn(Mono.error(originalException)).when(adapter).save(stat);

        // Act & Assert
        StepVerifier.create(adapter.statSave(stat))
                .expectErrorMatches(throwable ->
                        throwable instanceof DataPersistenceException &&
                                throwable.getMessage().contains("Error persistiendo en DynamoDB"))
                .verify();

        Mockito.verify(adapter).save(stat);
    }
}
