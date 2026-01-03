package deim.urv.cat.homework2.model;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Base64;

/**
 * Bean de sesión que almacena el estado de autenticación del usuario.
 * 
 * Un BEAM es una clase que no creamos con un NEW sino que Jakarta la crea y la gestiona
 * Recordar que tiene un tiempo de vida y un estado
 * Por ejemplo, en este caso, gestionamos al usuario que está conectado mientras dura la sesión
 * 
 * Esto se necesita porque como HTTP es sin estado alguien tiene que recordar si estamos autentificados o no
 * 
 * RESPONSABILIDADES:
 * - Mantener username y password del usuario autenticado
 * - Proporcionar el header de Authorization (HTTP Basic)
 * - Guardar la URL de retorno para redirección post-login
 * - Indicar si el usuario está autenticado
 * 
 */
@Named
@SessionScoped
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private boolean authenticated;
    private String returnUrl;
    private String loginError;

    // Constructor vacío: cuando se crea la sesión, el usuario NO está logueado
    public UserSession() {
        this.authenticated = false;
    }

    /**
     * Genera la cabecera de autenticación para enviar a la API.
     * La API usa HTTP Basic Auth, así que tenemos que mandar las credenciales
     * en formato "Basic usuario:contraseña" pero codificado en Base64.
     */
    public String getAuthHeader() {
        // Si no hay login o faltan datos, no podemos generar nada
        if (!authenticated || username == null || password == null) {
            return null;
        }
        
        // Juntamos usuario y contraseña con dos puntos (así lo pide HTTP Basic)
        String credentials = username + ":" + password;
        
        // Lo pasamos a Base64 porque no se puede mandar texto plano en las cabeceras
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        
        // Devolvemos con el prefijo "Basic " que espera la API
        return "Basic " + encodedCredentials;
    }

    /**
     * Limpia toda la sesión cuando el usuario hace logout.
     * Básicamente borramos todo lo que sabíamos del usuario.
     */
    public void clearSession() {
        // Borramos las credenciales
        this.username = null;
        this.password = null;
        
        // Ya no está autenticado
        this.authenticated = false;
        
        // Limpiamos también la URL a la que iba antes del login
        this.returnUrl = null;
        
        // Y cualquier error que hubiera
        this.loginError = null;
    }

    /**
     * Solo limpia la URL de retorno.
     * Esto lo usamos después de redirigir al usuario tras el login,
     * para que no se quede guardada una URL vieja.
     */
    public void clearReturnUrl() {
        this.returnUrl = null;
    }

    // ==================== GETTERS Y SETTERS ====================

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getLoginError() {
        return loginError;
    }

    public void setLoginError(String loginError) {
        this.loginError = loginError;
    }
}
