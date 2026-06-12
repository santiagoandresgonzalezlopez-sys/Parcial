package com.parcial.app.repository;

import com.parcial.app.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    Optional<Alumno> findByCedula(String cedula);

    List<Alumno> findByFacultad(String facultad);

    boolean existsByCedula(String cedula);

    boolean existsByNumeroRegistro(String numeroRegistro);

    // Excluye el propio registro al validar unicidad en edición
    boolean existsByNumeroRegistroAndIdNot(String numeroRegistro, Long id);

    boolean existsByCedulaAndIdNot(String cedula, Long id);
}
