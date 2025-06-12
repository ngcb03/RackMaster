package com.ngcamargob.rackmaster.utilidades.mapper;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.presentacion.dto.CredencialDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ConvCredencialDTO implements IMapper<EntidadCredencial, CredencialDTO>{

    @Override
    public CredencialDTO getIO(EntidadCredencial entidadCredencial) {
        CredencialDTO credencialDTO = new CredencialDTO();

        if(entidadCredencial != null) {
            credencialDTO = CredencialDTO.builder()
                    .credencial_id(entidadCredencial.getCredencial_id())
                    .usuario(entidadCredencial.getUsuario())
                    .contrasenia(entidadCredencial.getContrasenia())
                    .puerto(entidadCredencial.getPuerto())
                    .tipo_conexion(entidadCredencial.getTipo_conexion())
                    .privilegios(entidadCredencial.getPrivilegios())
                    .uso_destinado(entidadCredencial.getUso_destinado())
                    .primaria(entidadCredencial.isPrimaria())
                    .build();
        } return credencialDTO;
    }

    @Override
    public EntidadCredencial getOI(CredencialDTO credencialDTO) {
        EntidadCredencial entidadCredencial = new EntidadCredencial();

        if(credencialDTO != null) {
            entidadCredencial = EntidadCredencial.builder()
                    .credencial_id(credencialDTO.getCredencial_id())
                    .usuario(credencialDTO.getUsuario())
                    .contrasenia(credencialDTO.getContrasenia())
                    .puerto(credencialDTO.getPuerto())
                    .tipo_conexion(credencialDTO.getTipo_conexion())
                    .privilegios(credencialDTO.getPrivilegios())
                    .uso_destinado(credencialDTO.getUso_destinado())
                    .build();
        } return entidadCredencial;
    }

    @Override
    public List<CredencialDTO> getLIO(List<EntidadCredencial> credenciales) {
        return credenciales.stream().map(this::getIO).collect(Collectors.toList());
    }

}
