package com.ngcamargob.rackmaster.presentacion.controladora;


import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCluster;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadMaquina;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import com.ngcamargob.rackmaster.presentacion.dto.ClusterDTO;
import com.ngcamargob.rackmaster.presentacion.dto.MaquinaDTO;
import com.ngcamargob.rackmaster.presentacion.dto.ServidorDTO;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCluster;
import com.ngcamargob.rackmaster.servicio.interfaces.IServMaquina;
import com.ngcamargob.rackmaster.servicio.interfaces.IServServidor;
import com.ngcamargob.rackmaster.utilidades.AppUtil;
import com.ngcamargob.rackmaster.utilidades.mapper.ConvClusterDTO;
import com.ngcamargob.rackmaster.utilidades.mapper.ConvMaquinaDTO;
import com.ngcamargob.rackmaster.utilidades.mapper.ConvServidorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rackmaster/clusters")
public class ClustersControl {

    @Autowired
    private IServCluster servCluster;

    @Autowired
    private IServServidor servServidor;

    @Autowired
    private IServMaquina servMaquina;

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private ConvClusterDTO convClusterDTO;

    @Autowired
    private ConvServidorDTO convServidorDTO;

    @Autowired
    private ConvMaquinaDTO convMaquinaDTO;


    private ClusterDTO clusterAux = new ClusterDTO();
    private List<String> datos_duplicados = new ArrayList<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(ClustersControl.class);


    @GetMapping("/a2/devolver-clusters")
    @ResponseBody
    public List<ClusterDTO> getClusters() {
        List<EntidadCluster> clusters = servCluster.buscarTodosClusters() ;
        if(clusters != null) {
            return convClusterDTO.getLIO(clusters);
        } return new ArrayList<>();
    }

    @GetMapping("/a1")
    public String clusterUI(Model model) {
        model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
        model.addAttribute("isFilter", false);
        model.addAttribute("clusters", convClusterDTO.getLIO(servCluster.buscarTodosClusters()));
        return "clusters";
    }

    @GetMapping("/a1/buscar")
    public String buscarClusters(@RequestParam(required = false) String frase,
                                 @RequestParam String categoria,
                                 Model model) {

        List<EntidadCluster> clusters = servCluster.buscarPorFrase(frase, categoria);
        if(clusters != null) {

            model.addAttribute("clusters", convClusterDTO.getLIO(clusters));
            model.addAttribute("isFilter", true);
            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            return "clusters";

        } return "redirect:/rackmaster/clusters/a1";
    }

    @GetMapping("/a1/{cluster_id}")
    public String infoClusterUI(@PathVariable Integer cluster_id,
                                Model model) {
        EntidadCluster cluster = servCluster.buscarCluster(cluster_id);
        if(cluster != null) {

            List<ServidorDTO> servidores = convServidorDTO.getLIO(cluster.getServidores());
            List<MaquinaDTO> maquinas = cluster.getServidores().stream()
                    .flatMap(s -> s.getMaquinas().stream()) // Aplana los streams de máquinas de cada servidor
                    .map(m -> convMaquinaDTO.getIO(m)) // Convierte cada máquina en un MaquinaDTO
                    .collect(Collectors.toList()); // Recolecta los resultados en una lista

            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            model.addAttribute("cluster", convClusterDTO.getIO(cluster));
            model.addAttribute("servidores", servidores);
            model.addAttribute("maquinas", maquinas);
            return "info_cluster";
        } return "redirect:/rackmaster/clusters/a1";
    }

    @GetMapping("/a2/crear-cluster")
    public String crearClusterUI(Model model) {
        model.addAttribute("datos_duplicados", datos_duplicados);
        model.addAttribute("cluster_aux", clusterAux);
        model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
        clusterAux = new ClusterDTO();
        datos_duplicados = new ArrayList<>();
        return "admin/clusters/crear_cluster";
    }

    @PostMapping("/a2/guardar-cluster")
    public String guardarCluster(ClusterDTO clusterDTO,
                                 @RequestParam("servidores_ids") String servidoresIds) {
        verificarDatosDuplicados(clusterDTO);
        if(datos_duplicados.isEmpty() && verificarDatosCorrectos(clusterDTO)) {

            // verificamos si proyecto existe, sino asignamos valor por defecto
            clusterDTO.setProyecto(clusterDTO.getProyecto().isEmpty() ? "NO DEFINIDO" : clusterDTO.getProyecto());

            // creamos entidad para enviar a db
            EntidadCluster entidadCluster = convClusterDTO.getOI(clusterDTO);

            if(entidadCluster != null) {
                entidadCluster = servCluster.crearCluster(entidadCluster, servidoresIds);
                clusterAux = new ClusterDTO();
                return "redirect:/rackmaster/clusters/a1/" + entidadCluster.getCluster_id();
            } else {
                LOGGER.info("¡No se pudo crear el cluster {} !", clusterDTO);
                return "redirect:/rackmaster/clusters/a1";
            }

        }

        clusterAux = clusterDTO;
        return "redirect:/rackmaster/clusters/a2/crear-cluster";
    }

