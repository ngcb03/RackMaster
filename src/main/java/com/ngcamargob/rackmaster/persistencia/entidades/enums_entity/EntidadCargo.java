package com.ngcamargob.rackmaster.persistencia.entidades.enums_entity;

import com.ngcamargob.rackmaster.persistencia.entidades.enums.ECargo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name="cargos")
public class EntidadCargo {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer cargo_id;

    @Enumerated(EnumType.STRING)
    private ECargo cargoUsuario;

    @Override
    public String toString() {
        return "EntidadCargo{" +
                "cargo_id=" + cargo_id +
                ", cargoUsuario=" + cargoUsuario +
                '}';
    }
}

