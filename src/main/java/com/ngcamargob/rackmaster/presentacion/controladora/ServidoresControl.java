package com.ngcamargob.rackmaster.presentacion.controladora;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCluster;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadMaquina;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import com.ngcamargob.rackmaster.presentacion.dto.ClusterDTO;
import com.ngcamargob.rackmaster.presentacion.dto.CredencialDTO;
import com.ngcamargob.rackmaster.presentacion.dto.ServidorDTO;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCluster;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCredencial;
import com.ngcamargob.rackmaster.servicio.interfaces.IServMaquina;
import com.ngcamargob.rackmaster.servicio.interfaces.IServServidor;
import com.ngcamargob.rackmaster.utilidades.AppUtil;
import com.ngcamargob.rackmaster.utilidades.mapper.ConvCredencialDTO;
import com.ngcamargob.rackmaster.utilidades.mapper.ConvMaquinaDTO;
import com.ngcamargob.rackmaster.utilidades.mapper.ConvServidorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/rackmaster/servidores")
public class ServidoresControl {

    @Autowired
    private IServServidor servServidor;

    @Autowired
    private IServMaquina servMaquina;

    @Autowired
    private IServCluster servCluster;

    @Autowired
    private IServCredencial servCredencial;

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private ConvServidorDTO convServidorDTO;

    @Autowired
    private ConvMaquinaDTO convMaquinaDTO;

    @Autowired
    private ConvCredencialDTO convCredencialDTO;

    private List<EntidadServidor> servidoresPaginables = new ArrayList<>();
    private Integer totalPages = null;
    private String categoriaux = "";
    private String fraseaux = "";

    private Integer total_servidores = null;
    private ServidorDTO servidorAux = new ServidorDTO();
    private List<String> datos_duplicados = new ArrayList<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(ServidoresControl.class);



    @GetMapping("/a2/devolver-servidores")
    @ResponseBody // resultado debe ser tratado como JSON
    public List<ServidorDTO> getServidores() {
        List<EntidadServidor> servidores = servServidor.buscarTodosServidores();
        if(servidores != null && !servidores.isEmpty()) {
            return convServidorDTO.getLIO(servidores);
        } return new ArrayList<>();
    }

    private List<EntidadServidor> obtenerPagina(List<EntidadServidor> listaCompleta, int page, int pageSize) {
        int start = page * pageSize;
        int end = Math.min(start + pageSize, listaCompleta.size());
        return listaCompleta.size() > pageSize ? listaCompleta.subList(start, end) : listaCompleta;
    }

    @GetMapping("/a1")
    public String servidoresUI(Model model, @RequestParam(defaultValue = "0") int page) {
        int pageSize = 10;
        Page<EntidadServidor> servidoresPage = servServidor.buscarServidoresPaginados(page, pageSize);

        if(total_servidores == null) {
            total_servidores = servServidor.buscarTodosServidores().size();
        }

        model.addAttribute("servidores", convServidorDTO.getLIO(servidoresPage.getContent()));
        model.addAttribute("total_servidores", total_servidores);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", servidoresPage.getTotalPages());
        model.addAttribute("isPageable", false);
        model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
        return "servidores";
    }

    @GetMapping("/a1/buscar")
    public String buscarServidor(@RequestParam(name = "frase", required = false) String frase,
                                 @RequestParam("categoria") String categoria,
                                 @RequestParam(defaultValue = "0") int page,
                                 Model model) {
        int pageSize = 10;
        if(categoria.equals("@")) {
            servidoresPaginables = servServidor.buscarPorFrase(fraseaux, categoriaux);
        } else {
            servidoresPaginables = servServidor.buscarPorFrase(frase, categoria);
            totalPages = (int) Math.ceil((double) servidoresPaginables.size() / pageSize);
            categoriaux = categoria;
            if (frase != null) fraseaux = frase;
        }

        if(total_servidores == null) total_servidores = servServidor.buscarTodosServidores().size();
        if(servidoresPaginables != null) {
            model.addAttribute("servidores", convServidorDTO.getLIO(obtenerPagina(servidoresPaginables, page, pageSize)));
            model.addAttribute("total_servidores", total_servidores);
            model.addAttribute("servidores_filtro", servidoresPaginables.size());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("isPageable", true);
            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            return "servidores";
        } return "redirect:/rackmaster/servidores/a1";
    }

    @GetMapping("/a1/{servidor_id}")
    public String infoServidorUI(
            @PathVariable Integer servidor_id,
            Model model) {
        EntidadServidor servidor = servServidor.buscarServidorConCredencialesDecryp(servidor_id);
        if(servidor != null) {

            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            model.addAttribute("servidor", convServidorDTO.getIO(servidor));
            model.addAttribute("maquinas", convMaquinaDTO.getLIO(servidor.getMaquinas()));
            model.addAttribute("credenciales", convCredencialDTO.getLIO(servidor.getCredenciales()));
            return "info_servidor";

        } return "redirect:/rackmaster/servidores/a1";
    }

