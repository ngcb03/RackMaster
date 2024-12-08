package com.ngcamargob.rackmaster.persistencia.repositorios;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepoCluster extends JpaRepository<EntidadCluster, Integer> {

    @Query("SELECT c FROM EntidadCluster c WHERE " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(c.proyecto) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(c.sede) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<EntidadCluster> searchByGeneralValue(@Param("value") String value);

    @Query("SELECT c FROM EntidadCluster c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadCluster> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT c FROM EntidadCluster c WHERE LOWER(c.proyecto) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadCluster> findByProyectoContainingIgnoreCase(String proyecto);

    @Query("SELECT c FROM EntidadCluster c WHERE LOWER(c.sede) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadCluster> findBySedeContainingIgnoreCase(String sede);

}
