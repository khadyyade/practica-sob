package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.UserSession;
import deim.urv.cat.homework2.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.Response;
import java.net.URI;

/**
 * Controlador para autenticación (login/logout).
 * 
 * RESPONSABILIDADES:
 * - Mostrar formulario de login
 * - Validar credenciales contra la API REST de Homework1
 * - Guardar credenciales en UserSession si login exitoso
 * - Redirigir a URL de retorno después del login
 * - Manejar logout (limpiar sesión)
 * - Mostrar errores de autenticación
 * 
 * ASIGNADO A: Persona B
 */
@Controller
@Path("/login")
public class LoginController {

    @Inject
    private Models models;
    
    @Inject
    private UserSession userSession;
    
    @Inject
    private CustomerService customerService;

    /**
     * GET /login
     * Muestra el formulario de login.
     */
    @GET
    public String showLoginForm() {
        
        // TODO: Verificar si el usuario ya está autenticado
        // if (userSession.isAuthenticated()) {
        //     // Si ya está logueado, redirigir a la página principal
        //     return "redirect:/";
        // }
        
        // TODO: Añadir mensaje de error si existe (después de un intento fallido)
        // models.put("error", userSession.getLoginError());
        
        return "login.jsp";
    }

    /**
     * POST /login
     * Procesa el formulario de login.
     */
    @POST
    public Response processLogin(
            @FormParam("username") String username,
            @FormParam("password") String password) {
        
        // TODO: Validar que username y password no estén vacíos
        // if (username == null || username.trim().isEmpty() ||
        //     password == null || password.trim().isEmpty()) {
        //     models.put("error", "Username and password are required");
        //     return Response.ok("login.jsp").build();
        // }
        
        // TODO: Llamar a customerService.authenticate(username, password)
        // boolean isValid = customerService.authenticate(username, password);
        
        // TODO: Si las credenciales son válidas:
        // - Guardar username y password en userSession
        // - userSession.setAuthenticated(true);
        // - userSession.setUsername(username);
        // - userSession.setPassword(password);
        
        // TODO: Obtener la URL de retorno guardada en la sesión
        // String returnUrl = userSession.getReturnUrl();
        // if (returnUrl == null || returnUrl.isEmpty()) {
        //     returnUrl = "/";
        // }
        // userSession.clearReturnUrl();
        
        // TODO: Redirigir a la URL de retorno
        // return Response.seeOther(URI.create(returnUrl)).build();
        
        // TODO: Si las credenciales NO son válidas:
        // - Mostrar mensaje de error
        // - Volver a renderizar login.jsp
        // models.put("error", "Invalid username or password");
        // return Response.ok("login.jsp").build();
        
        return Response.seeOther(URI.create("/")).build();
    }

    /**
     * GET /logout
     * Cierra la sesión del usuario.
     */
    @GET
    @Path("logout")
    public Response logout() {
        
        // TODO: Limpiar la sesión del usuario
        // userSession.clearSession();
        
        // TODO: Redirigir a la página principal
        return Response.seeOther(URI.create("/")).build();
    }
}