    @GetMapping("/a2/editar-cluster/{cluster_id}")
    public String editarClusterUI(@PathVariable Integer cluster_id, Model model) {
        EntidadCluster cluster = servCluster.buscarCluster(cluster_id);

        if(cluster != null) {

            ClusterDTO clusterDTO = convClusterDTO.getIO(cluster);
            StringBuilder sb = new StringBuilder();

            for (EntidadServidor servidor : cluster.getServidores()) {
                sb.append(servidor.getServidor_id()).append(", ");
            }
            if (!sb.isEmpty()) {
                sb.setLength(sb.length() - 2); // Elimina la última coma y espacio
            }

            clusterDTO.setServidoresdb_ids(sb.toString());
            LOGGER.info("lista ids servidores {}", clusterDTO.getServidoresdb_ids());

            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            model.addAttribute("cluster_aux", clusterDTO);
            model.addAttribute("datos_duplicados", datos_duplicados);
            return "admin/clusters/editar_cluster";

        } return "redirect:/rackmaster/clusters/a1";
    }

    @PostMapping("/a2/modificar-cluster")
    public String modificarClusters(ClusterDTO clusterDTO,
                                    @RequestParam("servidores_ids") String servidoresIds,
                                    Model model) {
        LOGGER.info("Cluster a modificar {}", clusterDTO);
        LOGGER.info("Ids servidores a asociar {}", servidoresIds);

        verificarDatosDuplicados(clusterDTO);
        if(datos_duplicados.isEmpty() && verificarDatosCorrectos(clusterDTO)) {

            // verificamos si proyecto existe, sino asignamos valor por defecto
            clusterDTO.setProyecto(clusterDTO.getProyecto().isEmpty() ? "NO DEFINIDO" : clusterDTO.getProyecto());

            // creamos entidad para enviar a db
            EntidadCluster entidadCluster = convClusterDTO.getOI(clusterDTO);

            if(entidadCluster != null) {
                entidadCluster = servCluster.modificarCluster(entidadCluster, servidoresIds);
                clusterAux = new ClusterDTO();
                return "redirect:/rackmaster/clusters/a1/" + entidadCluster.getCluster_id();
            } else {
                LOGGER.info("¡No se pudo modificar el cluster {} !", clusterDTO);
                return "redirect:/rackmaster/clusters/a1";
            }

        } else {
            EntidadCluster cluster = servCluster.buscarCluster(clusterDTO.getCluster_id());
            if(cluster != null) {
                ClusterDTO clusterDTORUI = convClusterDTO.getIO(cluster);
                clusterDTORUI.setServidoresdb_ids(servidoresIds);
                model.addAttribute("cluster_aux", clusterDTORUI);
                model.addAttribute("cluster_aux2", clusterDTO);
                model.addAttribute("datos_duplicados", datos_duplicados);
                model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
                return "admin/clusters/editar_cluster";
            }
        }
        return "redirect:/rackmaster/clusters/a1";
    }

    @GetMapping("/a2/eliminar-cluster/{cluster_id}")
    public String eliminarCluster(@PathVariable Integer cluster_id) {
        EntidadCluster cluster = servCluster.buscarCluster(cluster_id);

        if(cluster != null) {
            cluster.getServidores().forEach(s -> {
                s.setCluster(null);
                servServidor.modificarServidor(s);
            });

            // eliminamos cluster en la db
            servCluster.eliminarCluster(cluster_id);

        } return "redirect:/rackmaster/clusters/a1";
    }

    private boolean verificarDatosCorrectos(ClusterDTO clusterDTO) {
        return (appUtil.esTexto160Largo(clusterDTO.getNombre())
                && appUtil.esTexto160Largo(clusterDTO.getSede()));
    }

    private void verificarDatosDuplicados(ClusterDTO clusterDTO) {
        List<EntidadCluster> clusters = servCluster.buscarTodosClusters();
        List<EntidadServidor> servidores = servServidor.buscarTodosServidores();
        List<EntidadMaquina> maquinas = servMaquina.buscarTodasMaquinas();
        datos_duplicados = Collections.synchronizedList(new ArrayList<>());

        if(clusterDTO.getCluster_id() != null) {
            clusters.removeIf(c -> c.getCluster_id().equals(clusterDTO.getCluster_id()));
        }

        clusters.parallelStream().forEach(cluster -> {
            if(cluster.getNombre().equals(clusterDTO.getNombre().trim())) {
                datos_duplicados.add("NOMBRE_CLUSTER");
            }
        });

        servidores.parallelStream().forEach(servidor -> {
            if(servidor.getNombre().equals(clusterDTO.getNombre().trim())) {
                datos_duplicados.add("NOMBRE_SERVIDOR");
            }
        });

        maquinas.parallelStream().forEach(maquina -> {
            if(maquina.getNombre().equals(clusterDTO.getNombre().trim())) {
                datos_duplicados.add("NOMBRE_MAQUINA");
            }
        });

    }

}
