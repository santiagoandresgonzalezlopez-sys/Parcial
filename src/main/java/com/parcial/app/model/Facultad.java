package com.parcial.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Facultad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    @Column(unique = true)
    private String nombre;
    
    private String decano;
    private String descripcion;
    
    // Constructores
    public Facultad() {}
    
    public Facultad(String nombre, String decano, String descripcion) {
        this.nombre = nombre;
        this.decano = decano;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDecano() { return decano; }
    public void setDecano(String decano) { this.decano = decano; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}