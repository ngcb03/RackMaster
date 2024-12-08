package com.ngcamargob.rackmaster.utilidades;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCuenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class AppUtil {

    @Autowired
    private IServCuenta servUsuario;


    // Obtener el usuario autenticado desde el contexto de seguridad
    public UserDetails traerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Validar si la autenticación es válida y el usuario está autenticado
        if (authentication != null && authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof UserDetails &&
                !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
            return (UserDetails) authentication.getPrincipal();
        } return null;
    }

    public String getNombreUsuario() {
        UserDetails usuarioAutenticado = this.traerUsuarioAutenticado();
        return usuarioAutenticado.getUsername();
    }

    // verificación longitud requerida campos texto
    public boolean esTexto160Largo(String texto) {
        return texto != null && !texto.isEmpty() && texto.length() <= 160;
    }

    public boolean verificarDatosCorrectosCredencial(EntidadCredencial entidadCredencial) {
        boolean validar = esTexto160Largo(entidadCredencial.getUsuario()) &&
                (entidadCredencial.getPuerto() >= 0 && entidadCredencial.getPuerto() < 1000000) &&
                esTexto160Largo(entidadCredencial.getTipo_conexion()) &&
                esTexto160Largo(entidadCredencial.getPrivilegios()) &&
                esTexto160Largo(entidadCredencial.getUso_destinado());

        if(!entidadCredencial.getContrasenia().isEmpty()) {
            validar = (entidadCredencial.getContrasenia().length() >= 8 && entidadCredencial.getContrasenia().length() <= 260);
        } return validar;
    }

}
