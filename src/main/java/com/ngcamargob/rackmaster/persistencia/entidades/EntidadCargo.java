package com.ngcamargob.rackmaster.persistencia.entidades;

import com.ngcamargob.rackmaster.persistencia.entidades.enums.ECargo;
import jakarta.persistence.*;
import lombok.Builder;

@Builder
@Entity
@Table(name="cargos")
public class EntidadCargo {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer cargo_id;

    @Enumerated(EnumType.STRING)
    private ECargo cargoUsuario;

    public EntidadCargo() {
    }

    public EntidadCargo(Integer cargo_id, ECargo cargoUsuario) {
        this.cargo_id = cargo_id;
        this.cargoUsuario = cargoUsuario;
    }

    public Integer getCargo_id() {
        return cargo_id;
    }

    public void setCargo_id(Integer cargo_id) {
        this.cargo_id = cargo_id;
    }

    public ECargo getCargoUsuario() {
        return cargoUsuario;
    }

    public void setCargoUsuario(ECargo cargoUsuario) {
        this.cargoUsuario = cargoUsuario;
    }

    @Override
    public String toString() {
        return "EntidadCargo{" +
                "cargo_id=" + cargo_id +
                ", cargoUsuario=" + cargoUsuario +
                '}';
    }

}
