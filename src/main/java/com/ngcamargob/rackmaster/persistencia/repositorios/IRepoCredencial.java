package com.ngcamargob.rackmaster.persistencia.repositorios;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadMaquina;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepoCredencial extends JpaRepository <EntidadCredencial, Integer> {

    @Query("SELECT m FROM EntidadCredencial m WHERE m.servidor = ?1")
    List<EntidadCredencial> findByServidor(EntidadServidor servidor);

    @Query("SELECT m FROM EntidadCredencial m WHERE m.maquina = ?1")
    List<EntidadCredencial> findByMaquina(EntidadMaquina maquina);

}
