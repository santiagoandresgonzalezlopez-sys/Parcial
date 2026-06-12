package com.parcial.app.model;

import jakarta.persistence.*;

@Entity
public class ResultadoMateria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String materia;
    private int puntaje;
    private String modulo;
    
    @ManyToOne
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;
    
    // Constructores
    public ResultadoMateria() {}
    
    public ResultadoMateria(String materia, int puntaje, String modulo, Alumno alumno) {
        this.materia = materia;
        this.puntaje = puntaje;
        this.modulo = modulo;
        this.alumno = alumno;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
    
    public int getPuntaje() { return puntaje; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }
    
    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    
    public Alumno getAlumno() { return alumno; }
    public void setAlumno(Alumno alumno) { this.alumno = alumno; }
}