package co.com.bancolombia.config;

import co.com.bancolombia.model.stat.gateways.StatPersistenceGateway;
import co.com.bancolombia.model.stat.gateways.StatEventPublisher;
import co.com.bancolombia.usecase.stat.StatUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.mock;

public class UseCasesConfigTest {

    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public StatPersistenceGateway statPersistenceGateway() {
            return mock(StatPersistenceGateway.class);
        }

        @Bean
        public StatEventPublisher statEventPublisher() {
            return mock(StatEventPublisher.class);
        }


        /*@Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }

        @Bean
        public StatUseCase statUseCase(StatPersistenceGateway dynamoDbGateway, StatEventPublisher rabbitMQGateway) {
            return new StatUseCase(dynamoDbGateway, rabbitMQGateway);
        }*/

    }

    /*static class MyUseCase {
        public String execute() {
            return "MyUseCase Test";
        }
    }*/
}