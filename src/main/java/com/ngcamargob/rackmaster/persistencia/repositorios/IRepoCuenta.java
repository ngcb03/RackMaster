package com.ngcamargob.rackmaster.persistencia.repositorios;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRepoCuenta extends JpaRepository<EntidadCuenta, Integer> {

    Page<EntidadCuenta> findAll(Pageable pageable);

    @Query("SELECT c FROM EntidadCuenta c WHERE c.usuario = ?1")
    Optional<EntidadCuenta> findByUsuario(String cuenta);

    @Query("SELECT c FROM EntidadCuenta c WHERE " +
            "LOWER(c.nombre_completo) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(c.usuario) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<EntidadCuenta> searchByGeneralValue(@Param("value") String value);

    @Query("SELECT c FROM EntidadCuenta c WHERE LOWER(c.nombre_completo) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadCuenta> findByNombre_completoContainingIgnoreCase(String nombre_completo);

    @Query("SELECT c FROM EntidadCuenta c WHERE LOWER(c.usuario) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadCuenta> findByUsuarioContainingIgnoreCase(String usuario);

    @Query("SELECT c FROM EntidadCuenta c WHERE c.habilitado = ?1")
    List<EntidadCuenta> findByHabilitado(boolean habilitado);

}
