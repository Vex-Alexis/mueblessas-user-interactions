package co.com.bancolombia.api.response;

import co.com.bancolombia.api.dto.response.error.APIErrorResponse;
import co.com.bancolombia.api.dto.response.success.APISuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApiResponseBuilder {

    public <T> Mono<ServerResponse> success(HttpStatus status, String message, T data) {
        APISuccessResponse<T> successResponse = APISuccessResponse.<T>builder()
                .httpStatus(status.value())
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .data(data)
                .build();

        return ServerResponse.status(status)
                .bodyValue(successResponse);
    }

    /*public Mono<ServerResponse> success(String message) {
        APISuccessResponse<Map<String, Object>> successResponse = APISuccessResponse.<Map<String, Object>>builder()
                .httpStatus(HttpStatus.OK.value())
                .success(true)
                .message(message)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .data(Map.of())
                .build();

        return ServerResponse.ok()
                .bodyValue(successResponse);
    }*/

    public Mono<ServerResponse> error(HttpStatus status, String message, String detail) {
        APIErrorResponse errorResponse = APIErrorResponse.builder()
                .httpStatus(status.value())
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .errorDetail(detail)
                .build();

        return ServerResponse.status(status)
                .bodyValue(errorResponse);
    }
}
