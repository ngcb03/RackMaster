package com.ngcamargob.rackmaster.servicio.interfaces;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCargo;

import java.util.List;

public interface IServCargo {

    List<EntidadCargo> buscarTodosCargos();

}
