package com.ngcamargob.rackmaster.servicio.implementaciones;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadRol;
import com.ngcamargob.rackmaster.persistencia.repositorios.IRepoRol;
import com.ngcamargob.rackmaster.servicio.interfaces.IServRol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServRolImpl implements IServRol {

    @Autowired
    private IRepoRol repoRol;


    @Override
    public List<EntidadRol> buscarTodosRoles() {
        return repoRol.findAll();
    }

}
