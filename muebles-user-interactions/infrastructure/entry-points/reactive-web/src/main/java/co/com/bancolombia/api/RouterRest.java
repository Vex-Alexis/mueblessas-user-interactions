package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    private static final String BASE_PATH = "/api/stats";

    @Bean
    public RouterFunction<ServerResponse> routerFunction(StatHandler handler) {
        return route(GET(BASE_PATH + "/test"), handler::listenGETUseCase)
                .and(route(POST(BASE_PATH), handler::listenPOSTStatUseCase));
    }
}
