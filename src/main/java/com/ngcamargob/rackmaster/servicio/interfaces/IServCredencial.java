package com.ngcamargob.rackmaster.servicio.interfaces;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadMaquina;

import java.util.List;

public interface IServCredencial {
    
    List<EntidadCredencial> traerCredencialesMaquina(EntidadMaquina maquina);
    EntidadCredencial traerCredencial(Integer credencial_id);

    EntidadCredencial crearCredencial(EntidadCredencial credencial);
    EntidadCredencial modificarCredencial(EntidadCredencial credencial);
    void eliminarCredencial(Integer credencial_id);

}
