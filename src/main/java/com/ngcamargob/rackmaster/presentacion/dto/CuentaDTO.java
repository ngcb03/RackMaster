package com.ngcamargob.rackmaster.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CuentaDTO {

    private Integer cuenta_id;
    private String nombre_completo;
    private String usuario;
    private String contrasenia;
    private String cargo;
    private String rol;
    private boolean habilitado;

}