    @GetMapping("/a2/crear-servidor")
    public String crearServidorUI(Model model) {
        model.addAttribute("datos_duplicados", datos_duplicados);
        model.addAttribute("servidor_aux", servidorAux);
        model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
        servidorAux = new ServidorDTO();
        datos_duplicados = new ArrayList<>();
        return "admin/servidores/crear_servidor";
    }

    @GetMapping("/a2/crear-servidor/{servidor_id}")
    public String crearServidorPlantillaUI(@PathVariable Integer servidor_id, Model model) {
        EntidadServidor servidor = servServidor.buscarServidor(servidor_id);
        if(servidor != null) {

            // pasamos datos únicamente necesarios
            ServidorDTO servidorDTO = ServidorDTO.builder()
                    .mac("AA:BB:CC:DD:EE:FF")
                    .sistema_op(servidor.getSistema_op())
                    .modelo(servidor.getModelo())
                    .procesador(servidor.getProcesador())
                    .discos(servidor.getDiscos())
                    .almacenamiento_total(servidor.getAlmacenamiento_total())
                    .ram(servidor.getRam())
                    .rack(servidor.getRack())
                    .sede(servidor.getSede())
                    .en_uso(servidor.isEn_uso())
                    .build();
            if(servidor.getCluster() != null) {
                servidorDTO.setCluster(ClusterDTO.builder()
                        .cluster_id(servidor.getCluster().getCluster_id())
                        .nombre(servidor.getCluster().getNombre())
                        .build());
            }

            model.addAttribute("datos_duplicados", datos_duplicados);
            model.addAttribute("servidor_aux", servidorDTO);
            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            servidorAux = new ServidorDTO();
            datos_duplicados = new ArrayList<>();
            return "admin/servidores/crear_servidor";
        } return "redirect:/rackmaster/servidores/a1";
    }

    @PostMapping("/a2/guardar-servidor")
    public String guardarServidor(ServidorDTO servidorDTO,
                                  @RequestParam(name = "cluster_id", required = false) String cluster_id,
                                  @RequestParam(name = "cluster_asignar",  required = false) String cluster_asignar) {

        LOGGER.info("servidor a guardar {}", servidorDTO);
        LOGGER.info("Id cluster a asignar {}", cluster_id);
        LOGGER.info("Nombre cluster a asignar {}", cluster_asignar);

        servidorAux = servidorDTO;
        verificarDuplicidadDatos(servidorDTO);
        servidorDTO = verificarDatosCorrectos(servidorDTO);
        if(datos_duplicados.isEmpty() && servidorDTO != null) {

            // creamos servidor a guardar
            EntidadServidor servidorSave = convServidorDTO.getOI(servidorDTO);

            // buscamos cluster asociar
            try {
                if (!cluster_id.isEmpty() && !cluster_asignar.isEmpty()) {
                    EntidadCluster clusterAsociar = servCluster.buscarCluster(Integer.parseInt(cluster_id));
                    if(clusterAsociar != null) {
                        servidorSave.setCluster(clusterAsociar);
                    }
                }
            } catch (Exception e) {
                LOGGER.info("¡ID incorrecta de cluster a asociar {} !", e.getMessage());
            }

            // guardamos servidor en db
            servidorSave = servServidor.crearServidor(servidorSave, cluster_id, cluster_asignar);

            if(servidorSave != null) {
                // creamos credencial y asociamos a servidor
                EntidadCredencial entidadCredencial = EntidadCredencial.builder()
                        .usuario(servidorDTO.getUsuario())
                        .contrasenia(servidorDTO.getContrasenia())
                        .puerto(servidorDTO.getPuerto())
                        .privilegios("Administrador")
                        .tipo_conexion("Terminal sistema")
                        .uso_destinado("Administración del sistema")
                        .primaria(true)
                        .servidor(servidorSave)
                        .build();

                // verificamos si datos de la credencial son correctos
                if(appUtil.verificarDatosCorrectosCredencial(entidadCredencial)) {
                    // Guardar la credencial en la base de datos
                    servCredencial.crearCredencial(entidadCredencial);
                }

                total_servidores = null;
                servidorAux = new ServidorDTO();
                return "redirect:/rackmaster/servidores/a1/" + servidorSave.getServidor_id();
            }

        } return "redirect:/rackmaster/servidores/a2/crear-servidor";
    }

