
package edu.pe.cibertec.botica.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice(basePackages = "edu.pe.cibertec.botica.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String error404() {
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    public String error500(Exception ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("error", ex.getMessage());
        return "error/500";
    }
}
