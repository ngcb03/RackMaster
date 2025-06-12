package com.ngcamargob.rackmaster.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CredencialDTO {

    private Integer credencial_id;
    private String usuario;
    private String contrasenia;
    private Integer puerto;
    private String tipo_conexion;
    private String privilegios;
    private String uso_destinado;
    private boolean primaria;
    private Integer id_equipo;

}
