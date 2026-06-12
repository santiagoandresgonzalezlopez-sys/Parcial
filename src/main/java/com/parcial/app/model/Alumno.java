package com.parcial.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "alumno")
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Identificación ─────────────────────────────────────────────────────────

    /** Tipo de documento: CC, TI, CE, PP, NIT */
    private String tipoDocumento;

    /** Número de documento (cédula) — único */
    @Column(unique = true, nullable = false)
    private String cedula;

    /**
     * Número de registro institucional.
     * Formato: 2 letras + 11 dígitos  (Ej: US20231234567)
     * Único en la tabla.
     */
    @Column(unique = true, length = 13)
    private String numeroRegistro;

    // ── Nombre ─────────────────────────────────────────────────────────────────

    private String primerApellido;
    private String segundoApellido;
    private String primerNombre;

    /** Campo opcional */
    private String segundoNombre;

    // ── Contacto ───────────────────────────────────────────────────────────────

    private String correoElectronico;
    private String telefono;

    // ── Académico ──────────────────────────────────────────────────────────────

    private String facultad;
    private String programa;
    private int    creditosAprobados;

    // ── Estado de pago ─────────────────────────────────────────────────────────

    private boolean pagoAprobado = false;

    // ── Relación con resultado ─────────────────────────────────────────────────

    @OneToOne(mappedBy = "alumno", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private ResultadoSaberPro resultadoTotal;

    // ── Constructores ──────────────────────────────────────────────────────────

    public Alumno() {}

    // ── Helpers ────────────────────────────────────────────────────────────────

    /** Nombre completo para mostrar en la UI. */
    public String getNombre() {
        StringBuilder sb = new StringBuilder();
        if (primerNombre   != null) sb.append(primerNombre).append(" ");
        if (segundoNombre  != null && !segundoNombre.isBlank()) sb.append(segundoNombre).append(" ");
        if (primerApellido != null) sb.append(primerApellido).append(" ");
        if (segundoApellido!= null && !segundoApellido.isBlank()) sb.append(segundoApellido);
        return sb.toString().trim();
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────

    public Long getId()                          { return id; }
    public void setId(Long id)                   { this.id = id; }

    public String getTipoDocumento()                       { return tipoDocumento; }
    public void   setTipoDocumento(String tipoDocumento)   { this.tipoDocumento = tipoDocumento; }

    public String getCedula()                    { return cedula; }
    public void   setCedula(String cedula)        { this.cedula = cedula; }

    public String getNumeroRegistro()                      { return numeroRegistro; }
    public void   setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public String getPrimerApellido()                        { return primerApellido; }
    public void   setPrimerApellido(String primerApellido)   { this.primerApellido = primerApellido; }

    public String getSegundoApellido()                       { return segundoApellido; }
    public void   setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getPrimerNombre()                          { return primerNombre; }
    public void   setPrimerNombre(String primerNombre)       { this.primerNombre = primerNombre; }

    public String getSegundoNombre()                         { return segundoNombre; }
    public void   setSegundoNombre(String segundoNombre)     { this.segundoNombre = segundoNombre; }

    public String getCorreoElectronico()                           { return correoElectronico; }
    public void   setCorreoElectronico(String correoElectronico)   { this.correoElectronico = correoElectronico; }

    public String getTelefono()                  { return telefono; }
    public void   setTelefono(String telefono)   { this.telefono = telefono; }

    public String getFacultad()                  { return facultad; }
    public void   setFacultad(String facultad)   { this.facultad = facultad; }

    public String getPrograma()                  { return programa; }
    public void   setPrograma(String programa)   { this.programa = programa; }

    public int    getCreditosAprobados()                        { return creditosAprobados; }
    public void   setCreditosAprobados(int creditosAprobados)   { this.creditosAprobados = creditosAprobados; }

    public boolean isPagoAprobado()                  { return pagoAprobado; }
    public void    setPagoAprobado(boolean v)         { this.pagoAprobado = v; }

    public ResultadoSaberPro getResultadoTotal()                         { return resultadoTotal; }
    public void              setResultadoTotal(ResultadoSaberPro r)      { this.resultadoTotal = r; }
}
