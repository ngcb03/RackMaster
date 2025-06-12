package com.ngcamargob.rackmaster.persistencia.entidades.enums_entity;

import com.ngcamargob.rackmaster.persistencia.entidades.enums.ERol;
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
@Table(name="roles")
public class EntidadRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rol_id;

    @Enumerated(EnumType.STRING)
    private ERol rolUsuario;

}
