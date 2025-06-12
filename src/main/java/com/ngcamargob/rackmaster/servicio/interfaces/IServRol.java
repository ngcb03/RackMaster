package com.ngcamargob.rackmaster.servicio.interfaces;

import com.ngcamargob.rackmaster.persistencia.entidades.enums_entity.EntidadRol;

import java.util.List;

public interface IServRol {

    List<EntidadRol> buscarTodosRoles();

}
