package com.ngcamargob.rackmaster.presentacion.controladora;

import com.ngcamargob.rackmaster.presentacion.dto.CuentaDTO;
import com.ngcamargob.rackmaster.servicio.interfaces.IServCuenta;
import com.ngcamargob.rackmaster.utilidades.AppUtil;
import com.ngcamargob.rackmaster.utilidades.mapper.ConvCuentaDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rackmaster")
public class GeneralControl {

    @Autowired
    private IServCuenta servCuenta;

    @Autowired
    private AppUtil appUtil;

    @Autowired
    private ConvCuentaDTO convCuentaDTO;

    private static boolean first_instance = true;
    private boolean invalid_session = false;
    private final static Logger LOGGER = LoggerFactory.getLogger(GeneralControl.class);


    @GetMapping("/inicio")
    public String inicioUI(Model model) {
        model.addAttribute("usuario_logeado", appUtil.getNombreUsuario());
        return "inicio";
    }

    @GetMapping("/acceder")
    public String accesoUI(Model model) {

        // load superAdmin user into the db
        if(first_instance) {
            CuentaDTO cuentaDTO = CuentaDTO.builder()
                    .nombre_completo("Administrador general")
                    .usuario("superAdmin")
                    .contrasenia("5UD0_5U..")
                    .cargo("OTRO")
                    .rol("SUPER_ADMIN")
                    .build();
            servCuenta.crearCuenta(convCuentaDTO.getOI(cuentaDTO));
            first_instance = false;
        }

        model.addAttribute("invalid_session", invalid_session);
        invalid_session = false;
        return "acceso/acceso";
    }

    @GetMapping("/invalid-session")
    public String invalidSession() {
        invalid_session = true;
        return "redirect:/rackmaster/acceder";
    }

    @GetMapping("/verificar-usuario")
    public String verificarUsuario() {
        // Validar usuario y contraseña
        UserDetails userAuthenticated = appUtil.traerUsuarioAutenticado();
        if (userAuthenticated != null) {
            LOGGER.info("User authenticated: {}", userAuthenticated);
            return "redirect:/rackmaster/inicio";
        } else { LOGGER.info("User not found: {}", userAuthenticated); }
        return "redirect:/acceder";
    }

    @GetMapping("/cerrar-sesion")
    public String cerrarSesionUI(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        } return "redirect:/rackmaster/acceder";
    }


}
