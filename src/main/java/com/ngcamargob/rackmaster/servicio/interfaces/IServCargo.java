package com.ngcamargob.rackmaster.servicio.interfaces;

import com.ngcamargob.rackmaster.persistencia.entidades.enums_entity.EntidadCargo;

import java.util.List;

public interface IServCargo {

    List<EntidadCargo> buscarTodosCargos();

}
