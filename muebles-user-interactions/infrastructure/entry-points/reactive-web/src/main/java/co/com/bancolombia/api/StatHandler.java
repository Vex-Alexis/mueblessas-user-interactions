package co.com.bancolombia.api;

import co.com.bancolombia.api.dto.request.StatRequest;
import co.com.bancolombia.api.mapper.StatMapper;
import co.com.bancolombia.api.response.ApiResponseBuilder;
import co.com.bancolombia.model.exceptions.DataPersistenceException;
import co.com.bancolombia.model.exceptions.InvalidHashException;
import co.com.bancolombia.model.exceptions.MessagePublishingException;
import co.com.bancolombia.usecase.stat.StatUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class StatHandler {

    private final StatUseCase statUseCase;
    private final StatMapper statMapper;
    private final ApiResponseBuilder apiResponseBuilder;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("Testeando...");
    }

    public Mono<ServerResponse> listenPOSTStatUseCase(ServerRequest request) {
        return request.bodyToMono(StatRequest.class)
                .doOnNext(req -> log.info("Solicitud recibida: {}", req))
                .map(statMapper::requestToDomain) // Mejor extraer esto a un Mapper
                .flatMap(statUseCase::createStat)
                .flatMap(stat ->
                        apiResponseBuilder.success(HttpStatus.CREATED,
                                        "Estadística almacenada en DynamoDB y evento enviado a RabbitMQ", stat)
                                .doOnSuccess(resp -> log.info("Estadística procesada correctamente. Timestamp: {}, Hash: {}", stat.getTimestamp(), stat.getHash()))
                )
                .onErrorResume(InvalidHashException.class, e ->
                        apiResponseBuilder.error(HttpStatus.BAD_REQUEST, "No fue posible procesar la estadística", e.getMessage())
                )
                .onErrorResume(DataPersistenceException.class, e ->
                        apiResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "No fue posible almacenar la estadística en DynamoDB", e.getMessage())
                )
                .onErrorResume(MessagePublishingException.class, e ->
                        apiResponseBuilder.error(HttpStatus.SERVICE_UNAVAILABLE, "No fue posible publicar el evento en RabbitMQ", e.getMessage())
                )
                .onErrorResume(Throwable.class, e -> {
                    log.error("Error inesperado procesando estadística. Ex: {}", e.getMessage());
                    return apiResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", e.getMessage());
                });
    }
}
