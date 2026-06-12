package com.parcial.app.repository;

import com.parcial.app.model.Docente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DocenteRepository extends JpaRepository<Docente, Long> {
    Optional<Docente> findByCedula(String cedula);
    List<Docente> findByFacultad(String facultad);
    boolean existsByCedula(String cedula);
}