package com.ngcamargob.rackmaster.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class MaquinaDTO {

    private Integer maquina_id;
    private String nombre;
    private String nombre_en_hipervisor;
    private Integer id_en_hipervisor;
    private String ip;
    private String mac;
    private String sistema_op;
    private String proyecto;
    private String aplicacion;
    private String servicio;
    private String procesador_asig;
    private String almacenamiento_asig;
    private Integer almacenamiento_total;
    private Integer ram_asig;
    private boolean en_uso;

    private String usuario;
    private Integer puerto;
    private String contrasenia;

    private ServidorDTO servidor;

}
