package com.parcial.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    @Column(unique = true, nullable = false)
    private String email;

    /** Contraseña en texto plano. Por defecto es la cédula del usuario. */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    /** Cédula vinculada — se usa para asociar con Alumno, Docente o Director. */
    private String cedula;

    /** Nombre para mostrar en la interfaz. */
    private String nombre;

    private boolean activo = true;

    public enum Rol {
        ADMIN, COORDINADOR, DOCENTE, ESTUDIANTE
    }

    // ── Constructores ──────────────────────────────────────────────────────────

    public Usuario() {}

    public Usuario(String email, String password, Rol rol, String cedula, String nombre) {
        this.email    = email;
        this.password = password;
        this.rol      = rol;
        this.cedula   = cedula;
        this.nombre   = nombre;
    }

    // ── Getters / Setters ──────────────────────────────────────────────────────

    public Long getId()                  { return id; }
    public void setId(Long id)           { this.id = id; }

    public String getEmail()             { return email; }
    public void setEmail(String email)   { this.email = email; }

    public String getPassword()                  { return password; }
    public void setPassword(String password)     { this.password = password; }

    public Rol getRol()                  { return rol; }
    public void setRol(Rol rol)          { this.rol = rol; }

    public String getCedula()            { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }

    public String getNombre()            { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public boolean isActivo()               { return activo; }
    public void setActivo(boolean activo)   { this.activo = activo; }
}
