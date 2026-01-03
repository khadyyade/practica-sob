package deim.urv.cat.homework2.controller;

import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

/**
 * Controlador para manejar errores HTTP y páginas de error personalizadas.
 * 
 * RESPONSABILIDADES:
 * - Mostrar páginas de error 404, 401, 500, etc.
 * - Proporcionar mensajes de error amigables al usuario
 * 
 * ASIGNADO A: Persona B
 */
@Controller
@Path("/error")
public class ErrorController {

    @Inject
    private Models models;

    /**
     * GET /error?code=404&message=...
     * Muestra una página de error personalizada.
     */
    @GET
    public String showError(
            @QueryParam("code") Integer errorCode,
            @QueryParam("message") String errorMessage) {
        
        // TODO: Añadir el código de error y el mensaje al modelo
        // models.put("errorCode", errorCode != null ? errorCode : 500);
        // models.put("errorMessage", errorMessage != null ? errorMessage : "An unexpected error occurred");
        
        // TODO: Personalizar el mensaje según el código de error
        // switch (errorCode) {
        //     case 404:
        //         models.put("errorTitle", "Resource Not Found");
        //         break;
        //     case 401:
        //         models.put("errorTitle", "Unauthorized");
        //         break;
        //     case 500:
        //         models.put("errorTitle", "Internal Server Error");
        //         break;
        //     default:
        //         models.put("errorTitle", "Error");
        // }
        
        return "Error404.jsp";
    }
}
