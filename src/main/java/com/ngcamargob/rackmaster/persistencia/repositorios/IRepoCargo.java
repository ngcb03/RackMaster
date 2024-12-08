package com.ngcamargob.rackmaster.persistencia.repositorios;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepoCargo extends JpaRepository<EntidadCargo, Integer> {
}
