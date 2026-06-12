package com.parcial.app.repository;

import com.parcial.app.model.ResultadoSaberPro;
import com.parcial.app.model.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ResultadoSaberProRepository extends JpaRepository<ResultadoSaberPro, Long> {
    Optional<ResultadoSaberPro> findByAlumnoId(Long alumnoId);
    Optional<ResultadoSaberPro> findByAlumno(Alumno alumno);
}