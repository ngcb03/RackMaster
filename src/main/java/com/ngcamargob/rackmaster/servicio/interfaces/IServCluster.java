package com.ngcamargob.rackmaster.servicio.interfaces;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCluster;

import java.util.List;

public interface IServCluster {

    List<EntidadCluster> buscarTodosClusters();
    EntidadCluster buscarCluster(Integer cluter_id);
    List<EntidadCluster> buscarPorFrase(String frase, String categoria);
    EntidadCluster crearCluster(EntidadCluster entidadCluster, String servidoresIds);
    EntidadCluster modificarCluster(EntidadCluster entidadCluster, String servidoresIds);
    void eliminarCluster(Integer cluter_id);

}
