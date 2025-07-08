package co.com.bancolombia.api.dto.response.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class APIErrorResponse {
    private int httpStatus;
    private boolean success;
    private String message;
    private String timestamp;
    private String errorDetail;
}
