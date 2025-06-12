package com.ngcamargob.rackmaster.presentacion.controladora;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCluster;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCredencial;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadMaquina;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadServidor;
import com.ngcamargob.rackmaster.presentacion.dto.CredencialDTO;
import com.ngcamargob.rackmaster.presentacion.dto.MaquinaDTO;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/rackmaster/maquinas-virtuales")
public class MaquinasControl {

    @Autowired
    private IServMaquina servMaquina;

    @Autowired
    private IServServidor servServidor;

    @Autowired
    private IServCluster servCluster;

    @Autowired
    private IServCredencial servCredencial;

    @Autowired
    private ConvMaquinaDTO convMaquinaDTO;

    @Autowired
    private ConvServidorDTO convServidorDTO;

    @Autowired
    private ConvCredencialDTO convCredencialDTO;

    @Autowired
    private AppUtil appUtil;

    private List<EntidadMaquina> maquinasPaginables = new ArrayList<>();
    private Integer totalPages = null;
    private String categoriaux = "";
    private String fraseaux = "";

    private static Integer total_maquinas = null;
    private MaquinaDTO maquinaAux = new MaquinaDTO();
    private List<String> datos_duplicados = new ArrayList<>();
    private final static Logger LOGGER = LoggerFactory.getLogger(MaquinasControl.class);

    private List<EntidadMaquina> obtenerPagina(List<EntidadMaquina> listaCompleta, int page, int pageSize) {
        int start = page * pageSize;

        // Manejo de errores para evitar IndexOutOfBoundsException
        if (start >= listaCompleta.size()) {
            return Collections.emptyList(); // Devuelve una lista vacía si la página está fuera de rango
        }

        int end = Math.min(start + pageSize, listaCompleta.size());
        return listaCompleta.subList(start, end);
    }

    @GetMapping("/a1")
    public String inicioUI(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 10;
        Page<EntidadMaquina> maquinasPage = servMaquina.buscarMaquinasPaginadas(page, pageSize);

        if(total_maquinas == null) {
            total_maquinas = servMaquina.buscarTodasMaquinas().size();
        }

        model.addAttribute("maquinas", convMaquinaDTO.getLIO(maquinasPage.getContent()));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", maquinasPage.getTotalPages());
        model.addAttribute("total_maquinas", total_maquinas);
        model.addAttribute("isPageable", false);
        model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
        return "maquinas";
    }

    @GetMapping("/a1/buscar")
    public String buscarMaquinasUI(@RequestParam(required = false) String frase,
                                   @RequestParam String categoria,
                                   @RequestParam(defaultValue = "0") int page,
                                   Model model) {
        int pageSize = 10;
        if(categoria.equals("@")) {
            maquinasPaginables = servMaquina.buscarPorFrase(fraseaux, categoriaux);
        } else {
            maquinasPaginables = servMaquina.buscarPorFrase(frase, categoria);
            totalPages = (int) Math.ceil((double) maquinasPaginables.size() / pageSize);
            categoriaux = categoria;
            if (frase != null) fraseaux = frase;
        }

        if(total_maquinas == null) total_maquinas = servMaquina.buscarTodasMaquinas().size();
        if(maquinasPaginables != null) {
            LOGGER.info("INDICE PAGINA {}", page);
            model.addAttribute("maquinas", convMaquinaDTO.getLIO(obtenerPagina(maquinasPaginables, page, pageSize)));
            model.addAttribute("maquinas_filtro", maquinasPaginables.size());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("total_maquinas", total_maquinas);
            model.addAttribute("isPageable", true);
            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            return "maquinas";
        } return "redirect:/rackmaster/maquinas-virtuales/a1";
    }

    @GetMapping("/a1/{maquina_id}")
    public String infoMaquinaUI(@PathVariable Integer maquina_id,
                                Model model) {
        EntidadMaquina maquina = servMaquina.buscarMaquinaConCredencialesDecryp(maquina_id);
        if(maquina != null) {
            MaquinaDTO maquinaDTO = convMaquinaDTO.getIO(maquina);
            maquinaDTO.setServidor(convServidorDTO.getIO(maquina.getServidor()));
            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            model.addAttribute("maquina", maquinaDTO);
            model.addAttribute("credenciales", convCredencialDTO.getLIO(maquina.getCredenciales()));
            return "info_maquina";
        } return "redirect:/rackmaster/maquinas-virtuales/a1";
    }

