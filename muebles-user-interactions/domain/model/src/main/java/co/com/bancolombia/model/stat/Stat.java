package co.com.bancolombia.model.stat;
import lombok.*;
//import lombok.NoArgsConstructor;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Stat {
    private int totalContactoClientes;
    private int motivoReclamo;
    private int motivoGarantia;
    private int motivoDuda;
    private int motivoCompra;
    private int motivoFelicitaciones;
    private int motivoCambio;
    private String hash;
    private String timestamp;
}
