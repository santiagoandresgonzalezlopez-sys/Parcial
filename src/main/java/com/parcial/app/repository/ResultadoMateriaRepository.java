package com.parcial.app.repository;

import com.parcial.app.model.ResultadoMateria;
import com.parcial.app.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResultadoMateriaRepository extends JpaRepository<ResultadoMateria, Long> {
    List<ResultadoMateria> findByAlumnoId(Long alumnoId);
    List<ResultadoMateria> findByAlumno(Alumno alumno);
}