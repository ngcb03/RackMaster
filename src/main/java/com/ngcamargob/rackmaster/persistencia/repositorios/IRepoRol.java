package com.ngcamargob.rackmaster.persistencia.repositorios;

import com.ngcamargob.rackmaster.persistencia.entidades.enums_entity.EntidadRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepoRol extends JpaRepository<EntidadRol, Integer> {
}
