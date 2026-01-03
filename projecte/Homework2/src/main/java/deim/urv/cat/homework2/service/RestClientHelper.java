package deim.urv.cat.homework2.service;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Clase auxiliar para realizar llamadas HTTP a la API REST de Homework1.
 * 
 * RESPONSABILIDADES:
 * - Realizar peticiones HTTP GET, POST, PUT, DELETE
 * - Añadir headers de autenticación (Authorization)
 * - Manejar respuestas HTTP (códigos de estado, body)
 * - Lanzar excepciones personalizadas según el código HTTP
 * 
 * NOTA: Esta clase es compartida por todos los servicios REST.
 * 
 * ASIGNADO A: Persona B (con colaboración de Persona A)
 */
@ApplicationScoped
public class RestClientHelper {

    /**
     * Realiza una petición HTTP GET.
     * 
     * @param url URL del endpoint
     * @param authHeader Header de Authorization (puede ser null)
     * @return Cuerpo de la respuesta (JSON/XML)
     * @throws RestClientException Si ocurre un error HTTP
     */
    public String get(String url, String authHeader) throws RestClientException {
        
        // TODO: Abrir conexión HTTP
        // HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        // connection.setRequestMethod("GET");
        // connection.setRequestProperty("Accept", "application/json");
        
        // TODO: Añadir header de autenticación si existe
        // if (authHeader != null) {
        //     connection.setRequestProperty("Authorization", authHeader);
        // }
        
        // TODO: Obtener código de respuesta HTTP
        // int responseCode = connection.getResponseCode();
        
        // TODO: Si código es 200-299, leer el body de la respuesta
        // if (responseCode >= 200 && responseCode < 300) {
        //     BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        //     StringBuilder response = new StringBuilder();
        //     String line;
        //     while ((line = in.readLine()) != null) {
        //         response.append(line);
        //     }
        //     in.close();
        //     return response.toString();
        // }
        
        // TODO: Si código es 401, lanzar UnauthorizedException
        // if (responseCode == 401) {
        //     throw new UnauthorizedException("Authentication required");
        // }
        
        // TODO: Si código es 404, lanzar NotFoundException
        // if (responseCode == 404) {
        //     throw new NotFoundException("Resource not found");
        // }
        
        // TODO: Para otros errores, lanzar RestClientException genérica
        // throw new RestClientException("HTTP error: " + responseCode);
        
        return null; // PLACEHOLDER
    }

    /**
     * Realiza una petición HTTP POST.
     * 
     * @param url URL del endpoint
     * @param jsonBody Cuerpo de la petición en formato JSON
     * @param authHeader Header de Authorization (puede ser null)
     * @return Cuerpo de la respuesta (JSON/XML)
     * @throws RestClientException Si ocurre un error HTTP
     */
    public String post(String url, String jsonBody, String authHeader) throws RestClientException {
        
        // TODO: Abrir conexión HTTP
        // HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        // connection.setRequestMethod("POST");
        // connection.setRequestProperty("Content-Type", "application/json");
        // connection.setRequestProperty("Accept", "application/json");
        // connection.setDoOutput(true);
        
        // TODO: Añadir header de autenticación si existe
        // if (authHeader != null) {
        //     connection.setRequestProperty("Authorization", authHeader);
        // }
        
        // TODO: Escribir el body de la petición
        // OutputStream os = connection.getOutputStream();
        // os.write(jsonBody.getBytes("UTF-8"));
        // os.flush();
        // os.close();
        
        // TODO: Obtener código de respuesta y procesar igual que GET
        
        return null; // PLACEHOLDER
    }

    // TODO: Implementar métodos PUT y DELETE si son necesarios
    
    // ==================== EXCEPCIONES PERSONALIZADAS ====================

    public static class RestClientException extends RuntimeException {
        public RestClientException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedException extends RestClientException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    public static class NotFoundException extends RestClientException {
        public NotFoundException(String message) {
            super(message);
        }
    }
}
