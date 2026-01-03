package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.CustomerDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.ArrayList;

/**
 * Servicio para interactuar con el endpoint /customers de Homework1 (API REST).
 * 
 * RESPONSABILIDADES:
 * - Autenticar usuarios mediante HTTP Basic contra la API REST
 * - Obtener información del cliente autenticado
 * - Manejar errores HTTP (401 Unauthorized, 404 Not Found)
 * 
 * ASIGNADO A: Persona B
 */
@ApplicationScoped
public class CustomerService {

    @Inject
    private RestClientHelper restClient;

    // URL base de la API REST de Homework1
    private static final String API_BASE_URL = "http://localhost:8080/Homework1/rest/api/v1";

    /**
     * Autentica un usuario contra la API REST.
     * 
     * Estrategia: Hacer una petición a un endpoint que requiera autenticación.
     * Si devuelve 200 OK → credenciales válidas
     * Si devuelve 401 Unauthorized → credenciales inválidas
     * 
     * Endpoint sugerido: GET /customer/{id} (no tiene @Secured, pero podemos probar con cualquier otro)
     * Mejor opción: Hacer GET /comment/{id} que SÍ tiene @Secured
     * 
     * NOTA: Homework1 usa HTTP Basic Authentication manejado por RESTRequestFilter
     * 
     * @param username Nombre de usuario
     * @param password Contraseña
     * @return true si las credenciales son válidas, false en caso contrario
     */
    public boolean authenticate(String username, String password) {
        
        // TODO: Construir el header de Authorization (HTTP Basic)
        // import java.util.Base64;
        // import java.nio.charset.StandardCharsets;
        // String credentials = username + ":" + password;
        // String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        // String authHeader = "Basic " + encodedCredentials;
        
        // TODO: Hacer una llamada de prueba a un endpoint protegido
        // Opción 1: GET /comment/1 (tiene @Secured, requiere autenticación)
        // Opción 2: Probar con cualquier endpoint que sepamos que existe
        // String url = API_BASE_URL + "/comment/1"; // Ajustar ID según tu BD
        
        // TODO: Llamar a restClient.get(url, authHeader)
        // try {
        //     String response = restClient.get(url, authHeader);
        //     // Si no lanza excepción, las credenciales son válidas
        //     return true;
        // } catch (RestClientHelper.UnauthorizedException e) {
        //     // 401 Unauthorized → credenciales inválidas
        //     return false;
        // } catch (RestClientHelper.NotFoundException e) {
        //     // 404 Not Found → el recurso no existe, pero las credenciales SÍ son válidas
        //     // (porque si fueran inválidas, habría devuelto 401 antes)
        //     return true;
        // } catch (Exception e) {
        //     // Error de red, timeout, etc.
        //     System.err.println("Error al autenticar: " + e.getMessage());
        //     return false;
        // }
        
        return false; // PLACEHOLDER
    }

    /**
     * Obtiene la información del cliente autenticado.
     * 
     * Endpoint: GET /customer/{id}
     * 
     * NOTA: En Homework1, este endpoint NO requiere @Secured, pero devuelve info del cliente.
     * Necesitarás saber el ID del cliente. Alternativamente, puedes:
     *   1. Obtener todos los clientes (GET /customer) y buscar por username
     *   2. Implementar un endpoint /customer/me en Homework1 (recomendado)
     * 
     * @param customerId ID del cliente
     * @param authHeader Header de Authorization (HTTP Basic) - OPCIONAL
     * @return Información del cliente o null si no está autenticado
     */
    public CustomerDTO getCustomerById(Long customerId, String authHeader) {
        
        // TODO: Construir la URL del endpoint
        // String url = API_BASE_URL + "/customer/" + customerId;
        
        // TODO: Hacer llamada HTTP GET con autenticación (opcional)
        // try {
        //     String jsonResponse = restClient.get(url, authHeader);
        //     
        //     // Parsear JSON a CustomerDTO
        //     try (Jsonb jsonb = JsonbBuilder.create()) {
        //         CustomerDTO customer = jsonb.fromJson(jsonResponse, CustomerDTO.class);
        //         return customer;
        //     }
        // } catch (RestClientHelper.NotFoundException e) {
        //     // Cliente no encontrado (404)
        //     return null;
        // } catch (Exception e) {
        //     System.err.println("Error al obtener cliente " + customerId + ": " + e.getMessage());
        //     return null;
        // }
        
        return null; // PLACEHOLDER
    }

    /**
     * Obtiene todos los clientes.
     * 
     * Endpoint: GET /customer
     * 
     * Devuelve un array JSON con todos los clientes (id, username, telefono, ultimoModeloVisitado).
     * 
     * @return Lista de clientes
     */
    public List<CustomerDTO> getAllCustomers() {
        
        // TODO: Construir la URL del endpoint
        // String url = API_BASE_URL + "/customer";
        
        // TODO: Hacer llamada HTTP GET (sin autenticación)
        // try {
        //     String jsonResponse = restClient.get(url, null);
        //     
        //     // Parsear JSON a List<CustomerDTO>
        //     try (Jsonb jsonb = JsonbBuilder.create()) {
        //         Type listType = new TypeToken<List<CustomerDTO>>(){}.getType();
        //         List<CustomerDTO> customers = jsonb.fromJson(jsonResponse, listType);
        //         return customers;
        //     }
        // } catch (Exception e) {
        //     System.err.println("Error al obtener clientes: " + e.getMessage());
        //     return new ArrayList<>();
        // }
        
        return null; // PLACEHOLDER
    }

    /**
     * Busca un cliente por username.
     * 
     * NOTA: Este método NO existe en la API REST de Homework1.
     * Implementación sugerida: Obtener todos los clientes y filtrar por username.
     * 
     * @param username Username del cliente
     * @return Cliente o null si no existe
     */
    public CustomerDTO getCustomerByUsername(String username) {
        
        // TODO: Obtener todos los clientes
        // List<CustomerDTO> allCustomers = getAllCustomers();
        
        // TODO: Filtrar por username
        // for (CustomerDTO customer : allCustomers) {
        //     if (customer.getUsername().equalsIgnoreCase(username)) {
        //         return customer;
        //     }
        // }
        
        // TODO: Si no se encuentra, retornar null
        // return null;
        
        return null; // PLACEHOLDER
    }
}
