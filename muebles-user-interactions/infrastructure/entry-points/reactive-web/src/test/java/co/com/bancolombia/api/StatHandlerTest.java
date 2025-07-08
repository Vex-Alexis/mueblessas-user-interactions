package co.com.bancolombia.api;

import co.com.bancolombia.api.response.ApiResponseBuilder;
import co.com.bancolombia.api.mapper.StatMapper;
import co.com.bancolombia.api.dto.request.StatRequest;
import co.com.bancolombia.model.exceptions.DataPersistenceException;
import co.com.bancolombia.model.exceptions.InvalidHashException;
import co.com.bancolombia.model.exceptions.MessagePublishingException;
import co.com.bancolombia.model.stat.Stat;
import co.com.bancolombia.usecase.stat.StatUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StatHandlerTest {

    private StatUseCase statUseCase;
    private StatMapper statMapper;
    private StatHandler statHandler;

    private final ServerRequest requestMock = mock(ServerRequest.class);

    @BeforeEach
    void setUp() {
        statUseCase = mock(StatUseCase.class);
        statMapper = new StatMapper();
        ApiResponseBuilder apiResponseBuilder = new ApiResponseBuilder();
        statHandler = new StatHandler(statUseCase, statMapper, apiResponseBuilder);
    }

    @Test
    void shouldReturn201WhenRequestIsValid() {
        StatRequest request = createRequest();
        Stat domain = statMapper.requestToDomain(request);

        // Simulamos que el body del request es el request JSON
        when(requestMock.bodyToMono(StatRequest.class)).thenReturn(Mono.just(request));
        when(statUseCase.createStat(any())).thenReturn(Mono.just(domain));

        Mono<ServerResponse> responseMono = statHandler.listenPOSTStatUseCase(requestMock);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.CREATED))
                .verifyComplete();
    }

    @Test
    void shouldReturn400WhenHashIsInvalid() {
        StatRequest request = createRequest();

        when(requestMock.bodyToMono(StatRequest.class)).thenReturn(Mono.just(request));
        when(statUseCase.createStat(any())).thenReturn(Mono.error(new InvalidHashException("Hash inválido")));

        Mono<ServerResponse> responseMono = statHandler.listenPOSTStatUseCase(requestMock);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.BAD_REQUEST))
                .verifyComplete();
    }

    @Test
    void shouldReturn503WhenRabbitFails() {
        StatRequest request = createRequest();

        when(requestMock.bodyToMono(StatRequest.class)).thenReturn(Mono.just(request));
        when(statUseCase.createStat(any())).thenReturn(Mono.error(new MessagePublishingException("RabbitMQ down")));

        Mono<ServerResponse> responseMono = statHandler.listenPOSTStatUseCase(requestMock);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.SERVICE_UNAVAILABLE))
                .verifyComplete();
    }

    @Test
    void shouldReturn500WhenDynamoFails() {
        StatRequest request = createRequest();

        when(requestMock.bodyToMono(StatRequest.class)).thenReturn(Mono.just(request));
        when(statUseCase.createStat(any())).thenReturn(Mono.error(new DataPersistenceException("Error en Dynamo")));

        Mono<ServerResponse> responseMono = statHandler.listenPOSTStatUseCase(requestMock);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }

    @Test
    void shouldReturn500WhenUnexpectedError() {
        StatRequest request = createRequest();

        when(requestMock.bodyToMono(StatRequest.class)).thenReturn(Mono.just(request));
        when(statUseCase.createStat(any())).thenReturn(Mono.error(new RuntimeException("Otro error")));

        Mono<ServerResponse> responseMono = statHandler.listenPOSTStatUseCase(requestMock);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR))
                .verifyComplete();
    }

    @Test
    void shouldReturn200OnStatsTest() {
        Mono<ServerResponse> responseMono = statHandler.listenGETUseCase(requestMock);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.statusCode().equals(HttpStatus.OK))
                .verifyComplete();
    }

    private StatRequest createRequest() {
        return StatRequest.builder()
                .totalContactoClientes(1)
                .motivoReclamo(0)
                .motivoGarantia(0)
                .motivoDuda(0)
                .motivoCompra(1)
                .motivoFelicitaciones(0)
                .motivoCambio(0)
                .hash("5d7b5000e2f5cfd8806483f0b3d15ecf")
                .build();
    }
}