package co.com.bancolombia.dynamodb.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatEntityTest {

    @Test
    void shouldSetAndGetAllFieldsCorrectly() {
        // Arrange
        StatEntity entity = new StatEntity();
        String timestamp = "2025-07-08T12:00:00Z";
        String hash = "unique-hash-123";

        int totalContactoClientes = 10;
        int motivoReclamo = 2;
        int motivoGarantia = 3;
        int motivoDuda = 1;
        int motivoCompra = 4;
        int motivoFelicitaciones = 5;
        int motivoCambio = 6;

        // Act
        entity.setTimestamp(timestamp);
        entity.setHash(hash);
        entity.setTotalContactoClientes(totalContactoClientes);
        entity.setMotivoReclamo(motivoReclamo);
        entity.setMotivoGarantia(motivoGarantia);
        entity.setMotivoDuda(motivoDuda);
        entity.setMotivoCompra(motivoCompra);
        entity.setMotivoFelicitaciones(motivoFelicitaciones);
        entity.setMotivoCambio(motivoCambio);

        // Assert
        assertEquals(timestamp, entity.getTimestamp());
        assertEquals(hash, entity.getHash());
        assertEquals(totalContactoClientes, entity.getTotalContactoClientes());
        assertEquals(motivoReclamo, entity.getMotivoReclamo());
        assertEquals(motivoGarantia, entity.getMotivoGarantia());
        assertEquals(motivoDuda, entity.getMotivoDuda());
        assertEquals(motivoCompra, entity.getMotivoCompra());
        assertEquals(motivoFelicitaciones, entity.getMotivoFelicitaciones());
        assertEquals(motivoCambio, entity.getMotivoCambio());
    }
}
