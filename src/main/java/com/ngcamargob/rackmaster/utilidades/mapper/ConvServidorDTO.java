package com.ngcamargob.rackmaster.utilidades.mapper;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import com.ngcamargob.rackmaster.presentacion.dto.ClusterDTO;
import com.ngcamargob.rackmaster.presentacion.dto.ServidorDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvServidorDTO implements IMapper<EntidadServidor, ServidorDTO>{

    @Override
    public ServidorDTO getIO(EntidadServidor entidadServidor) {

        ConvMaquinaDTO convMaquinaDTO = new ConvMaquinaDTO();
        ConvCredencialDTO convCredencialDTO = new ConvCredencialDTO();
        ServidorDTO servidorDTO = new ServidorDTO();

        if(entidadServidor != null) {
            servidorDTO = ServidorDTO.builder()
                    .servidor_id(entidadServidor.getServidor_id())
                    .nombre(entidadServidor.getNombre())
                    .ip(entidadServidor.getIp())
                    .mac(entidadServidor.getMac().toUpperCase())
                    .sistema_op(entidadServidor.getSistema_op())
                    .modelo(entidadServidor.getModelo())
                    .procesador(entidadServidor.getProcesador())
                    .discos(entidadServidor.getDiscos())
                    .almacenamiento_total(entidadServidor.getAlmacenamiento_total())
                    .ram(entidadServidor.getRam())
                    .serial(entidadServidor.getSerial().toUpperCase())
                    .placa(entidadServidor.getPlaca())
                    .rack(entidadServidor.getRack())
                    .unidad(entidadServidor.getUnidad())
                    .sede(entidadServidor.getSede())
                    .en_uso(entidadServidor.isEn_uso())
                    .build();

            if(entidadServidor.getMaquinas() != null) {
                servidorDTO.setTotal_maquinas(entidadServidor.getMaquinas().size());
            }

            if(entidadServidor.getCluster() != null) {
                servidorDTO.setCluster(ClusterDTO.builder()
                        .cluster_id(entidadServidor.getCluster().getCluster_id())
                        .nombre(entidadServidor.getCluster().getNombre())
                        .build());
            }
        } return servidorDTO;
    }

    @Override
    public EntidadServidor getOI(ServidorDTO servidorDTO) {

        EntidadServidor entidadServidor = new EntidadServidor();
        if(servidorDTO != null) {

            entidadServidor = EntidadServidor.builder()
                    .servidor_id(servidorDTO.getServidor_id())
                    .nombre(servidorDTO.getNombre())
                    .ip(servidorDTO.getIp())
                    .mac(servidorDTO.getMac().toUpperCase())
                    .sistema_op(servidorDTO.getSistema_op())
                    .modelo(servidorDTO.getModelo())
                    .procesador(servidorDTO.getProcesador())
                    .discos(servidorDTO.getDiscos())
                    .almacenamiento_total(servidorDTO.getAlmacenamiento_total())
                    .ram(servidorDTO.getRam())
                    .serial(servidorDTO.getSerial().toUpperCase())
                    .placa(servidorDTO.getPlaca())
                    .rack(servidorDTO.getRack())
                    .unidad(servidorDTO.getUnidad())
                    .sede(servidorDTO.getSede())
                    .en_uso(servidorDTO.isEn_uso())
                    .build();
        } return entidadServidor;

    }

    @Override
    public List<ServidorDTO> getLIO(List<EntidadServidor> servidores) {
        return servidores.stream().map(this::getIO).collect(Collectors.toList());
    }

}
