package com.ngcamargob.rackmaster.servicio.interfaces;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadMaquina;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IServMaquina {

    List<EntidadMaquina> buscarTodasMaquinas();
    EntidadMaquina buscarMaquina(Integer maquina_id);
    EntidadMaquina buscarMaquinaConCredencialesDecryp(Integer maquina_id);
    Page<EntidadMaquina> buscarMaquinasPaginadas(int page, int pageSize);
    List<EntidadMaquina> buscarPorFrase(String frase, String categoria);

    EntidadMaquina crearMaquina(EntidadMaquina entidadMaquina, String hipervisor_id, String hipervisor_asignar);
    EntidadMaquina modificarMaquina(EntidadMaquina entidadMaquina);
    EntidadMaquina modificarMaquina(EntidadMaquina entidadMaquina, String hipervisor_id, String hipervisor_asignar);
    void eliminarMaquina(Integer maquina_id);

    void guardarCredencial(EntidadCredencial credencial, Integer credencial_id);
    Integer eliminarCredencial(Integer maquina_id);

}
