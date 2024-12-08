package com.ngcamargob.rackmaster.presentacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ClusterDTO {

    private Integer cluster_id;
    private String nombre;
    private String proyecto;
    private String sede;
    private String almacenamiento_total;
    private String ram_total;
    private Integer total_servidores;
    private Integer total_maquinas;

    private String servidoresdb_ids;

}
