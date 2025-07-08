package co.com.bancolombia.api.dto.response.success;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class APISuccessResponse<T> {
    private int httpStatus;
    private boolean success;
    private String message;
    private String timestamp;
    private T data;
}
