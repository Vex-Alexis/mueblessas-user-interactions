package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.dto.request.StatRequest;
import co.com.bancolombia.model.stat.Stat;
import org.springframework.stereotype.Component;

@Component
public class StatMapper {
    public Stat requestToDomain(StatRequest dto) {
        return Stat.builder()
                .totalContactoClientes(dto.getTotalContactoClientes())
                .motivoReclamo(dto.getMotivoReclamo())
                .motivoGarantia(dto.getMotivoGarantia())
                .motivoDuda(dto.getMotivoDuda())
                .motivoCompra(dto.getMotivoCompra())
                .motivoFelicitaciones(dto.getMotivoFelicitaciones())
                .motivoCambio(dto.getMotivoCambio())
                .hash(dto.getHash())
                .build();
    }
}
