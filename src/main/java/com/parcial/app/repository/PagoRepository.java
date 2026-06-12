package com.parcial.app.repository;

import com.parcial.app.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByAprobado(boolean aprobado);
    List<Pago> findByCedulaAlumno(String cedulaAlumno);
    Optional<Pago> findByCedulaAlumnoAndAprobado(String cedulaAlumno, boolean aprobado);
}