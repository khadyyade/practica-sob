package deim.urv.cat.homework2.model;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.Base64;

/**
 * Bean de sesión que almacena el estado de autenticación del usuario.
 * 
 * RESPONSABILIDADES:
 * - Mantener username y password del usuario autenticado
 * - Proporcionar el header de Authorization (HTTP Basic)
 * - Guardar la URL de retorno para redirección post-login
 * - Indicar si el usuario está autenticado
 * 
 * ASIGNADO A: Persona B
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

    // TODO: Implementar constructor vacío
    public UserSession() {
        this.authenticated = false;
    }

    // TODO: Método para generar el header de Authorization (HTTP Basic)
    // public String getAuthHeader() {
    //     if (!authenticated || username == null || password == null) {
    //         return null;
    //     }
    //     String credentials = username + ":" + password;
    //     String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
    //     return "Basic " + encodedCredentials;
    // }

    // TODO: Método para limpiar la sesión (logout)
    // public void clearSession() {
    //     this.username = null;
    //     this.password = null;
    //     this.authenticated = false;
    //     this.returnUrl = null;
    //     this.loginError = null;
    // }

    // TODO: Método para limpiar la URL de retorno
    // public void clearReturnUrl() {
    //     this.returnUrl = null;
    // }

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
