package com.ngcamargob.rackmaster.persistencia.entidades;

import com.ngcamargob.rackmaster.persistencia.entidades.enums.ERol;
import jakarta.persistence.*;
import lombok.Builder;

@Builder
@Entity
@Table(name="roles")
public class EntidadRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rol_id;

    @Enumerated(EnumType.STRING)
    private ERol rolUsuario;

    public EntidadRol() {
    }

    public EntidadRol(Integer rol_id, ERol rolUsuario) {
        this.rol_id = rol_id;
        this.rolUsuario = rolUsuario;
    }

    public Integer getRol_id() {
        return rol_id;
    }

    public void setRol_id(Integer rol_id) {
        this.rol_id = rol_id;
    }

    public ERol getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(ERol rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    @Override
    public String toString() {
        return "EntidadRol{" +
                "rol_id=" + rol_id +
                ", rolUsuario=" + rolUsuario +
                '}';
    }

}