    @GetMapping("/a2/crear-maquina")
    public String crearMaquinaUI(Model model){
        model.addAttribute("datos_duplicados", datos_duplicados);
        model.addAttribute("maquina_aux", maquinaAux);
        model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
        maquinaAux = new MaquinaDTO();
        datos_duplicados = new ArrayList<>();
        return "admin/maquinas/crear_maquina";
    }

    @GetMapping("/a2/crear-maquina/{maquina_id}")
    public String crearMaquinaUI(@PathVariable Integer maquina_id, Model model){
        EntidadMaquina maquina = servMaquina.buscarMaquina(maquina_id);
        if(maquina != null) {

            // pasamos datos únicamente necesarios
            MaquinaDTO maquinaDTO = MaquinaDTO.builder()
                    .mac("AA:BB:CC:DD:EE:FF")
                    .sistema_op(maquina.getSistema_op())
                    .proyecto(maquina.getProyecto())
                    .aplicacion(maquina.getAplicacion())
                    .servicio(maquina.getServicio())
                    .procesador_asig(maquina.getProcesador_asig())
                    .almacenamiento_asig(maquina.getAlmacenamiento_asig())
                    .almacenamiento_total(maquina.getAlmacenamiento_total())
                    .ram_asig(maquina.getRam_asig())
                    .en_uso(maquina.isEn_uso())
                    .servidor(
                            ServidorDTO.builder()
                                    .servidor_id(maquina.getServidor().getServidor_id())
                                    .nombre(maquina.getServidor().getNombre())
                                    .en_uso(maquina.getServidor().isEn_uso())
                                    .build())
                    .build();

            model.addAttribute("datos_duplicados", datos_duplicados);
            model.addAttribute("maquina_aux", maquinaDTO);
            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            maquinaAux = new MaquinaDTO();
            datos_duplicados = new ArrayList<>();
            return "admin/maquinas/crear_maquina";
        } return "redirect:/rackmaster/maquina-virtuales/a1";
    }

    @PostMapping("/a2/guardar-maquina")
    public String guardarMaquinaUI(MaquinaDTO maquinaDTO,
                                        @RequestParam(name = "hipervisor_id", required = false) String hipervisor_id,
                                        @RequestParam(name = "hipervisor_asignar", required = false) String hipervisor_asignar) {
        LOGGER.info("Máquina virtual a guardar UI {}", maquinaDTO);
        LOGGER.info("Id del hipervisor a asignar {}", hipervisor_id);
        LOGGER.info("Nombre del hipervisor a asignar {}", hipervisor_asignar);

        maquinaAux = maquinaDTO;
        verificarDuplicidadDatos(maquinaDTO);
        maquinaDTO = verificarDatosCorrectos(maquinaDTO);
        if(datos_duplicados.isEmpty() && maquinaDTO != null) {

            LOGGER.info("Máquina virtual a guardar VERIFICADA {}", maquinaDTO);

            // creamos máquina a guardar
            EntidadMaquina maquinaSave = convMaquinaDTO.getOI(maquinaDTO);

            // guardamos máquina en db
            maquinaSave = servMaquina.crearMaquina(maquinaSave, hipervisor_id, hipervisor_asignar);

            // Crear la credencial y asociarla a la máquina
            EntidadCredencial entidadCredencial = EntidadCredencial.builder()
                    .usuario(maquinaDTO.getUsuario())
                    .contrasenia(maquinaDTO.getContrasenia())
                    .puerto(maquinaDTO.getPuerto())
                    .privilegios("Administrador")
                    .tipo_conexion("Terminal sistema")
                    .uso_destinado("Administración del sistema")
                    .primaria(true)
                    .maquina(maquinaSave)
                    .build();

            // verificamos que datos de credencial sean correctos
            if(appUtil.verificarDatosCorrectosCredencial(entidadCredencial)) {
                // Guardar la credencial en la base de datos
                servCredencial.crearCredencial(entidadCredencial);
            }

            if(maquinaSave != null) {
                total_maquinas = null;
                maquinaAux = new MaquinaDTO();
                return "redirect:/rackmaster/maquinas-virtuales/a1/" +  maquinaSave.getMaquina_id();
            }

        } return "redirect:/rackmaster/maquinas-virtuales/a2/crear-maquina";
    }

