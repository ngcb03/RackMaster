package com.ngcamargob.rackmaster.utilidades.mapper;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadMaquina;
import com.ngcamargob.rackmaster.presentacion.dto.MaquinaDTO;
import com.ngcamargob.rackmaster.presentacion.dto.ServidorDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvMaquinaDTO implements IMapper <EntidadMaquina, MaquinaDTO>{

    @Override
    public MaquinaDTO getIO(EntidadMaquina entidadMaquina) {

        ConvCredencialDTO convCredencialDTO = new ConvCredencialDTO();
        MaquinaDTO maquinaDTO = new MaquinaDTO();

        if(entidadMaquina != null) {
            maquinaDTO = MaquinaDTO.builder()
                    .maquina_id(entidadMaquina.getMaquina_id())
                    .nombre(entidadMaquina.getNombre())
                    .nombre_en_hipervisor(entidadMaquina.getNombre_en_hipervisor())
                    .id_en_hipervisor(entidadMaquina.getId_en_hipervisor())
                    .ip(entidadMaquina.getIp())
                    .mac(entidadMaquina.getMac().toUpperCase())
                    .sistema_op(entidadMaquina.getSistema_op())
                    .proyecto(entidadMaquina.getProyecto())
                    .aplicacion(entidadMaquina.getAplicacion())
                    .servicio(entidadMaquina.getServicio())
                    .procesador_asig(entidadMaquina.getProcesador_asig())
                    .almacenamiento_asig(entidadMaquina.getAlmacenamiento_asig())
                    .almacenamiento_total(entidadMaquina.getAlmacenamiento_total())
                    .ram_asig(entidadMaquina.getRam_asig())
                    .en_uso(entidadMaquina.isEn_uso())
                    .servidor(
                            ServidorDTO.builder()
                                    .servidor_id(entidadMaquina.getServidor().getServidor_id())
                                    .nombre(entidadMaquina.getServidor().getNombre())
                                    .en_uso(entidadMaquina.getServidor().isEn_uso())
                                    .build())
                    .build();
        } return maquinaDTO;
    }

    @Override
    public EntidadMaquina getOI(MaquinaDTO maquinaDTO) {
        EntidadMaquina entidadMaquina = new EntidadMaquina();

        if(maquinaDTO!= null) {
            entidadMaquina =  EntidadMaquina.builder()
                    .maquina_id(maquinaDTO.getMaquina_id())
                    .nombre(maquinaDTO.getNombre())
                    .nombre_en_hipervisor(maquinaDTO.getNombre_en_hipervisor())
                    .id_en_hipervisor(maquinaDTO.getId_en_hipervisor())
                    .ip(maquinaDTO.getIp())
                    .mac(maquinaDTO.getMac().toUpperCase())
                    .sistema_op(maquinaDTO.getSistema_op())
                    .proyecto(maquinaDTO.getProyecto())
                    .aplicacion(maquinaDTO.getAplicacion())
                    .servicio(maquinaDTO.getServicio())
                    .procesador_asig(maquinaDTO.getProcesador_asig())
                    .almacenamiento_asig(maquinaDTO.getAlmacenamiento_asig())
                    .almacenamiento_total(maquinaDTO.getAlmacenamiento_total())
                    .ram_asig(maquinaDTO.getRam_asig())
                    .en_uso(maquinaDTO.isEn_uso())
                    .build();
        } return entidadMaquina;
    }

    @Override
    public List<MaquinaDTO> getLIO(List<EntidadMaquina> maquinas) {
        return maquinas.stream().map(this::getIO).collect(Collectors.toList());
    }

}
