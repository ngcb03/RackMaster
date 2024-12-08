package com.ngcamargob.rackmaster.servicio.implementaciones;

import com.ngcamargob.rackmaster.configuracion.security.SimpleEncryption;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import com.ngcamargob.rackmaster.persistencia.repositorios.IRepoServidor;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCredencial;
import com.ngcamargob.rackmaster.servicio.interfaces.IServServidor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ServServidorImpl implements IServServidor {

    @Autowired
    private IRepoServidor repoServidor;

    @Autowired
    private IServCredencial servCredencial;

    private final static Logger LOGGER = LoggerFactory.getLogger(ServServidorImpl.class);


    private List<EntidadServidor> ordenarListaServidores(List<EntidadServidor> servidores) {
        return servidores.stream()
                .sorted(Comparator.comparing(EntidadServidor::getNombre, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    @Override
    public List<EntidadServidor> buscarTodosServidores() {
        return ordenarListaServidores(repoServidor.findAll());
    }

    @Override
    public Page<EntidadServidor> buscarServidoresPaginados(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repoServidor.findAll(pageable);
    }

    @Override
    public EntidadServidor buscarServidor(Integer servidor_id) {
        Optional<EntidadServidor> optServidor = repoServidor.findById(servidor_id);
        if(optServidor != null && optServidor.isPresent()) {
            return optServidor.get();
        } return null;
    }

    @Override
    public EntidadServidor buscarServidorConCredencialesDecryp(Integer servidor_id) {
        Optional<EntidadServidor> optServidor = repoServidor.findById(servidor_id);

        if(optServidor != null && optServidor.isPresent()) {
            optServidor.get().setCredenciales(
                    optServidor.get().getCredenciales().stream()
                            .peek(c -> c.setContrasenia(SimpleEncryption.decrypt(c.getContrasenia())))
                            .collect(Collectors.toList())
            ); return optServidor.get();
        } return null;
    }

    @Override
    public List<EntidadServidor> buscarPorFrase(String frase, String categoria) {
        List<EntidadServidor> servidores = new ArrayList<>();
        switch (categoria.toUpperCase()) {
            case "TODO":
                servidores = repoServidor.searchByGeneralValue(frase);
                break;

            case "SEDE":
                servidores = repoServidor.findBySedeContainingIgnoreCase(frase);
                break;

            case "NOMBRE":
                servidores = repoServidor.findByNombreContainingIgnoreCase(frase);
                break;

            case "IP":
                servidores = repoServidor.findByIpContainingIgnoreCase(frase);
                break;

            case "MAC":
                servidores = repoServidor.findByMacContainingIgnoreCase(frase);
                break;

            case "SISTEMA_OP":
                servidores = repoServidor.findBySistema_opContainingIgnoreCase(frase);
                break;

            case "MODELO":
                servidores =repoServidor.findByModeloContainingIgnoreCase(frase);
                break;

            case "SERIAL":
                servidores = repoServidor.findBySerialContainingIgnoreCase(frase);
                break;

            case "PLACA":
                try {
                    servidores = repoServidor.findByPlaca(Integer.parseInt(frase));
                } catch (Exception e) { return null; }
                break;

            case "RACK":
                servidores = repoServidor.findByRackContainingIgnoreCase(frase);
                break;

            case "EN_USO":
                servidores = repoServidor.findByEn_Uso(true);
                break;

            case "DISPONIBLE":
                servidores = repoServidor.findByEn_Uso(false);
                break;

        } return ordenarListaServidores(servidores);
    }

    @Override
    @Transactional
    public EntidadServidor crearServidor(EntidadServidor servidor, String cluster_id, String cluster_asignar) {
        // guardamos servidor en db
        return repoServidor.save(servidor);
    }

    @Override
    public EntidadServidor modificarServidor(EntidadServidor servidor) {
        return modificarServidor(servidor, "", "");
    }

    @Override
    @Transactional
    public EntidadServidor modificarServidor(EntidadServidor entidadServidor, String cluster_id, String cluster_asignar) {
        Optional<EntidadServidor> optServidor = repoServidor.findById(entidadServidor.getServidor_id());
        if(optServidor.isPresent()) {

            entidadServidor.setMaquinas(optServidor.get().getMaquinas());
            entidadServidor.setCredenciales(optServidor.get().getCredenciales());
            return repoServidor.save(entidadServidor);

        } return null;
    }

    @Override
    @Transactional
    public void guardarCredencial(EntidadCredencial credencial, Integer servidor_id) {
        EntidadServidor servidor;
        if(credencial.getCredencial_id() == null){
            servidor = buscarServidor(servidor_id);
            if(servidor != null) {
                credencial.setServidor(servidor);
                servCredencial.crearCredencial(credencial);
            }
        } else {
            servCredencial.modificarCredencial(credencial);
        }
    }

    @Override
    @Transactional
    public Integer eliminarCredencial(Integer credencial_id) {
        EntidadCredencial credencial = servCredencial.traerCredencial(credencial_id);
        if(credencial != null && credencial.getServidor() != null) {
            EntidadServidor servidorSave = credencial.getServidor();
            // removemos asociación con credenciales del servidor
            servidorSave.getCredenciales().remove(credencial);
            servidorSave = modificarServidor(servidorSave);

            // eliminamos credencial
            servCredencial.eliminarCredencial(credencial.getCredencial_id());
            return servidorSave.getServidor_id();
        } return null;
    }

    @Override
    @Transactional
    public void eliminarServidor(Integer servidor_id) {
        repoServidor.deleteById(servidor_id);
    }

}
