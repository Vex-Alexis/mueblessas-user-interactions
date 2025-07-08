package co.com.bancolombia.model.stat.gateways;

import co.com.bancolombia.model.stat.Stat;
import reactor.core.publisher.Mono;

public interface StatPersistenceGateway {
    Mono<Stat> statSave(Stat stat);
}
