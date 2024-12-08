package com.ngcamargob.rackmaster.servicio.implementaciones;

import com.ngcamargob.rackmaster.configuracion.security.SimpleEncryption;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadMaquina;
import com.ngcamargob.rackmaster.persistencia.repositorios.IRepoCredencial;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCredencial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServCredencialImpl implements IServCredencial {

    @Autowired
    private IRepoCredencial repoCredencial;


    @Override
    public List<EntidadCredencial> traerCredencialesMaquina(EntidadMaquina maquina) {
        return repoCredencial.findByMaquina(maquina);
    }

    @Override
    public EntidadCredencial traerCredencial(Integer credencial_id) {
        Optional<EntidadCredencial> optCredencial = repoCredencial.findById(credencial_id);
        if(optCredencial != null && optCredencial.isPresent()) {
            return optCredencial.get();
        } return null;
    }

    @Override
    @Transactional
    public EntidadCredencial crearCredencial(EntidadCredencial credencial) {
        credencial.setContrasenia(SimpleEncryption.encrypt(credencial.getContrasenia()));
        return repoCredencial.save(credencial);
    }

    @Override
    @Transactional
    public EntidadCredencial modificarCredencial(EntidadCredencial credencial) {
        Optional<EntidadCredencial> optCredencial = repoCredencial.findById(credencial.getCredencial_id());
        if(optCredencial.isPresent()) {
            if(credencial.getContrasenia() != null &&
                    !credencial.getContrasenia().isEmpty() &&
                    credencial.getContrasenia().length() > 7) {
                optCredencial.get().setContrasenia(SimpleEncryption.encrypt(credencial.getContrasenia()));
            }
            optCredencial.get().setUsuario(credencial.getUsuario());
            optCredencial.get().setPuerto(credencial.getPuerto());
            optCredencial.get().setTipo_conexion(credencial.getTipo_conexion());
            optCredencial.get().setPrivilegios(credencial.getPrivilegios());
            optCredencial.get().setUso_destinado(credencial.getUso_destinado());
            return repoCredencial.save(optCredencial.get());
        } return null;
    }

    @Override
    @Transactional
    public void eliminarCredencial(Integer credencial_id) {
        repoCredencial.deleteById(credencial_id);
    }

}
