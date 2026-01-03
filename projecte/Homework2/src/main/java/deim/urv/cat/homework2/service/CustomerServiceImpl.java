package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.exception.RestClientException;
import deim.urv.cat.homework2.exception.UnauthorizedException;
import deim.urv.cat.homework2.model.CustomerDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.util.Base64;

/**
 * Implementación del servicio de Customer.
 * 
 * Esta clase implementa la interfaz CustomerService y contiene
 * toda la lógica real para comunicarse con la API REST de Homework1.
 * 
 * Solo necesitamos dos métodos:
 * 1. authenticate() → para verificar credenciales en el login
 * 2. getAuthenticatedCustomer() → para obtener datos del usuario y mostrar "Benvingut X!"
 */
@ApplicationScoped
public class CustomerServiceImpl implements CustomerService {

    @Inject
    private RestClientHelper restClient;

    // URL base de la API REST de Homework1
    private static final String API_BASE_URL = "http://localhost:8080/Homework1/rest/api/v1";

    /**
     * Comprueba si las credenciales son correctas.
     * 
     * Hacemos una llamada al endpoint GET /customer/me que tiene @Secured.
     * Este endpoint lo hemos creado específicamente para verificar credenciales
     * porque los otros métodos @Secured son PUT/POST y modificarían datos.
     * 
     * - Si la API responde 200 → credenciales correctas
     * - Si responde 401 → credenciales incorrectas (UnauthorizedException)
     * 
     * @param username nombre de usuario
     * @param password contraseña
     * @return true si las credenciales son válidas, false si no
     */
    @Override
    public boolean authenticate(String username, String password) {
        
        // Construimos el header "Basic base64(usuario:contraseña)"
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        String authHeader = "Basic " + encodedCredentials;
        
        // Llamamos a /customer/me que tiene @Secured
        String url = API_BASE_URL + "/customer/me";
        
        try {
            restClient.get(url, authHeader);
            // Si no salta excepción, las credenciales son válidas
            return true;
            
        } catch (UnauthorizedException e) {
            // 401 = credenciales incorrectas
            return false;
            
        } catch (RestClientException e) {
            // Error de red o servidor
            System.err.println("Error al autenticar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene los datos del usuario autenticado.
     * 
     * Usamos el mismo endpoint /customer/me que ya nos devuelve
     * los datos del usuario (id, username, telefono) si las credenciales son correctas.
     * 
     * Esto nos sirve para mostrar "Benvingut {username}!" en la web.
     * 
     * @param authHeader header de autorización "Basic base64(user:pass)"
     * @return CustomerDTO con los datos del usuario, o null si hay error
     */
    @Override
    public CustomerDTO getAuthenticatedCustomer(String authHeader) {
        
        String url = API_BASE_URL + "/customer/me";
        
        try {
            String jsonResponse = restClient.get(url, authHeader);
            
            // Convertimos el JSON a CustomerDTO usando Jakarta JSON-B
            try (Jsonb jsonb = JsonbBuilder.create()) {
                return jsonb.fromJson(jsonResponse, CustomerDTO.class);
            }
            
        } catch (UnauthorizedException e) {
            return null;
            
        } catch (Exception e) {
            System.err.println("Error al obtener datos del usuario: " + e.getMessage());
            return null;
        }
    }
}
