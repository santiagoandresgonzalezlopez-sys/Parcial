package com.parcial.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    
    @NotBlank(message = "La cédula es obligatoria")
    @Column(unique = true)
    private String cedula;
    
    private String facultad;
    
    @Email(message = "Email inválido")
    private String email;
    
    private String telefono;
    
    // Constructores
    public Docente() {}
    
    public Docente(String nombre, String cedula, String facultad, String email, String telefono) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.facultad = facultad;
        this.email = email;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    
    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}