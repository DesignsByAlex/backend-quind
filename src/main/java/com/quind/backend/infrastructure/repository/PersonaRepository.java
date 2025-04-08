package com.quind.backend.infrastructure.repository;

import com.quind.backend.domain.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    // agregar métodos personalizados para buscar por correo o identificación:
    boolean existsByCorreoElectronico(String correoElectronico);
}
