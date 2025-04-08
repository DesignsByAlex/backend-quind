package com.quind.backend.infrastructure.controller;

import com.quind.backend.application.service.TransaccionService;
import com.quind.backend.infrastructure.dto.TransaccionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService service;

    @PostMapping("/consignar")
    public void consignar(@RequestBody TransaccionRequest request) {
        service.consignar(request.getCuentaId(), request.getMonto());
    }

    @PostMapping("/retirar")
    public void retirar(@RequestBody TransaccionRequest request) {
        service.retirar(request.getCuentaId(), request.getMonto());
    }

    @PostMapping("/transferir")
    public void transferir(@RequestBody TransaccionRequest request) {
        service.transferir(request.getCuentaId(), request.getCuentaDestinoId(), request.getMonto());
    }
}

