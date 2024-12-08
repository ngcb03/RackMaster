package com.ngcamargob.rackmaster.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;

@Builder
@Entity
@Table(name="cuentas")
public class EntidadCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cuenta_id;

    @NotBlank(message = "El nombre completo es obligatorio.")
    @Size(max = 160, message = "El nombre completo debe tener un máximo de 160 caracteres.")
    private String nombre_completo;

    @NotBlank(message = "El nombre de usuario es obligatorio.")
    @Size(max = 160, message = "El nombre de usuario debe tener un máximo de 160 caracteres.")
    private String usuario;

    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(min = 8, max = 260, message = "La contraseña debe tener entre 8 y 260 caracteres.")
    private String contrasenia;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "cargo_id", referencedColumnName = "cargo_id", nullable = false)
    private EntidadCargo entidadCargo;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id", referencedColumnName = "rol_id", nullable = false)
    private EntidadRol entidadRol;

    private boolean habilitado;

    public EntidadCuenta() {
    }

    public EntidadCuenta(Integer cuenta_id, String nombre_completo, String usuario, String contrasenia, EntidadCargo entidadCargo, EntidadRol entidadRol, boolean habilitado) {
        this.cuenta_id = cuenta_id;
        this.nombre_completo = nombre_completo;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.entidadCargo = entidadCargo;
        this.entidadRol = entidadRol;
        this.habilitado = habilitado;
    }

    public Integer getCuenta_id() {
        return cuenta_id;
    }

    public void setCuenta_id(Integer cuenta_id) {
        this.cuenta_id = cuenta_id;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public EntidadCargo getEntidadCargo() {
        return entidadCargo;
    }

    public void setEntidadCargo(EntidadCargo entidadCargo) {
        this.entidadCargo = entidadCargo;
    }

    public EntidadRol getEntidadRol() {
        return entidadRol;
    }

    public void setEntidadRol(EntidadRol entidadRol) {
        this.entidadRol = entidadRol;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    @Override
    public String toString() {
        return "EntidadCuenta{" +
                "cuenta_id=" + cuenta_id +
                ", nombre_completo='" + nombre_completo + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                ", entidadCargo=" + entidadCargo +
                ", entidadRol=" + entidadRol +
                ", habilitado=" + habilitado +
                '}';
    }
}
