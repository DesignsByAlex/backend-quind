package com.quind.backend.domain.entity;

import com.quind.backend.domain.enums.TipoTransaccion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "transacciones")
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipo;

    private BigDecimal monto;

    private LocalDateTime fecha;

    @ManyToOne
    private Cuenta cuentaOrigen;

    @ManyToOne
    private Cuenta cuentaDestino; // solo se usa en transferencia

    // Getters, setters y constructor vacío

    @PrePersist
    public void prePersist() {
        this.fecha = LocalDateTime.now();
    }
}