    @GetMapping("/a2/editar-servidor/{servidor_id}")
    public String editarServidorUI(@PathVariable Integer servidor_id,
                                   Model model) {
        EntidadServidor servidor = servServidor.buscarServidor(servidor_id);
        if(servidor != null) {
            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            model.addAttribute("datos_duplicados", datos_duplicados);
            model.addAttribute("servidor_aux", convServidorDTO.getIO(servidor));
            return "admin/servidores/editar_servidor";
        } return "redirect:/rackmaster/servidores/a1";
    }

    @PostMapping("/a2/modificar-servidor")
    public String modificarServidorDB(ServidorDTO servidorDTO,
                                      @RequestParam(name = "cluster_id", required = false) String cluster_id,
                                      @RequestParam(name = "cluster_asignar",  required = false) String cluster_asignar,
                                      Model model) {
        LOGGER.info("Servidor a modificar: {}", servidorDTO);
        LOGGER.info("Id cluster a asignar {}", cluster_id);
        LOGGER.info("Nombre clsuter a asignar {}", cluster_asignar);

        servidorAux = servidorDTO;
        verificarDuplicidadDatos(servidorDTO);
        servidorDTO = verificarDatosCorrectos(servidorDTO);
        if (datos_duplicados.isEmpty() && servidorDTO != null) {

            // creamos servidor a modificar
            EntidadServidor servidorSave = convServidorDTO.getOI(servidorDTO);

            // buscamos cluster asociar
            try {
                if (!cluster_id.isEmpty() && !cluster_asignar.isEmpty()) {
                    EntidadCluster clusterAsociar = servCluster.buscarCluster(Integer.parseInt(cluster_id));
                    if(clusterAsociar != null) {
                        servidorSave.setCluster(clusterAsociar);
                    }
                }
            } catch (Exception e) {
                LOGGER.info("¡ID incorrecta de cluster a asociar {} !", e.getMessage());
            }

            // guardamos servidor a modificar en db
            servidorSave = servServidor.modificarServidor(servidorSave, cluster_id, cluster_asignar);

            if(servidorSave != null) {
                servidorAux = new ServidorDTO();
                return "redirect:/rackmaster/servidores/a1/" + servidorSave.getServidor_id();
            }

        } else {
            EntidadServidor servidor = servServidor.buscarServidor(servidorAux.getServidor_id());
            if(servidor != null) {
                model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
                model.addAttribute("datos_duplicados", datos_duplicados);
                model.addAttribute("servidor_aux", convServidorDTO.getIO(servidor));
                model.addAttribute("servidor_aux2", servidorAux);
                return "admin/servidores/a2/editar_servidor";
            }
        } return "redirect:/rackmaster/servidores/a1";
    }

    @PostMapping("/a2/registrar-credencial")
    public String modificarCredencial(CredencialDTO credencialDTO) {

        LOGGER.info("Credencial a crear / modificar {}", credencialDTO);
        LOGGER.info("ID Servidor a crear / modificar credencial {}", credencialDTO.getId_equipo());

        // creamos la entidad a guardar en la db
        EntidadCredencial credencial = convCredencialDTO.getOI(credencialDTO);

        // verificamos que datos de credencial sean correctos
        if(appUtil.verificarDatosCorrectosCredencial(credencial)) {

            // guardamos credencial en la db
            servServidor.guardarCredencial(credencial, credencialDTO.getId_equipo());
            return "redirect:/rackmaster/servidores/a1/" + credencialDTO.getId_equipo();

        } return "redirect:/rackmaster/servidores/a1";
    }

    @GetMapping("/a2/eliminar-credencial/{credencial_id}")
    public String eliminarCredencial(@PathVariable Integer credencial_id) {
        Integer servidor_id = servServidor.eliminarCredencial(credencial_id);
        if(servidor_id != null) {
            return "redirect:/rackmaster/servidores/a1/" + servidor_id;
        } return "redirect:/rackmaster/servidores/a1";
    }

    @GetMapping("/a2/eliminar-servidor/{servidor_id}")
    public String eliminarServidor(@PathVariable Integer servidor_id) {
        LOGGER.info("Id de servidor a eliminar {}", servidor_id);

        EntidadServidor servidor = servServidor.buscarServidor(servidor_id);
        if(servidor != null) {

            // removemos máquinas virtuales asociadas
            List<EntidadMaquina> maquinas = servidor.getMaquinas();
            for(EntidadMaquina maquina: maquinas) {

                // buscamos credenciales
                List<EntidadCredencial> credenciales = servCredencial.traerCredencialesMaquina(maquina);
                if(!credenciales.isEmpty()) {
                    // removemos asociación credencial y guardamos en db
                    credenciales.forEach(c -> servCredencial.eliminarCredencial(c.getCredencial_id()));
                }

                // eliminamos máquina db
                servMaquina.eliminarMaquina(maquina.getMaquina_id());
            }

            // removemos asociación credencial y guardamos en db
            List<EntidadCredencial> credenciales = servidor.getCredenciales();
            if(!credenciales.isEmpty()) {
                credenciales.forEach(c -> servCredencial.eliminarCredencial(c.getCredencial_id()));
            }

            // eliminamos servidor db
            servServidor.eliminarServidor(servidor_id);
            total_servidores = null;
        } return "redirect:/rackmaster/servidores/a1";
    }

