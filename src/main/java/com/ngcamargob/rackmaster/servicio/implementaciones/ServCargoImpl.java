package com.ngcamargob.rackmaster.servicio.implementaciones;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCargo;
import com.ngcamargob.rackmaster.persistencia.repositorios.IRepoCargo;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCargo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServCargoImpl implements IServCargo {

    @Autowired
    private IRepoCargo repoCargo;


    @Override
    public List<EntidadCargo> buscarTodosCargos() {
        return repoCargo.findAll();
    }

}
