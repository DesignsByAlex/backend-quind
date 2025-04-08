package com.quind.backend.application.service;

import com.quind.backend.domain.entity.Cuenta;
import com.quind.backend.domain.enums.EstadoCuenta;
import com.quind.backend.infrastructure.repository.CuentaRepository;
import com.quind.backend.infrastructure.repository.PersonaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final PersonaRepository personaRepository;

    private final Random random = new Random();

    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaRepository.findAll();
    }

    public Cuenta obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + id));
    }

    @Transactional
    public Cuenta crearCuenta(Cuenta cuenta) {
        if (cuenta.getCliente() == null || cuenta.getCliente().getId() == null) {
            throw new RuntimeException("Debe proporcionar un cliente con ID válido.");
        }
        validarClienteExiste(cuenta.getCliente().getId());
        validarSaldoInicial(cuenta);
        asignarNumeroCuenta(cuenta);
        cuenta.setFechaCreacion(LocalDateTime.now());
        cuenta.setFechaModificacion(LocalDateTime.now());
        cuenta.setEstado(EstadoCuenta.ACTIVA); // predeterminada
        return cuentaRepository.save(cuenta);
    }

    private void validarClienteExiste(Long clienteId) {
        if (!personaRepository.existsById(clienteId)) {
            throw new RuntimeException("No existe un cliente con ID: " + clienteId);
        }
    }

    private void validarSaldoInicial(Cuenta cuenta) {
        if ("AHORRO".equalsIgnoreCase(cuenta.getTipoCuenta()) &&
                (cuenta.getSaldo() == null || cuenta.getSaldo().compareTo(BigDecimal.ZERO) <= 0)) {
            throw new RuntimeException("La cuenta de ahorro debe tener un saldo mayor a 0.");
        }
    }

    private void asignarNumeroCuenta(Cuenta cuenta) {
        String prefijo = "CORRIENTE".equalsIgnoreCase(cuenta.getTipoCuenta()) ? "33" : "53";
        String numeroAleatorio;
        String numeroCuenta;
        do {
            numeroAleatorio = String.format("%08d", random.nextInt(100_000_000));
            numeroCuenta = prefijo + numeroAleatorio;
        } while (cuentaRepository.existsByNumeroCuenta(numeroCuenta));
        cuenta.setNumeroCuenta(numeroCuenta);
    }

    @Transactional
    public Cuenta actualizarCuenta(Long id, Cuenta cuentaActualizada) {
        Cuenta cuentaExistente = obtenerCuentaPorId(id);
        cuentaExistente.setSaldo(cuentaActualizada.getSaldo());
        cuentaExistente.setEstado(cuentaActualizada.getEstado());
        cuentaExistente.setFechaModificacion(LocalDateTime.now());
        return cuentaRepository.save(cuentaExistente);
    }

    @Transactional
    public void eliminarCuenta(Long id) {
        Cuenta cuenta = obtenerCuentaPorId(id);
        cuentaRepository.delete(cuenta);
    }
}
