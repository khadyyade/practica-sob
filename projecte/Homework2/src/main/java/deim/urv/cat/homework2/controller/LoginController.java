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
 * Controlador para gestionar el login y logout de usuarios.
 * 
 * Funcionalidades:
 * - Mostrar formulario de login
 * - Procesar las credenciales y autenticar
 * - Redirigir al usuario a donde quería ir después del login
 * - Cerrar sesión (logout)
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
        
        // Si el usuario ya está logueado, no tiene sentido mostrar el login
        // Lo mandamos directamente a la página principal
        if (userSession.isAuthenticated()) {
            return "redirect:/Web/";
        }
        
        // Si hubo un error en un intento anterior, lo pasamos a la vista
        // para que muestre el mensaje "Dades incorrectes" o similar
        if (userSession.getLoginError() != null) {
            models.put("error", userSession.getLoginError());
            // Limpiamos el error para que no aparezca otra vez si recarga
            userSession.setLoginError(null);
        }
        
        return "login.jsp";
    }

    /**
     * POST /login
     * Procesa el formulario de login.
     * Si las credenciales son correctas, guarda la sesión y redirige.
     * Si son incorrectas, vuelve a mostrar el login con un error.
     */
    @POST
    public Response processLogin(
            @FormParam("username") String username,
            @FormParam("password") String password) {
        
        // Primero comprobamos que hayan rellenado los campos
        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            models.put("error", "Has d'introduir usuari i contrasenya");
            return Response.ok("login.jsp").build();
        }
        
        // Llamamos al servicio para verificar las credenciales contra la API
        boolean credencialsCorrectes = customerService.authenticate(username, password);
        
        if (credencialsCorrectes) {
            // ¡Login correcto! Guardamos los datos en la sesión
            userSession.setUsername(username);
            userSession.setPassword(password);
            userSession.setAuthenticated(true);
            
            // Miramos si el usuario quería ir a alguna página antes del login
            // (por ejemplo, si intentó ver un modelo privado sin estar logueado)
            String returnUrl = userSession.getReturnUrl();
            
            // Si no hay URL guardada, lo mandamos a la home
            if (returnUrl == null || returnUrl.isEmpty()) {
                returnUrl = "/Homework2/Web/";
            }
            
            // Limpiamos la URL de retorno para que no se quede guardada
            userSession.clearReturnUrl();
            
            // Redirigimos al usuario a donde quería ir
            return Response.seeOther(URI.create(returnUrl)).build();
            
        } else {
            // Las credenciales son incorrectas
            // Mostramos el mensaje de error como dice el enunciado (Figura 3)
            models.put("error", "Dades introduïdes incorrectes");
            return Response.ok("login.jsp").build();
        }
    }

    /**
     * GET /login/logout
     * Cierra la sesión del usuario.
     * Borramos todos sus datos para que nadie más pueda usar su cuenta.
     */
    @GET
    @Path("/logout")
    public Response logout() {
        
        // Limpiamos toda la sesión del usuario
        userSession.clearSession();
        
        // Lo mandamos a la página principal
        return Response.seeOther(URI.create("/Homework2/Web/")).build();
    }
}
