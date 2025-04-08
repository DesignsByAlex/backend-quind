package com.quind.backend.infrastructure.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Setter
@Getter
public class TransaccionRequest {

    // Getter y Setter para cuentaId
    private Long cuentaId;
    // Getter y Setter para monto
    private BigDecimal monto;
    private Long cuentaDestinoId; // opcional para transferencias
    private String descripcion;   // opcional

}
