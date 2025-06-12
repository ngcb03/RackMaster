package com.ngcamargob.rackmaster.utilidades.mapper;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCluster;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import com.ngcamargob.rackmaster.presentacion.dto.ClusterDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvClusterDTO implements IMapper<EntidadCluster, ClusterDTO>{
    @Override
    public ClusterDTO getIO(EntidadCluster entidadCluster) {
        ClusterDTO clusterDTO = new ClusterDTO();

        if(entidadCluster != null) {
            double almacenamiento_total = 0;;
            double ram_total = 0;;

            if(entidadCluster.getServidores() != null ) {
                almacenamiento_total = entidadCluster.getServidores().stream()
                        .mapToDouble(EntidadServidor::getAlmacenamiento_total)
                        .sum();
                ram_total = entidadCluster.getServidores().stream().mapToDouble(EntidadServidor::getRam)
                        .sum();
            }

            clusterDTO = ClusterDTO.builder()
                    .cluster_id(entidadCluster.getCluster_id())
                    .nombre(entidadCluster.getNombre())
                    .sede(entidadCluster.getSede())
                    .proyecto(entidadCluster.getProyecto())
                    .almacenamiento_total(
                            (almacenamiento_total > 1023) ?
                                    String.format("%.1f", (almacenamiento_total / 1024)) + " TB" :
                                    String.format("%.1f", almacenamiento_total) + " GB"
                    )
                    .ram_total(
                            (ram_total > 1023) ?
                                    String.format("%.1f", (ram_total / 1024)) + " TB" :
                                    String.format("%.1f", ram_total) + " GB"
                    )
                    .total_servidores(entidadCluster.getServidores().size())
                    .total_maquinas(entidadCluster.getServidores().stream()
                            .mapToInt(s -> s.getMaquinas().size())
                            .sum())
                    .build();
        } return clusterDTO;
    }

    @Override
    public EntidadCluster getOI(ClusterDTO clusterDTO) {
        EntidadCluster entidadCluster = new EntidadCluster();

        if(clusterDTO != null) {
            entidadCluster = EntidadCluster.builder()
                    .cluster_id(clusterDTO.getCluster_id())
                    .nombre(clusterDTO.getNombre())
                    .proyecto(clusterDTO.getProyecto())
                    .sede(clusterDTO.getSede())
                    .build();

        } return entidadCluster;
    }

    @Override
    public List<ClusterDTO> getLIO(List<EntidadCluster> clusters) {
        return clusters.stream().map(this::getIO).collect(Collectors.toList());
    }
}
