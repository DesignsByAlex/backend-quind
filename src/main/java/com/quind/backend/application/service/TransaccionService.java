package com.quind.backend.application.service;

import com.quind.backend.domain.entity.Cuenta;
import com.quind.backend.domain.entity.Transaccion;
import com.quind.backend.domain.enums.TipoTransaccion;
import com.quind.backend.infrastructure.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.quind.backend.application.exception.CuentaNoEncontradaException;
import com.quind.backend.application.exception.SaldoInsuficienteException;
import java.math.BigDecimal;

@Service
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepo;

    @Autowired
    private CuentaRepository cuentaRepo;

    @Transactional
    public void consignar(Long cuentaId, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        Cuenta cuenta = obtenerCuenta(cuentaId);

        cuenta.setSaldo(cuenta.getSaldo().add(monto));
        cuentaRepo.save(cuenta);

        Transaccion t = new Transaccion();
        t.setTipo(TipoTransaccion.CONSIGNACION);
        t.setMonto(monto);
        t.setCuentaOrigen(cuenta);

        transaccionRepo.save(t);
    }

    @Transactional
    public void retirar(Long cuentaId, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        Cuenta cuenta = obtenerCuenta(cuentaId); //

        if (cuenta.getSaldo().compareTo(monto) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta " + cuenta.getId());
        }

        cuenta.setSaldo(cuenta.getSaldo().subtract(monto));
        cuentaRepo.save(cuenta);

        Transaccion t = new Transaccion();
        t.setTipo(TipoTransaccion.RETIRO);
        t.setMonto(monto);
        t.setCuentaOrigen(cuenta);

        transaccionRepo.save(t);
    }


    @Transactional
    public void transferir(Long origenId, Long destinoId, BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }

        if (origenId.equals(destinoId)) {
            throw new IllegalArgumentException("No se puede transferir a la misma cuenta");
        }

        Cuenta origen = obtenerCuenta(origenId);
        Cuenta destino = obtenerCuenta(destinoId);

        if (origen.getSaldo().compareTo(monto) < 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta de origen " + origen.getId());
        }

        origen.setSaldo(origen.getSaldo().subtract(monto));
        destino.setSaldo(destino.getSaldo().add(monto));

        cuentaRepo.save(origen);
        cuentaRepo.save(destino);

        Transaccion t = new Transaccion();
        t.setTipo(TipoTransaccion.TRANSFERENCIA);
        t.setMonto(monto);
        t.setCuentaOrigen(origen);
        t.setCuentaDestino(destino);

        transaccionRepo.save(t);
    }


    private Cuenta obtenerCuenta(Long id) {
        return cuentaRepo.findById(id)
                .orElseThrow(() -> new CuentaNoEncontradaException("Cuenta no encontrada con ID: " + id));
    }

}
