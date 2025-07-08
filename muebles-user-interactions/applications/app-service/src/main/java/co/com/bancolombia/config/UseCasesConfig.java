package co.com.bancolombia.config;

import co.com.bancolombia.model.stat.gateways.StatPersistenceGateway;
import co.com.bancolombia.model.stat.gateways.StatEventPublisher;
import co.com.bancolombia.usecase.stat.StatUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "co.com.bancolombia.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {

        @Bean
        public StatUseCase statUseCase(StatPersistenceGateway dynamoDbGateway, StatEventPublisher rabbitMQGateway){
                return new StatUseCase(dynamoDbGateway, rabbitMQGateway);
        }
}
