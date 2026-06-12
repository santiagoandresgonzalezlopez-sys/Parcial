package com.parcial.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cedulaAlumno;

    /** Nombre original del archivo subido (Ej: recibo_pago.pdf) */
    private String nombreArchivo;

    /** Tipo MIME del archivo (Ej: application/pdf, image/jpeg) */
    private String tipoArchivo;

    /**
     * Ruta relativa del archivo guardado en disco.
     * Ej: uploads/pagos/1111111111_1718150000000.pdf
     */
    private String rutaArchivo;

    private boolean aprobado = false;
    private LocalDateTime fechaCarga;
    private LocalDateTime fechaAprobacion;

    public Pago() {
        this.fechaCarga = LocalDateTime.now();
    }

    public Pago(String cedulaAlumno, String nombreArchivo, String tipoArchivo, String rutaArchivo) {
        this.cedulaAlumno  = cedulaAlumno;
        this.nombreArchivo = nombreArchivo;
        this.tipoArchivo   = tipoArchivo;
        this.rutaArchivo   = rutaArchivo;
        this.fechaCarga    = LocalDateTime.now();
        this.aprobado      = false;
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────

    public Long   getId()              { return id; }
    public void   setId(Long id)       { this.id = id; }

    public String getCedulaAlumno()                  { return cedulaAlumno; }
    public void   setCedulaAlumno(String v)           { this.cedulaAlumno = v; }

    public String getNombreArchivo()                 { return nombreArchivo; }
    public void   setNombreArchivo(String v)          { this.nombreArchivo = v; }

    public String getTipoArchivo()                   { return tipoArchivo; }
    public void   setTipoArchivo(String v)            { this.tipoArchivo = v; }

    public String getRutaArchivo()                   { return rutaArchivo; }
    public void   setRutaArchivo(String v)            { this.rutaArchivo = v; }

    public boolean isAprobado()                      { return aprobado; }
    public void    setAprobado(boolean v) {
        this.aprobado = v;
        if (v) this.fechaAprobacion = LocalDateTime.now();
    }

    public LocalDateTime getFechaCarga()                     { return fechaCarga; }
    public void          setFechaCarga(LocalDateTime v)       { this.fechaCarga = v; }

    public LocalDateTime getFechaAprobacion()                { return fechaAprobacion; }
    public void          setFechaAprobacion(LocalDateTime v)  { this.fechaAprobacion = v; }
}