    private ServidorDTO verificarDatosCorrectos(ServidorDTO servidorDTO) {

        if(appUtil.esTexto160Largo(servidorDTO.getNombre())
                && appUtil.esTexto160Largo(servidorDTO.getIp())
                && appUtil.esTexto160Largo(servidorDTO.getSede())) {
            if(servidorDTO.getMac().isEmpty()) { servidorDTO.setMac("NO DEFINIDO"); }
            if(servidorDTO.getSistema_op().isEmpty()) { servidorDTO.setSistema_op("NO DEFINIDO"); }
            if(servidorDTO.getModelo().isEmpty()) { servidorDTO.setModelo("NO DEFINIDO"); }
            if(servidorDTO.getProcesador().isEmpty()) { servidorDTO.setProcesador("NO DEFINIDO"); }
            if(servidorDTO.getDiscos().isEmpty()) { servidorDTO.setDiscos("NO DEFINIDO"); }
            if(servidorDTO.getAlmacenamiento_total() == null) { servidorDTO.setAlmacenamiento_total(0); }
            if(servidorDTO.getRam() == null) { servidorDTO.setRam(0); }
            if(servidorDTO.getSerial().isEmpty()) { servidorDTO.setSerial("NO DEFINIDO"); }
            if(servidorDTO.getPlaca() == null) { servidorDTO.setPlaca(0); }
            if(servidorDTO.getRack().isEmpty()) { servidorDTO.setRack("NO DEFINIDO");
                if(servidorDTO.getUnidad() < 0 || servidorDTO.getUnidad() > 43) { servidorDTO.setUnidad((byte) 0); }
            } return servidorDTO;
        } return null;
    }

    private void verificarDuplicidadDatos(ServidorDTO servidorDTO) {
        List<EntidadServidor> servidores = servServidor.buscarTodosServidores();
        List<EntidadMaquina> maquinas = servMaquina.buscarTodasMaquinas();
        List<EntidadCluster> clusters = servCluster.buscarTodosClusters();
        datos_duplicados = Collections.synchronizedList(new ArrayList<>());

        if(servidorDTO.getServidor_id() != null) {
            servidores.removeIf(servidor -> servidor.getServidor_id().equals(servidorDTO.getServidor_id())); }

        servidores.parallelStream().forEach(servidor ->  {
            if(servidor.getNombre().equals(servidorDTO.getNombre())) {
                datos_duplicados.add("NOMBRE_SERVIDOR"); }

            if(servidor.getIp().equals(servidorDTO.getIp())) {
                datos_duplicados.add("IP_SERVIDOR"); }

            if(!servidor.getMac().equals("NO DEFINIDO")
                    && servidor.getMac().equals(servidorDTO.getMac())) {
                datos_duplicados.add("MAC_SERVIDOR"); }

            if(!servidor.getSerial().equals("NO DEFINIDO")
                    && servidor.getSerial().equals(servidorDTO.getSerial())) {
                datos_duplicados.add("SERIAL"); }

            if(!servidor.getPlaca().equals(0) && servidor.getPlaca().equals(servidorDTO.getPlaca())) {
                datos_duplicados.add("PLACA"); }

            if(!servidor.getRack().equals("NO DEFINIDO") &&
                    servidor.getRack().equalsIgnoreCase(servidorDTO.getRack())
                        && servidor.getUnidad() == servidorDTO.getUnidad()) {
                datos_duplicados.add("RACK_UNIDAD"); }
        });

        maquinas.parallelStream().forEach( maquina -> {
            if(maquina.getNombre().equals(servidorDTO.getNombre())) {
                datos_duplicados.add("NOMBRE_MAQUINA"); }

            if(maquina.getIp().equals(servidorDTO.getIp())) {
                datos_duplicados.add("IP_MAQUINA"); }

            if(!maquina.getMac().equals("NO DEFINIDO") &&
                    maquina.getMac().equals(servidorDTO.getMac())) {
                datos_duplicados.add("MAC_MAQUINA"); }
        });

        // Comparar si datos de cluster existen en paralelo
        clusters.parallelStream().forEach(cluster -> {
            // Verificación de nombre de cluster
            if(cluster.getNombre().equals(servidorDTO.getNombre())) {
                datos_duplicados.add("NOMBRE_CLUSTER");
            }
        });

    }

}
