package deim.urv.cat.homework2.controller;

import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

/**
 * Controlador para mostrar páginas de error amigables.
 * 
 * En vez de mostrar errores feos del servidor, enseñamos una página
 * bonita explicando qué ha pasado y cómo volver a la home.
 */
@Controller
@Path("/error")
public class ErrorController {

    @Inject
    private Models models;

    /**
     * GET /error?code=404&message=...
     * Muestra una página de error con el código y mensaje correspondientes.
     */
    @GET
    public String showError(
            @QueryParam("code") Integer errorCode,
            @QueryParam("message") String errorMessage) {
        
        // Si no nos pasan código, asumimos error 500 (error del servidor)
        int code = (errorCode != null) ? errorCode : 500;
        models.put("errorCode", code);
        
        // Ponemos un título descriptivo según el tipo de error
        String title;
        switch (code) {
            case 404:
                title = "Pàgina no trobada";
                break;
            case 401:
                title = "No autoritzat";
                break;
            case 403:
                title = "Accés denegat";
                break;
            case 500:
                title = "Error del servidor";
                break;
            default:
                title = "Error";
        }
        models.put("errorTitle", title);
        
        // Si nos pasan un mensaje específico lo usamos, si no, uno genérico
        String message = (errorMessage != null) ? errorMessage : "Hi ha hagut un error inesperat";
        models.put("errorMessage", message);
        
        return "error.jsp";
    }
}
