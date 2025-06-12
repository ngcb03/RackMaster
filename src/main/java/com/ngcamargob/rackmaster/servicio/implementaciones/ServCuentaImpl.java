package com.ngcamargob.rackmaster.servicio.implementaciones;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCuenta;
import com.ngcamargob.rackmaster.persistencia.repositorios.IRepoCuenta;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServCuentaImpl implements IServCuenta {

    @Autowired
    private IRepoCuenta repoCuenta;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();;


    private List<EntidadCuenta> ordenarListaCuentas(List<EntidadCuenta> cuentas) {
        return cuentas.stream()
                .sorted(Comparator.comparing(c -> {
                    String rol = c.getEntidadRol().getRolUsuario().toString();
                    return switch (rol) {
                        case "SUPER_ADMIN" -> 1;
                        case "ADMIN" -> 2;
                        case "USER" -> 3;
                        default -> Integer.MAX_VALUE;
                    };
                }))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EntidadCuenta> buscarCuenta(Integer cuenta_id) {
        return repoCuenta.findById(cuenta_id);
    }

    @Override
    public Optional<EntidadCuenta> buscarPorNombreUsuario(String usuario) {
        return repoCuenta.findByUsuario(usuario);
    }

    @Override
    public List<EntidadCuenta> buscarTodasCuentas() {
        return ordenarListaCuentas(repoCuenta.findAll());
    }

    @Override
    public Page<EntidadCuenta> buscarCuentasPaginadas(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repoCuenta.findAll(pageable);
    }

    @Override
    public List<EntidadCuenta> buscarPorFrase(String categoria, String frase) {
        String categoria_resultante = categoria.trim().replace("CAT_", "");
        List<EntidadCuenta> cuentas = new ArrayList<>();

        switch (categoria_resultante) {
            case "TODO":
                cuentas = repoCuenta.searchByGeneralValue(frase);
                break;
            case "JEFE", "LIDER", "ANALISTA", "OPERADOR", "OTRO":
                cuentas = repoCuenta.findAll().stream().filter(
                            c -> c.getEntidadCargo().getCargoUsuario().toString()
                                    .equals(categoria_resultante))
                    .collect(Collectors.toList());
                break;
            case "SUPER_ADMIN", "ADMIN", "USER":
                    cuentas = repoCuenta.findAll().stream().filter(
                            c -> c.getEntidadRol().getRolUsuario().toString()
                                    .equals(categoria_resultante))
                    .collect(Collectors.toList());
                    break;
            case "NOMBRE":
                cuentas = repoCuenta.findByNombre_completoContainingIgnoreCase(frase);
                break;
            case "USUARIO":
                cuentas = repoCuenta.findByUsuarioContainingIgnoreCase(frase);
                break;
            case "HABILITADO":
                cuentas = repoCuenta.findByHabilitado(true);
                break;
            case "DESHABILITADO":
                cuentas = repoCuenta.findByHabilitado(false);
                break;
        } return ordenarListaCuentas(cuentas);
    }

    @Override
    @Transactional
    public boolean crearCuenta(EntidadCuenta entidadCuenta) {
        if(repoCuenta.findByUsuario(entidadCuenta.getUsuario()).isEmpty()) {
            if(!entidadCuenta.getContrasenia().isEmpty()){
                entidadCuenta.setContrasenia(bCryptPasswordEncoder.encode(entidadCuenta.getContrasenia()));
            } entidadCuenta.setHabilitado(true);
            repoCuenta.save(entidadCuenta);
            return true;
        } return false;
    }

    @Override
    @Transactional
    public boolean modificarCuenta(EntidadCuenta entidadCuenta) {
        Optional<EntidadCuenta> optCuenta = buscarCuenta(entidadCuenta.getCuenta_id());
        if(optCuenta.isPresent()) {
            if(entidadCuenta.getContrasenia().isEmpty()){
                entidadCuenta.setContrasenia(optCuenta.get().getContrasenia());
            } else {
                entidadCuenta.setContrasenia(
                        bCryptPasswordEncoder.encode(entidadCuenta.getContrasenia()));
            }

            if(entidadCuenta.getEntidadCargo().getCargoUsuario() == null) {
                entidadCuenta.setEntidadCargo(optCuenta.get().getEntidadCargo());
            }

            if(entidadCuenta.getEntidadRol().getRolUsuario() == null) {
                entidadCuenta.setEntidadRol(optCuenta.get().getEntidadRol());
            }

            repoCuenta.save(entidadCuenta);
            return true;
        } return false;
    }

    @Override
    public boolean estadoCuenta(Integer cuenta_id, boolean estado) {
        Optional<EntidadCuenta> optCuenta = repoCuenta.findById(cuenta_id);
        if (optCuenta.isPresent()) {
            optCuenta.get().setHabilitado(estado);
            repoCuenta.save(optCuenta.get());
            return true;
        } return false;
    }

    @Override
    @Transactional
    public boolean eliminarCuenta(Integer usuario_id) {
        repoCuenta.deleteById(usuario_id);
        return true;
    }

}
