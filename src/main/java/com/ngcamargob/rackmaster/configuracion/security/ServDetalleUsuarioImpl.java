package com.ngcamargob.rackmaster.configuracion.security;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCuenta;
import com.ngcamargob.rackmaster.persistencia.repositorios.IRepoCuenta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServDetalleUsuarioImpl implements UserDetailsService {

    @Autowired
    private IRepoCuenta repoCuenta;

    private Logger LOGGER = LoggerFactory.getLogger(ServDetalleUsuarioImpl.class);


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<EntidadCuenta> cuenta = repoCuenta.findByUsuario(username);
        if(cuenta.isPresent() && cuenta.get().isHabilitado()) {
            return User.builder()
                    .username(cuenta.get().getUsuario())
                    .password(cuenta.get().getContrasenia())
                    .roles(cuenta.get().getEntidadRol().getRolUsuario().toString())
                    .disabled(!cuenta.get().isHabilitado())
                    .build();
        }
        new UsernameNotFoundException("Usuario bloqueado! :: " + cuenta.toString());
        return User.builder()
                .username("BLOQUEADO")
                .password("BLOQUEADO")
                .roles("BLOQUEADO")
                .disabled(true)
                .build();
    }

}
