package co.com.bancolombia.usecase.stat;

import co.com.bancolombia.model.exceptions.InvalidHashException;
import co.com.bancolombia.model.stat.Stat;
import co.com.bancolombia.model.stat.gateways.StatPersistenceGateway;
import co.com.bancolombia.model.stat.gateways.StatEventPublisher;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class StatUseCase {
    private final StatPersistenceGateway statPersistenceGateway;
    private final StatEventPublisher statEventPublisher;

    public Mono<Stat> createStat(Stat stat) {
        return Mono.just(stat)
                .flatMap(this::validateHash)
                .map(this::addTimestamp)
                .flatMap(statWithTimestamp ->
                        statPersistenceGateway.statSave(statWithTimestamp)
                                .flatMap(saved ->
                                        statEventPublisher.publishValidatedStat(saved).thenReturn(saved)
                                )
                );
    }

    private Mono<Stat> validateHash(Stat stat) {
        String raw = String.format("%d,%d,%d,%d,%d,%d,%d",
                stat.getTotalContactoClientes(),
                stat.getMotivoReclamo(),
                stat.getMotivoGarantia(),
                stat.getMotivoDuda(),
                stat.getMotivoCompra(),
                stat.getMotivoFelicitaciones(),
                stat.getMotivoCambio()
        );

        String generatedHash = md5(raw);

        //System.out.println("Raw string for hash: " + raw);
        //System.out.println("Expected hash: " + stat.getHash());
        //System.out.println("Generated hash: " + generatedHash);

        if (!generatedHash.equalsIgnoreCase(stat.getHash())) {
            return Mono.error(new InvalidHashException("Hash inválido para los datos proporcionados"));
        }

        return Mono.just(stat);
    }

    private Stat addTimestamp(Stat stat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
                .withZone(ZoneId.of("America/Bogota"));
        String formattedTimestamp = formatter.format(Instant.now());
        stat.setTimestamp(formattedTimestamp);
        return stat;
    }

    private String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al calcular MD5", e);
        }
    }

}
