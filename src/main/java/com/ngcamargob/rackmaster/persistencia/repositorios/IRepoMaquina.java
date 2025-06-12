package com.ngcamargob.rackmaster.persistencia.repositorios;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadMaquina;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepoMaquina extends JpaRepository<EntidadMaquina, Integer> {

    Page<EntidadMaquina> findAll(Pageable pageable);

    @Query("SELECT m FROM EntidadMaquina m WHERE " +
            "LOWER(m.nombre) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(m.nombre_en_hipervisor) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "CAST(m.id_en_hipervisor AS string) LIKE CONCAT('%', :value, '%') OR " +
            "LOWER(m.ip) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(m.mac) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(m.sistema_op) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(m.servicio) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(m.proyecto) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(m.aplicacion) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<EntidadMaquina> searchByGeneralValue(@Param("value") String value);

    @Query("SELECT m FROM EntidadMaquina m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadMaquina> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT m FROM EntidadMaquina m WHERE LOWER(m.nombre_en_hipervisor) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadMaquina> findByNombre_en_hipervisorContainingIgnoreCase(String nombre_en_hipervisor);

    @Query("SELECT m FROM EntidadMaquina m WHERE m.id_en_hipervisor = ?1")
    List<EntidadMaquina> findById_en_hipervisor(Integer id_en_hipervisor);

    @Query("SELECT m FROM EntidadMaquina m WHERE LOWER(m.ip) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadMaquina> findByIpContainingIgnoreCase(String ip);

    @Query("SELECT m FROM EntidadMaquina m WHERE LOWER(m.mac) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadMaquina> findByMacContainingIgnoreCase(String mac);

    @Query("SELECT m FROM EntidadMaquina m WHERE LOWER(m.sistema_op) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadMaquina> findBySistema_opContainingIgnoreCase(String sistema_op);

    @Query("SELECT m FROM EntidadMaquina m WHERE LOWER(m.proyecto) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadMaquina> findByProyectoContainingIgnoreCase(String proyecto);

    @Query("SELECT m FROM EntidadMaquina m WHERE LOWER(m.aplicacion) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadMaquina> findByAplicacionContainingIgnoreCase(String aplicacion);

    @Query("SELECT m FROM EntidadMaquina m WHERE LOWER(m.servicio) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadMaquina> findByServicioContainingIgnoreCase(String servicio);

    @Query("SELECT m FROM EntidadMaquina m WHERE m.en_uso = ?1")
    List<EntidadMaquina> findByEn_Uso(boolean en_uso);


}
