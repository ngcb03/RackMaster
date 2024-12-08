package com.ngcamargob.rackmaster.servicio.implementaciones;

import com.ngcamargob.rackmaster.configuracion.security.SimpleEncryption;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadMaquina;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import com.ngcamargob.rackmaster.persistencia.repositorios.IRepoMaquina;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCredencial;
import com.ngcamargob.rackmaster.servicio.interfaces.IServMaquina;
import com.ngcamargob.rackmaster.servicio.interfaces.IServServidor;
import com.ngcamargob.rackmaster.utilidades.AppUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServMaquinaImpl implements IServMaquina {

    @Autowired
    private IRepoMaquina repoMaquina;

    @Autowired
    private IServServidor servServidor;

    @Autowired
    private IServCredencial servCredencial;

    @Autowired
    private AppUtil appUtil;

    private final static Logger LOGGER = LoggerFactory.getLogger(ServMaquinaImpl.class);


    private List<EntidadMaquina> ordenarListaMaquinas(List<EntidadMaquina> maquinas) {
        return maquinas.stream()
                .sorted(Comparator.comparing(EntidadMaquina::getNombre, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    @Override
    public Page<EntidadMaquina> buscarMaquinasPaginadas(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repoMaquina.findAll(pageable);
    }

    @Override
    public List<EntidadMaquina> buscarTodasMaquinas() {
        return ordenarListaMaquinas(repoMaquina.findAll());
    }

    @Override
    public EntidadMaquina buscarMaquina(Integer maquina_id) {
        Optional<EntidadMaquina> optMaquina = repoMaquina.findById(maquina_id);
        if (optMaquina != null && optMaquina.isPresent()){
            return optMaquina.get();
        } return null;
    }

    @Override
    public EntidadMaquina buscarMaquinaConCredencialesDecryp(Integer maquina_id) {
        Optional<EntidadMaquina> optMaquina = repoMaquina.findById(maquina_id);
        if(optMaquina.isPresent()) {
            optMaquina.get().setCredenciales(
                    optMaquina.get().getCredenciales().stream()
                            .map(c -> {
                                c.setContrasenia(SimpleEncryption.decrypt(c.getContrasenia()));
                                return c;
                            }).collect(Collectors.toList())
            ); return optMaquina.get();
        } return null;
    }

    @Override
    public List<EntidadMaquina> buscarPorFrase(String frase, String categoria) {
        List<EntidadMaquina> maquinas = new ArrayList<>();
        switch (categoria.toUpperCase()) {
            case "TODO":
                maquinas = repoMaquina.searchByGeneralValue(frase);
                break;

            case "SEDE":
                maquinas = repoMaquina.findAll().stream()
                        .filter(m -> frase.equals(m.getServidor().getSede()))
                        .collect(Collectors.toList());

                break;

            case "NOMBRE":
                maquinas = repoMaquina.findByNombreContainingIgnoreCase(frase);
                break;

            case "NOMBRE_EN_HIPERVISOR":
                maquinas = repoMaquina.findByNombre_en_hipervisorContainingIgnoreCase(frase);
                break;

            case "ID_EN_HIPERVISOR":

                try { maquinas = repoMaquina.findById_en_hipervisor(Integer.parseInt(frase));
                } catch (Exception e) { return null; }
                break;

            case "IP":
                maquinas = repoMaquina.findByIpContainingIgnoreCase(frase);
                break;

            case "MAC":
                maquinas = repoMaquina.findByMacContainingIgnoreCase(frase);
                break;

            case "SISTEMA_OP":
                maquinas = repoMaquina.findBySistema_opContainingIgnoreCase(frase);
                break;

            case "PROYECTO":
                maquinas = repoMaquina.findByProyectoContainingIgnoreCase(frase);
                break;

            case "APLICACION":
                maquinas = repoMaquina.findByAplicacionContainingIgnoreCase(frase);
                break;

            case "SERVICIO":
                maquinas = repoMaquina.findByServicioContainingIgnoreCase(frase);
                break;

            case "ENCENDIDAS":
                maquinas = repoMaquina.findByEn_Uso(true);
                break;

            case "APAGADAS":
                maquinas = repoMaquina.findByEn_Uso(false);
                break;

        }; return ordenarListaMaquinas(maquinas);
    }

    @Override
    @Transactional
    public EntidadMaquina crearMaquina(EntidadMaquina entidadMaquina,
                                       String hipervisor_id,
                                       String hipervisor_asignar) {
        try {
            // le asignamos servidor a asociar
            if (!hipervisor_id.isEmpty() && !hipervisor_asignar.isEmpty()) {
                EntidadServidor servidor = servServidor.buscarServidor(Integer.parseInt(hipervisor_id));
                if(servidor != null) {
                    entidadMaquina.setServidor(servidor);
                }
            }
        } catch (Exception e) {
            LOGGER.info("¡ID incorrecta de servidor a asociar {} !", e.getMessage());
        }

        // creamos máquina en db
        return repoMaquina.save(entidadMaquina);

    }

    @Override
    public EntidadMaquina modificarMaquina(EntidadMaquina entidadMaquina) {
        return modificarMaquina(entidadMaquina, "", "");
    }

    @Override
    @Transactional
    public EntidadMaquina modificarMaquina(EntidadMaquina entidadMaquina,
                                    String hipervisor_id,
                                    String hipervisor_asignar) {
        Optional<EntidadMaquina> optMaquina = repoMaquina.findById(entidadMaquina.getMaquina_id());
        if(optMaquina.isPresent()) {

            try {
                // buscamos servidor a asociar
                if (!hipervisor_id.isEmpty() && !hipervisor_asignar.isEmpty()) {
                    EntidadServidor servidor = servServidor.buscarServidor(Integer.parseInt(hipervisor_id));
                    if(servidor != null) {
                        entidadMaquina.setServidor(servidor);
                    }
                }
            } catch (Exception e) {
                LOGGER.info("¡ID incorrecta de servidor a asociar {} !", e.getCause());
            }

            entidadMaquina.setCredenciales(optMaquina.get().getCredenciales());
            return repoMaquina.save(entidadMaquina);

        } return null;
    }

    @Override
    public void guardarCredencial(EntidadCredencial credencial, Integer maquina_id) {

        // verificamos que datos de credencial sean correctos
        if(appUtil.verificarDatosCorrectosCredencial(credencial)) {
            if(credencial.getCredencial_id() == null){
                Optional<EntidadMaquina> optMaquina = repoMaquina.findById(maquina_id);
                if(optMaquina != null && optMaquina.isPresent()) {
                    credencial.setMaquina(optMaquina.get());
                    servCredencial.crearCredencial(credencial);
                }
            } else { servCredencial.modificarCredencial(credencial); }
        }

    }

    @Override
    public Integer eliminarCredencial(Integer credencial_id) {
        EntidadCredencial credencial = servCredencial.traerCredencial(credencial_id);
        if(credencial != null && credencial.getMaquina() != null) {
            EntidadMaquina maquina = credencial.getMaquina();
            if(maquina != null) {
                maquina.getCredenciales().remove(credencial);
                repoMaquina.save(maquina);
                servCredencial.eliminarCredencial(credencial.getCredencial_id());
                return maquina.getMaquina_id();
            }
        } return null;
    }

    @Override
    @Transactional
    public void eliminarMaquina(Integer maquina_id) {
        Optional<EntidadMaquina> optMaquina = repoMaquina.findById(maquina_id);

        if(optMaquina != null && optMaquina.isPresent() && optMaquina.get().getServidor() != null) {
            EntidadServidor servidor = servServidor.buscarServidor(optMaquina.get().getServidor().getServidor_id());
            List<EntidadCredencial> credenciales = servCredencial.traerCredencialesMaquina(optMaquina.get());

            if(servidor != null && !credenciales.isEmpty()) {
                // removemos asociación servidores y guardamos en db
                servidor.getMaquinas().removeIf(m -> m.getMaquina_id().equals(maquina_id));
                servServidor.modificarServidor(servidor);

                // removemos asociación credencial y guardamos en db
                credenciales.forEach(c -> servCredencial.eliminarCredencial(c.getCredencial_id()));

                // eliminamos máquina db
                repoMaquina.deleteById(maquina_id);

            }
        }

    }

}
