package com.ngcamargob.rackmaster.persistencia.repositorios;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IRepoServidor extends JpaRepository<EntidadServidor, Integer> {

    Page<EntidadServidor> findAll(Pageable pageable);

    @Query("SELECT s FROM EntidadServidor s WHERE s.nombre = ?1")
    Optional<EntidadServidor> findByNombre(String nombre);

    @Query("SELECT s FROM EntidadServidor s WHERE " +
            "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(s.ip) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(s.mac) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(s.sistema_op) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(s.modelo) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(s.procesador) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(s.serial) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "CAST(s.placa AS string) LIKE CONCAT('%', :value, '%') OR " +
            "LOWER(s.rack) LIKE LOWER(CONCAT('%', :value, '%')) OR " +
            "LOWER(s.sede) LIKE LOWER(CONCAT('%', :value, '%'))")
    List<EntidadServidor> searchByGeneralValue(@Param("value") String value);

    @Query("SELECT s FROM EntidadServidor s WHERE LOWER(s.nombre) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadServidor> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT s FROM EntidadServidor s WHERE LOWER(s.ip) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadServidor> findByIpContainingIgnoreCase(String ip);

    @Query("SELECT s FROM EntidadServidor s WHERE LOWER(s.mac) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadServidor> findByMacContainingIgnoreCase(String mac);

    @Query("SELECT s FROM EntidadServidor s WHERE LOWER(s.sistema_op) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadServidor> findBySistema_opContainingIgnoreCase(String sistema_op);

    @Query("SELECT s FROM EntidadServidor s WHERE LOWER(s.modelo) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadServidor> findByModeloContainingIgnoreCase(String modelo);

    @Query("SELECT s FROM EntidadServidor s WHERE LOWER(s.serial) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadServidor> findBySerialContainingIgnoreCase(String serial);

    @Query("SELECT s FROM EntidadServidor s WHERE s.placa = ?1")
    List<EntidadServidor> findByPlaca(Integer placa);

    @Query("SELECT s FROM EntidadServidor s WHERE LOWER(s.rack) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadServidor> findByRackContainingIgnoreCase(String rack);

    @Query("SELECT s FROM EntidadServidor s WHERE LOWER(s.sede) LIKE LOWER(CONCAT('%', ?1, '%'))")
    List<EntidadServidor> findBySedeContainingIgnoreCase(String sede);

    @Query("SELECT s FROM EntidadServidor s WHERE s.en_uso = ?1")
    List<EntidadServidor> findByEn_Uso(boolean en_uso);

}
