package com.parcial.app.repository;

import com.parcial.app.model.Facultad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FacultadRepository extends JpaRepository<Facultad, Long> {
    Optional<Facultad> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}