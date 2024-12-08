package com.ngcamargob.rackmaster.servicio.implementaciones;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCluster;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import com.ngcamargob.rackmaster.persistencia.repositorios.IRepoCluster;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCluster;
import com.ngcamargob.rackmaster.servicio.interfaces.IServServidor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ServClusterImpl implements IServCluster {

    @Autowired
    private IRepoCluster repoCluster;

    @Autowired
    private IServServidor servServidor;

    @Override
    public List<EntidadCluster> buscarTodosClusters() {
        List<EntidadCluster> clusters = repoCluster.findAll();
        if (clusters != null) {
            return clusters;
        } return new ArrayList<>();
    }

    @Override
    public EntidadCluster buscarCluster(Integer cluter_id) {
        Optional<EntidadCluster> optCluster = repoCluster.findById(cluter_id);
        if (optCluster != null && optCluster.isPresent()) {
            return optCluster.get();
        } return null;
    }

    @Override
    public List<EntidadCluster> buscarPorFrase(String frase, String categoria) {
        return switch (categoria.toUpperCase()) {
            case "TODO" -> repoCluster.searchByGeneralValue(frase);
            case "NOMBRE" -> repoCluster.findByNombreContainingIgnoreCase(frase);
            case "PROYECTO" -> repoCluster.findByProyectoContainingIgnoreCase(frase);
            case "SEDE" -> repoCluster.findBySedeContainingIgnoreCase(frase);
            default -> null;
        };
    }

    @Override
    @Transactional
    public EntidadCluster crearCluster(EntidadCluster entidadCluster,
                                       String servidoresIds) {

        // creamos cluster y lo guardamos en db si asociar
        EntidadCluster cluster = repoCluster.save(entidadCluster);

        if(servidoresIds != null && !servidoresIds.isEmpty()) {
            List<Integer> listaServidoresIds = Arrays.stream(servidoresIds.split(","))
                    .map(Integer::parseInt)
                    .toList();

            // Buscamos servidores por sus IDs en una sola operación
            List<EntidadServidor> servidores = servServidor.buscarTodosServidores().stream()
                    .filter(s -> listaServidoresIds.contains(s.getServidor_id()))
                    .toList();

            // Verificamos si faltan servidores que no se encontraron
            if (servidores.size() != listaServidoresIds.size()) {
                throw new RuntimeException("Algunos servidores no fueron encontrados con los IDs proporcionados.");
            }

            // Asociamos el cluster a cada servidor
            servidores.forEach(s -> s.setCluster(cluster));
            servidores.forEach(s -> servServidor.modificarServidor(s));

        } return cluster;
    }

    @Override
    @Transactional
    public EntidadCluster modificarCluster(EntidadCluster entidadCluster, String servidoresIds) {

        Optional<EntidadCluster> optCluster = repoCluster.findById(entidadCluster.getCluster_id());
        if (optCluster.isPresent()) {
            EntidadCluster clusterExistente = optCluster.get();

            clusterExistente.setNombre(entidadCluster.getNombre());
            clusterExistente.setProyecto(entidadCluster.getProyecto());
            clusterExistente.setSede(entidadCluster.getSede());

            if(servidoresIds != null && !servidoresIds.isEmpty()) {
                List<Integer> listaServidoresIds = Arrays.stream(servidoresIds.split(","))
                        .map(Integer::parseInt)
                        .toList();

                // Buscamos servidores por sus IDs en una sola operación
                List<EntidadServidor> servidores = servServidor.buscarTodosServidores().stream()
                        .filter(s -> listaServidoresIds.contains(s.getServidor_id()))
                        .toList();

                // Verificamos si faltan servidores que no se encontraron
                if (servidores.size() != listaServidoresIds.size()) {
                    throw new RuntimeException("Algunos servidores no fueron encontrados con los IDs proporcionados.");
                }

                if(clusterExistente.getServidores() != null) {
                    // Actualizamos servidores a los cuales le removieron la asociación
                    List<EntidadServidor> servidoresActualizar = clusterExistente.getServidores().stream()
                            .filter(s -> !listaServidoresIds.contains(s.getServidor_id())) // Solo mantiene los que están en la lista de IDs proporcionados
                            .toList();
                    if (!servidoresActualizar.isEmpty()) {
                        servidoresActualizar.forEach(s -> s.setCluster(null));
                        servidoresActualizar.forEach(s -> servServidor.modificarServidor(s));
                    }
                }

                // Asociamos el clusterExistente a cada servidor
                if(!servidores.isEmpty()) {
                    servidores.forEach(s -> s.setCluster(clusterExistente));
                    servidores.forEach(s -> servServidor.modificarServidor(s));
                }

            } else {
                // en caso de que se no se envíen clusters a asociar
                // desvinculamos todos los servidores que esten asociados
                clusterExistente.getServidores().forEach(s -> {
                    s.setCluster(null);
                    servServidor.modificarServidor(s);
                });
                clusterExistente.setServidores(null);
            }

            // guardamos cluster a modificar en db
            return repoCluster.save(clusterExistente);

        } return null;

    }

    @Override
    @Transactional
    public void eliminarCluster(Integer cluster_id) {
        repoCluster.deleteById(cluster_id);
    }

}
