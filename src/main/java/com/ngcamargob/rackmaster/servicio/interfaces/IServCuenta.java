package com.ngcamargob.rackmaster.servicio.interfaces;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCuenta;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IServCuenta {

    Optional<EntidadCuenta> buscarCuenta(Integer cuenta_id);
    List<EntidadCuenta> buscarTodasCuentas();
    Page<EntidadCuenta> buscarCuentasPaginadas(int page, int pageSize);
    Optional<EntidadCuenta> buscarPorNombreUsuario(String usuario);
    List<EntidadCuenta> buscarPorFrase(String categoria, String frase);

    boolean crearCuenta(EntidadCuenta entidadCuenta);
    boolean modificarCuenta(EntidadCuenta entidadCuenta);
    boolean estadoCuenta(Integer cuenta_id, boolean estado);
    boolean eliminarCuenta(Integer cuenta_id);

}