    @GetMapping("/a2/editar-maquina/{maquina_id}")
    public String editarMaquinaUI(@PathVariable Integer maquina_id,
                                  Model model) {
        EntidadMaquina maquina = servMaquina.buscarMaquina(maquina_id);
        if(maquina != null) {
            model.addAttribute("datos_duplicados", datos_duplicados);
            model.addAttribute("maquina_aux", convMaquinaDTO.getIO(maquina));
            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            datos_duplicados = new ArrayList<>();
            return "admin/maquinas/editar_maquina";
        } return "redirect:/rackmaster/maquinas-virtuales/a1";
    }

    @PostMapping("/a2/modificar-maquina")
    public String modificarMaquina(MaquinaDTO maquinaDTO,
                                   @RequestParam(name = "hipervisor_id", required = false) String hipervisor_id,
                                   @RequestParam(name = "hipervisor_asignar", required = false) String hipervisor_asignar,
                                   Model model) {
        LOGGER.info("Maquina a modificar UI {}", maquinaDTO);
        LOGGER.info("Id del hipervisor a asignar {}", hipervisor_id);
        LOGGER.info("Nombre del hipervisor a asignar {}", hipervisor_asignar);

        maquinaAux = maquinaDTO;
        verificarDuplicidadDatos(maquinaDTO);
        maquinaDTO = verificarDatosCorrectos(maquinaDTO);

        if(datos_duplicados.isEmpty() && maquinaDTO != null) {

            LOGGER.info("Maquina a modificar VERIFICADA {}", maquinaDTO);

            // creamos máquina a modificar
            EntidadMaquina maquinaSave = convMaquinaDTO.getOI(maquinaDTO);

            if(maquinaSave != null) {
                servMaquina.modificarMaquina(maquinaSave, hipervisor_id, hipervisor_asignar);
                maquinaAux = new MaquinaDTO();
                return "redirect:/rackmaster/maquinas-virtuales/a1/" +  maquinaSave.getMaquina_id();
            }
        } else {
            EntidadMaquina maquina = servMaquina.buscarMaquina(maquinaAux.getMaquina_id());
            if(maquina != null) {
                model.addAttribute("maquina_aux", convMaquinaDTO.getIO(maquina));
                model.addAttribute("maquina_aux2", maquinaAux);
                model.addAttribute("datos_duplicados", datos_duplicados);
                model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
                return "admin/maquinas/editar_maquina";
            }
        } return "redirect:/rackmaster/maquinas-virtuales/a1";
    }

    @PostMapping("/a2/registrar-credencial")
    public String modificarCredencial(CredencialDTO credencialDTO) {

        LOGGER.info("Credencial a crear / modificar {}", credencialDTO);
        LOGGER.info("ID Máquina a crear / modificar credencial {}", credencialDTO.getId_equipo());

        // creamos máquina a guardar
        EntidadCredencial entidadCredencial = convCredencialDTO.getOI(credencialDTO);
        servMaquina.guardarCredencial(entidadCredencial, credencialDTO.getId_equipo());
        return "redirect:/rackmaster/maquinas-virtuales/a1/" + credencialDTO.getId_equipo();

    }

    @GetMapping("/a2/eliminar-credencial/{credencial_id}")
    public String eliminarCredencial(@PathVariable Integer credencial_id) {
        Integer maquina_id = servMaquina.eliminarCredencial(credencial_id);
        if(maquina_id != null) {
            return "redirect:/rackmaster/maquinas-virtuales/a1/" + maquina_id;
        } return "redirect:/rackmaster/maquinas-virtuales/a1";
    }

    @GetMapping("/a2/eliminar-maquina/{maquina_id}")
    public String eliminarMaquina(@PathVariable Integer maquina_id) {
        servMaquina.eliminarMaquina(maquina_id);
        total_maquinas = null;
        return "redirect:/rackmaster/maquinas-virtuales/a1";
    }

