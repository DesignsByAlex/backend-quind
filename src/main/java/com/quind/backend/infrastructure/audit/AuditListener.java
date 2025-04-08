package com.quind.backend.infrastructure.audit;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;
import com.quind.backend.domain.entity.Persona;

public class AuditListener {

    @PrePersist
    public void prePersist(Persona persona) {
        LocalDateTime now = LocalDateTime.now();
        persona.setFechaCreacion(now);
        persona.setFechaModificacion(now);
    }

    @PreUpdate
    public void preUpdate(Persona persona) {
        persona.setFechaModificacion(LocalDateTime.now());
    }
}
