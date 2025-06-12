package com.ngcamargob.rackmaster.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ServidorDTO {

    private Integer servidor_id;
    private String nombre;
    private String ip;
    private String mac;
    private String sistema_op;
    private String modelo;
    private String procesador;
    private String discos;
    private Integer almacenamiento_total;
    private Integer ram;
    private String serial;
    private Integer placa;
    private String rack;
    private byte unidad;
    private String sede;
    private boolean en_uso;

    private String usuario;
    private Integer puerto;
    private String contrasenia;

    private ClusterDTO cluster;
    private Integer total_maquinas;

}