    public MaquinaDTO verificarDatosCorrectos(MaquinaDTO maquinaDTO) {
        if(appUtil.esTexto160Largo(maquinaDTO.getNombre())
                && appUtil.esTexto160Largo(maquinaDTO.getNombre_en_hipervisor())
                && (maquinaDTO.getId_en_hipervisor() > 0 && maquinaDTO.getId_en_hipervisor() < 1000000000)
                && appUtil.esTexto160Largo(maquinaDTO.getIp())) {

            if(maquinaDTO.getMac().isEmpty()) { maquinaDTO.setMac("NO DEFINIDO"); }
            if(maquinaDTO.getSistema_op().isEmpty()) { maquinaDTO.setSistema_op("NO DEFINIDO"); }
            if(maquinaDTO.getServicio().isEmpty()) { maquinaDTO.setServicio("NO DEFINIDO"); }
            if(maquinaDTO.getProyecto().isEmpty()) { maquinaDTO.setProyecto("NO DEFINIDO"); }
            if(maquinaDTO.getAplicacion().isEmpty()) { maquinaDTO.setAplicacion("NO DEFINIDO"); }
            if(maquinaDTO.getProcesador_asig().isEmpty()) { maquinaDTO.setProcesador_asig("NO DEFINIDO"); }
            if(maquinaDTO.getAlmacenamiento_asig().isEmpty()) { maquinaDTO.setAlmacenamiento_asig("NO DEFINIDO"); }
            if(maquinaDTO.getAlmacenamiento_total() == null) { maquinaDTO.setAlmacenamiento_total(0); }
            if(maquinaDTO.getRam_asig() == null) { maquinaDTO.setRam_asig(0); }
            return maquinaDTO;
        } return null;
    }

    private void verificarDuplicidadDatos(MaquinaDTO maquinaDTO) {
        List<EntidadMaquina> maquinas = servMaquina.buscarTodasMaquinas();
        List<EntidadServidor> servidores = servServidor.buscarTodosServidores();
        List<EntidadCluster> clusters = servCluster.buscarTodosClusters();
        datos_duplicados = Collections.synchronizedList(new ArrayList<>()); // List concurrente para evitar problemas de acceso simultáneo

        // Verificar si el ID es nulo solo una vez
        Integer maquinaIdActual = (maquinaDTO.getMaquina_id() != null) ? maquinaDTO.getMaquina_id() : -1;

        // Comparar si datos de máquina existen para otra máquina en paralelo
        maquinas.parallelStream().forEach(maquina -> {
            // Ignorar la comparación consigo mismo para la máquina en modificación
            if (!maquinaIdActual.equals(maquina.getMaquina_id())) {

                // Verificación de otros atributos
                if (maquina.getNombre().equalsIgnoreCase(maquinaDTO.getNombre())) {
                    datos_duplicados.add("NOMBRE_MAQUINA");
                }
                if (maquina.getNombre_en_hipervisor().equalsIgnoreCase(maquinaDTO.getNombre_en_hipervisor())) {
                    datos_duplicados.add("NOMBRE_EN_HIPERVISOR");
                }
                if (maquina.getId_en_hipervisor().equals(maquinaDTO.getId_en_hipervisor())) {
                    datos_duplicados.add("ID_EN_HIPERVISOR");
                }
                if (maquina.getIp().equalsIgnoreCase(maquinaDTO.getIp())) {
                    datos_duplicados.add("IP_MAQUINA");
                }
                if (!maquina.getMac().equals("NO DEFINIDO")
                        && maquina.getMac().equalsIgnoreCase(maquinaDTO.getMac())) {
                    datos_duplicados.add("MAC_MAQUINA");
                }
            }
        });

        // Comparar si datos de servidor existen en paralelo
        servidores.parallelStream().forEach(servidor -> {
            // Verificación de nombre de servidor
            if (servidor.getNombre().equals(maquinaDTO.getNombre())) {
                datos_duplicados.add("NOMBRE_SERVIDOR");
            }

            // Verificación de IP de servidor
            if (servidor.getIp().equals(maquinaDTO.getIp())) {
                datos_duplicados.add("IP_SERVIDOR");
            }

            // Verificación de MAC de servidor
            if (!servidor.getMac().equals("NO DEFINIDO")
                    && servidor.getMac().equals(maquinaDTO.getMac())) {
                datos_duplicados.add("MAC_SERVIDOR");
            }
        });

        // Comparar si datos de clsuter existen en paralelo
        clusters.parallelStream().forEach(cluster -> {
            // Verificación de nombre de cluster
            if(cluster.getNombre().equals(maquinaDTO.getNombre())) {
                datos_duplicados.add("NOMBRE_CLUSTER");
            }
        });

    }



}
