package com.ngcamargob.rackmaster.servicio.interfaces;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IServServidor {

    List<EntidadServidor> buscarTodosServidores();
    Page<EntidadServidor> buscarServidoresPaginados(int page, int pageSize);
    EntidadServidor buscarServidor(Integer servidor_id);
    EntidadServidor buscarServidorConCredencialesDecryp(Integer servidor_id);

    List<EntidadServidor> buscarPorFrase(String frase, String categoria);

    EntidadServidor crearServidor(EntidadServidor servidor, String cluster_id, String cluster_asignar);
    EntidadServidor modificarServidor(EntidadServidor servidor);
    EntidadServidor modificarServidor(EntidadServidor servidor, String cluster_id, String cluster_asignar);
    void eliminarServidor(Integer servidor_id);

    void guardarCredencial(EntidadCredencial credencial, Integer servidor_id);
    Integer eliminarCredencial(Integer credencial_id);

}
