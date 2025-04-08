package com.quind.backend.infrastructure.repository;

import com.quind.backend.domain.entity.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
}
