package com.ngcamargob.rackmaster.presentacion.controladora;

import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCargo;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadCuenta;
import com.ngcamargob.rackmaster.persistencia.entidades.EntidadRol;
import com.ngcamargob.rackmaster.presentacion.dto.CuentaDTO;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCargo;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCuenta;
import com.ngcamargob.rackmaster.servicio.interfaces.IServRol;
import com.ngcamargob.rackmaster.utilidades.AppUtil;
import com.ngcamargob.rackmaster.utilidades.mapper.ConvCuentaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rackmaster/cuentas")
public class CuentasControl {

    @Autowired
    private IServCuenta servCuenta;

    @Autowired
    private IServRol servRol;

    @Autowired
    private IServCargo servCargo;

    @Autowired
    private SessionRegistry sessionRegistry;

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private ConvCuentaDTO convCuentaDTO;

    private List<EntidadCuenta> cuentasPaginables = new ArrayList<>();
    private Integer totalPages = null;
    private String categoriaux = "";
    private String fraseaux = "";

    private CuentaDTO cuentaDTO = new CuentaDTO();
    private List<String> datos_duplicados = new ArrayList<>();
    private Integer total_cuentas = null;
    private final static Logger LOGGER = LoggerFactory.getLogger(CuentasControl.class);


    private List<EntidadCuenta> obtenerPagina(List<EntidadCuenta> listaCompleta, int page, int pageSize) {
        int start = page * pageSize;

        // Manejo de errores para evitar IndexOutOfBoundsException
        if (start >= listaCompleta.size()) {
            return Collections.emptyList(); // Devuelve una lista vacía si la página está fuera de rango
        }

        int end = Math.min(start + pageSize, listaCompleta.size());
        return listaCompleta.subList(start, end);
    }

