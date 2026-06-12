package com.parcial.app.model;

import jakarta.persistence.*;

/**
 * Almacena el resultado completo de Saber Pro de un alumno.
 *
 * Módulos genéricos (escala 0-300):
 *   Comunicación Escrita, Razonamiento Cuantitativo, Lectura Crítica,
 *   Competencias Ciudadanas, Inglés
 *
 * Módulos específicos (escala 0-300):
 *   Formulación de Proyectos de Ingeniería,
 *   Pensamiento Científico – Matemáticas y Estadística,
 *   Diseño de Software
 *
 * Niveles genéricos (según ICFES): 1 | 2 | 3 | 4
 * Nivel Inglés: A- | A1 | A2 | B1 | B+ (escala ICFES para inglés)
 */
@Entity
@Table(name = "resultado_saber_pro")
public class ResultadoSaberPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Datos generales ────────────────────────────────────────────────────────
    private int    puntajeGlobal;
    private int    percentil;
    private String nivelDesempeno;   // nivel global calculado
    private int    anioPresentacion;

    // ── Módulos genéricos ──────────────────────────────────────────────────────
    private int    puntajeComunicacionEscrita;
    private String nivelComunicacionEscrita;

    private int    puntajeRazonamientoCuantitativo;
    private String nivelRazonamientoCuantitativo;

    private int    puntajeLecturaCritica;
    private String nivelLecturaCritica;

    private int    puntajeCompetenciasCiudadanas;
    private String nivelCompetenciasCiudadanas;

    private int    puntajeIngles;
    private String nivelIngles;       // Nivel ICFES: A- | A1 | A2 | B1 | B+
    private String nivelInglesMarco;  // Marco CEF: A1 | A2 | B1 | B2 | C1 | C2

    // ── Módulos específicos ────────────────────────────────────────────────────
    private int    puntajeFormulacionProyectos;
    private String nivelFormulacionProyectos;

    private int    puntajePensamientoCientifico;
    private String nivelPensamientoCientifico;

    private int    puntajeDisenioSoftware;
    private String nivelDisenioSoftware;

    /**
     * Indica que la prueba fue anulada por el ICFES.
     * Cuando es true, todos los puntajes deben quedar en 0 y los niveles vacíos.
     */
    private boolean pruebaNula = false;

    // ── Relación ───────────────────────────────────────────────────────────────
    @OneToOne
    @JoinColumn(name = "alumno_id", unique = true)
    private Alumno alumno;

    // ── Constructores ──────────────────────────────────────────────────────────

    public ResultadoSaberPro() {}

    /** Constructor mínimo usado para crear ejemplos demostrativos. */
    public ResultadoSaberPro(int puntajeGlobal, int percentil,
                              String nivelDesempeno, int anioPresentacion,
                              Alumno alumno) {
        this.puntajeGlobal    = puntajeGlobal;
        this.percentil        = percentil;
        this.nivelDesempeno   = nivelDesempeno;
        this.anioPresentacion = anioPresentacion;
        this.alumno           = alumno;
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────

    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }

    public int    getPuntajeGlobal()                      { return puntajeGlobal; }
    public void   setPuntajeGlobal(int v)                 { this.puntajeGlobal = v; }

    public int    getPercentil()                          { return percentil; }
    public void   setPercentil(int v)                     { this.percentil = v; }

    public String getNivelDesempeno()                     { return nivelDesempeno; }
    public void   setNivelDesempeno(String v)             { this.nivelDesempeno = v; }

    public int    getAnioPresentacion()                   { return anioPresentacion; }
    public void   setAnioPresentacion(int v)              { this.anioPresentacion = v; }

    // Comunicación Escrita
    public int    getPuntajeComunicacionEscrita()         { return puntajeComunicacionEscrita; }
    public void   setPuntajeComunicacionEscrita(int v)    { this.puntajeComunicacionEscrita = v; }
    public String getNivelComunicacionEscrita()           { return nivelComunicacionEscrita; }
    public void   setNivelComunicacionEscrita(String v)   { this.nivelComunicacionEscrita = v; }

    // Razonamiento Cuantitativo
    public int    getPuntajeRazonamientoCuantitativo()       { return puntajeRazonamientoCuantitativo; }
    public void   setPuntajeRazonamientoCuantitativo(int v)  { this.puntajeRazonamientoCuantitativo = v; }
    public String getNivelRazonamientoCuantitativo()         { return nivelRazonamientoCuantitativo; }
    public void   setNivelRazonamientoCuantitativo(String v) { this.nivelRazonamientoCuantitativo = v; }

    // Lectura Crítica
    public int    getPuntajeLecturaCritica()              { return puntajeLecturaCritica; }
    public void   setPuntajeLecturaCritica(int v)         { this.puntajeLecturaCritica = v; }
    public String getNivelLecturaCritica()                { return nivelLecturaCritica; }
    public void   setNivelLecturaCritica(String v)        { this.nivelLecturaCritica = v; }

    // Competencias Ciudadanas
    public int    getPuntajeCompetenciasCiudadanas()         { return puntajeCompetenciasCiudadanas; }
    public void   setPuntajeCompetenciasCiudadanas(int v)    { this.puntajeCompetenciasCiudadanas = v; }
    public String getNivelCompetenciasCiudadanas()           { return nivelCompetenciasCiudadanas; }
    public void   setNivelCompetenciasCiudadanas(String v)   { this.nivelCompetenciasCiudadanas = v; }

    // Inglés
    public int    getPuntajeIngles()                      { return puntajeIngles; }
    public void   setPuntajeIngles(int v)                 { this.puntajeIngles = v; }
    public String getNivelIngles()                        { return nivelIngles; }
    public void   setNivelIngles(String v)                { this.nivelIngles = v; }
    public String getNivelInglesMarco()                   { return nivelInglesMarco; }
    public void   setNivelInglesMarco(String v)           { this.nivelInglesMarco = v; }

    // Formulación de Proyectos de Ingeniería
    public int    getPuntajeFormulacionProyectos()           { return puntajeFormulacionProyectos; }
    public void   setPuntajeFormulacionProyectos(int v)      { this.puntajeFormulacionProyectos = v; }
    public String getNivelFormulacionProyectos()             { return nivelFormulacionProyectos; }
    public void   setNivelFormulacionProyectos(String v)     { this.nivelFormulacionProyectos = v; }

    // Pensamiento Científico
    public int    getPuntajePensamientoCientifico()          { return puntajePensamientoCientifico; }
    public void   setPuntajePensamientoCientifico(int v)     { this.puntajePensamientoCientifico = v; }
    public String getNivelPensamientoCientifico()            { return nivelPensamientoCientifico; }
    public void   setNivelPensamientoCientifico(String v)    { this.nivelPensamientoCientifico = v; }

    // Diseño de Software
    public int    getPuntajeDisenioSoftware()                { return puntajeDisenioSoftware; }
    public void   setPuntajeDisenioSoftware(int v)           { this.puntajeDisenioSoftware = v; }
    public String getNivelDisenioSoftware()                  { return nivelDisenioSoftware; }
    public void   setNivelDisenioSoftware(String v)          { this.nivelDisenioSoftware = v; }

    public Alumno getAlumno()               { return alumno; }
    public void   setAlumno(Alumno alumno)  { this.alumno = alumno; }

    public boolean isPruebaNula()             { return pruebaNula; }
    public void    setPruebaNula(boolean v)   { this.pruebaNula = v; }
}
