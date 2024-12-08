package com.ngcamargob.rackmaster.presentacion.manejoErrores;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

public class ManejadorErroresGlobal {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleException(Exception ex) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("errorMessage", "Ocurrió un error inesperado.");
        mav.setViewName("error");
        return mav;
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String handleError(Model model) {
        model.addAttribute("errorMessage", "Página no encontrada o error desconocido.");
        return "error";
    }

}