    @GetMapping()
    public String cuentasUI(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 10;
        Page<EntidadCuenta> cuentasPage = servCuenta.buscarCuentasPaginadas(page, pageSize);

        if(total_cuentas == null) {
            total_cuentas = servCuenta.buscarTodasCuentas().size();
        }

        model.addAttribute("cuentas", convCuentaDTO.getLIO(cuentasPage.getContent()));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cuentasPage.getTotalPages());
        model.addAttribute("total_cuentas", total_cuentas);
        model.addAttribute("isPageable", false);
        model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
        return "admin/cuentas/cuentas";
    }

    @GetMapping("/buscar")
    public String buscarCuentasPorFrase(@RequestParam(required = false) String frase,
                                        @RequestParam String categoria,
                                        @RequestParam(defaultValue = "0") int page,
                                        Model model) {
        int pageSize = 10;
        if(categoria.equals("@")) {
            cuentasPaginables = servCuenta.buscarPorFrase(categoriaux, fraseaux);
        } else {
            cuentasPaginables = servCuenta.buscarPorFrase(categoria, frase);
            totalPages = (int) Math.ceil((double) cuentasPaginables.size() / pageSize);
            categoriaux = categoria;
            if (frase != null) fraseaux = frase;
        }

        if(total_cuentas == null) total_cuentas = servCuenta.buscarTodasCuentas().size();
        if(cuentasPaginables != null) {
            LOGGER.info("INDICE PAGINA {}", page);

            model.addAttribute("cuentas", convCuentaDTO.getLIO(obtenerPagina(cuentasPaginables, page, pageSize)));
            model.addAttribute("total_servidores", total_cuentas);
            model.addAttribute("cuentas_filtro", cuentasPaginables.size());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("total_cuentas", total_cuentas);
            model.addAttribute("isPageable", true);
            model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
            return "admin/cuentas/cuentas";
        } return "redirect:/rackmaster/cuentas";
    }

    @GetMapping("/crear-cuenta")
    public String crearCuentaUI(Model model) {
        model.addAttribute("cuenta_aux", cuentaDTO);
        model.addAttribute("datos_duplicados", datos_duplicados);
        model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
        cuentaDTO = new CuentaDTO();
        return "admin/cuentas/crear_cuenta";
    }

    @PostMapping("/guardar-cuenta")
    public String guardarCuenta(CuentaDTO cuentaDTO) {

        LOGGER.info("Cuenta a crear {}", cuentaDTO);
        EntidadCuenta entidadCuenta = convCuentaDTO.getOI(cuentaDTO);

        // Buscar y asignar el rol solo si se encuentra
        List<EntidadRol> roles = servRol.buscarTodosRoles();
        roles.stream()
                .filter(rol -> rol.getRolUsuario().toString().equals(
                        entidadCuenta.getEntidadRol().getRolUsuario().toString()))
                .findFirst().ifPresent(entidadCuenta::setEntidadRol);

        // Buscar y asignar el cargo solo si se encuentra
        List<EntidadCargo> cargos = servCargo.buscarTodosCargos();
        cargos.stream()
                .filter(cargo -> cargo.getCargoUsuario().toString().equals(
                        entidadCuenta.getEntidadCargo().getCargoUsuario().toString()))
                .findFirst().ifPresent(entidadCuenta::setEntidadCargo);

        verificarDatosDuplicados(cuentaDTO);
        if(datos_duplicados.isEmpty() && verificarDatosCorrectos(entidadCuenta)) {
            if(servCuenta.crearCuenta(entidadCuenta)) {
                this.cuentaDTO = new CuentaDTO();
                total_cuentas = null;
                return "redirect:/rackmaster/cuentas";
            } else {
                LOGGER.info("Nombre de usuario ya está en uso :: {}", cuentaDTO.getUsuario());
            }
        }
        this.cuentaDTO = cuentaDTO;
        return "redirect:/rackmaster/cuentas/crear-cuenta";
    }

    @GetMapping("/bloqueo-cuenta/{cuenta_id}/{estado}")
    public String inhabilitarCuenta(
            @PathVariable("cuenta_id") Integer cuenta_id,
            @PathVariable("estado") String estado) {
        if(estado.equals("habilitar") || estado.equals("deshabilitar")) {
            boolean estado_save = estado.equals("habilitar");
            servCuenta.estadoCuenta(cuenta_id, estado_save);
            Optional<EntidadCuenta> cuenta = servCuenta.buscarCuenta(cuenta_id);
            if(cuenta != null && cuenta.isPresent()) {
                forceLogout(cuenta.get().getUsuario());
            }
        } return "redirect:/rackmaster/cuentas";
    }

    public void forceLogout(String username) {
        sessionRegistry.getAllPrincipals().stream()
                .filter(principal -> principal instanceof UserDetails)
                .map(UserDetails.class::cast)
                .filter(user -> user.getUsername().equals(username))
                .forEach(user -> {
                    sessionRegistry.getAllSessions(user, false).forEach(session -> session.expireNow());
                });
    }

    @GetMapping("/editar-cuenta/{cuenta_id}")
    public String editarCuentaUI(
            @PathVariable("cuenta_id") Integer cuenta_id, Model model) {
        Optional<EntidadCuenta> optCuenta = servCuenta.buscarCuenta(cuenta_id);
        if(optCuenta != null && optCuenta.isPresent()) {
            optCuenta.get().setContrasenia("");
            model.addAttribute("cuenta_aux", convCuentaDTO.getIO(optCuenta.get()));
            model.addAttribute("cuenta_aux2", cuentaDTO);
            model.addAttribute("datos_duplicados", datos_duplicados);
            cuentaDTO = new CuentaDTO();
            return "admin/cuentas/editar_cuenta";
        } return "redirect:/rackmaster/cuentas";
    }

    @PostMapping("/modificar-cuenta")
    public String modificarCuenta(CuentaDTO cuentaDTO, Model model) {
        Optional<EntidadCuenta> optCuenta = servCuenta.buscarCuenta(cuentaDTO.getCuenta_id());

        LOGGER.info("Cuenta a modificar {}", cuentaDTO);

        if(optCuenta != null && optCuenta.isPresent()) {
            EntidadCuenta entidadCuenta = convCuentaDTO.getOI(cuentaDTO);

            // Buscar y asignar el rol solo si se encuentra
            List<EntidadRol> roles = servRol.buscarTodosRoles();
            roles.stream()
                    .filter(rol -> rol.getRolUsuario().toString().equals(
                            entidadCuenta.getEntidadRol().getRolUsuario().toString()))
                    .findFirst()
                    .ifPresent(entidadCuenta::setEntidadRol);

            // Buscar y asignar el cargo solo si se encuentra
            List<EntidadCargo> cargos = servCargo.buscarTodosCargos();
            cargos.stream()
                    .filter(cargo -> cargo.getCargoUsuario().toString().equals(
                            entidadCuenta.getEntidadCargo().getCargoUsuario().toString()))
                    .findFirst()
                    .ifPresent(entidadCuenta::setEntidadCargo);

            verificarDatosDuplicados(cuentaDTO);
            if(datos_duplicados.isEmpty() &&  verificarDatosCorrectos(entidadCuenta)) {
                if(servCuenta.modificarCuenta(entidadCuenta)) {
                    this.cuentaDTO = new CuentaDTO();
                    return "redirect:/rackmaster/cuentas";
                } else {
                    LOGGER.info("Nombre de usuario ya está en uso :: {}", cuentaDTO.getUsuario());
                }
            }
        }

        this.cuentaDTO = cuentaDTO;
        return editarCuentaUI(cuentaDTO.getCuenta_id(), model);

    }

    @GetMapping("/eliminar-cuenta/{cuenta_id}")
    public String eliminarCuenta(
            @PathVariable("cuenta_id") Integer cuenta_id) {
        total_cuentas = null;
        servCuenta.eliminarCuenta(cuenta_id);
        return "redirect:/rackmaster/cuentas";
    }

    private boolean verificarDatosCorrectos(EntidadCuenta entidadCuenta) {
        boolean validar = appUtil.esTexto160Largo(entidadCuenta.getNombre_completo())
                && appUtil.esTexto160Largo(entidadCuenta.getUsuario())
                && entidadCuenta.getEntidadCargo() != null
                && entidadCuenta.getEntidadRol() != null;

        if(entidadCuenta.getCuenta_id() == null) {
            return (entidadCuenta.getContrasenia().length() >= 8 && entidadCuenta.getContrasenia().length() <= 260) && validar;
        } return validar;

    }

    private void verificarDatosDuplicados(CuentaDTO cuentaDTO) {

        List<EntidadCuenta> cuentas = servCuenta.buscarTodasCuentas();
        datos_duplicados = new ArrayList<>();

        if(cuentaDTO.getCuenta_id() != null) {
            cuentas.removeIf(c -> c.getCuenta_id().equals(cuentaDTO.getCuenta_id()));
        }

        for(EntidadCuenta cuenta: cuentas) {

            if(cuenta.getNombre_completo().equals(cuentaDTO.getNombre_completo())) {
                datos_duplicados.add("NOMBRE_COMP");
            }

            if(cuenta.getUsuario().equals(cuentaDTO.getUsuario())) {
                datos_duplicados.add("USUARIO");
            }

        }

    }

}
