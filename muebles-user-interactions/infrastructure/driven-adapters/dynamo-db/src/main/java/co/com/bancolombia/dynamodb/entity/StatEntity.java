package co.com.bancolombia.dynamodb.entity;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class StatEntity {

    private int totalContactoClientes;
    private int motivoReclamo;
    private int motivoGarantia;
    private int motivoDuda;
    private int motivoCompra;
    private int motivoFelicitaciones;
    private int motivoCambio;
    private String hash;
    private String timestamp; // clave primaria

    public StatEntity() {
    }


    // --- Getters y setters ---

    @DynamoDbPartitionKey
    @DynamoDbAttribute("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDbAttribute("totalContactoClientes")
    public int getTotalContactoClientes() {
        return totalContactoClientes;
    }

    public void setTotalContactoClientes(int totalContactoClientes) {
        this.totalContactoClientes = totalContactoClientes;
    }

    @DynamoDbAttribute("motivoReclamo")
    public int getMotivoReclamo() {
        return motivoReclamo;
    }

    public void setMotivoReclamo(int motivoReclamo) {
        this.motivoReclamo = motivoReclamo;
    }

    @DynamoDbAttribute("motivoGarantia")
    public int getMotivoGarantia() {
        return motivoGarantia;
    }

    public void setMotivoGarantia(int motivoGarantia) {
        this.motivoGarantia = motivoGarantia;
    }

    @DynamoDbAttribute("motivoDuda")
    public int getMotivoDuda() {
        return motivoDuda;
    }

    public void setMotivoDuda(int motivoDuda) {
        this.motivoDuda = motivoDuda;
    }

    @DynamoDbAttribute("motivoCompra")
    public int getMotivoCompra() {
        return motivoCompra;
    }

    public void setMotivoCompra(int motivoCompra) {
        this.motivoCompra = motivoCompra;
    }

    @DynamoDbAttribute("motivoFelicitaciones")
    public int getMotivoFelicitaciones() {
        return motivoFelicitaciones;
    }

    public void setMotivoFelicitaciones(int motivoFelicitaciones) {
        this.motivoFelicitaciones = motivoFelicitaciones;
    }

    @DynamoDbAttribute("motivoCambio")
    public int getMotivoCambio() {
        return motivoCambio;
    }

    public void setMotivoCambio(int motivoCambio) {
        this.motivoCambio = motivoCambio;
    }

    @DynamoDbAttribute("hash")
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
