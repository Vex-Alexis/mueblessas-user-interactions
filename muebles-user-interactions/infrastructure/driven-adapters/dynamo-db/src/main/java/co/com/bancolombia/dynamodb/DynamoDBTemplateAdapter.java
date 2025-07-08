package co.com.bancolombia.dynamodb;

import co.com.bancolombia.dynamodb.entity.StatEntity;
import co.com.bancolombia.dynamodb.helper.TemplateAdapterOperations;
import co.com.bancolombia.model.exceptions.DataPersistenceException;
import co.com.bancolombia.model.stat.Stat;
import co.com.bancolombia.model.stat.gateways.StatPersistenceGateway;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;

@Slf4j
@Repository
public class DynamoDBTemplateAdapter extends TemplateAdapterOperations<Stat, String, StatEntity> implements StatPersistenceGateway {

    public DynamoDBTemplateAdapter(DynamoDbEnhancedAsyncClient connectionFactory, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(connectionFactory, mapper, d -> mapper.map(d, Stat.class), "interaction_stats");
    }

    @Override
    public Mono<Stat> statSave(Stat stat) {
        return super.save(stat)
                .doOnSuccess(saved -> log.info("Estadística almacenada en DynamoDB. Timestamp: {}", saved.getTimestamp()))
                .doOnError(error -> log.error("Error almacenando estadística en DynamoDB. Message {}", error.getMessage()))
                .onErrorMap(e -> new DataPersistenceException("Error persistiendo en DynamoDB. Exception message: " + e.getMessage()));
    }




}
