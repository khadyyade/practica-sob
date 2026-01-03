package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.exception.NotFoundException;
import deim.urv.cat.homework2.exception.RestClientException;
import deim.urv.cat.homework2.exception.UnauthorizedException;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Esta clase es nuestro "puente" para hablar con la API REST de Homework1.
 * Tiene métodos para hacer peticiones GET y POST usando HttpURLConnection
 * (la forma básica de Java, sin librerías externas).
 * 
 * Es ApplicationScoped porque solo necesitamos una instancia para toda la app.
 * 
 */
@ApplicationScoped
public class RestClientHelper {

    /**
     * Hace una petición GET a la URL que le pasemos.
     * Si todo va bien, devuelve el JSON de respuesta como String.
     * Si hay errores, lanza excepciones según el código HTTP.
     */
    public String get(String url, String authHeader) {
        HttpURLConnection connection = null;
        
        try {
            // Abrimos la conexión con la URL de la API
            connection = (HttpURLConnection) new URL(url).openConnection();
            
            // Le decimos que queremos hacer un GET
            connection.setRequestMethod("GET");
            
            // Este header le dice al servidor que esperamos JSON, no HTML ni otra cosa
            connection.setRequestProperty("Accept", "application/json");
            
            // Si tenemos credenciales (el usuario ha hecho login), las añadimos
            // Esto es necesario para acceder a rutas protegidas de la API
            if (authHeader != null) {
                connection.setRequestProperty("Authorization", authHeader);
            }
            
            // Hacemos la petición y miramos qué código nos devuelve el servidor
            int responseCode = connection.getResponseCode();
            
            // Si el código está entre 200 y 299, significa que todo ha ido bien
            if (responseCode >= 200 && responseCode < 300) {
                // Leemos el cuerpo de la respuesta (el JSON con los datos)
                return readResponse(connection);
            }
            
            // Si llegamos aquí es que algo ha fallado, miramos qué tipo de error es
            handleErrorResponse(responseCode);
            
            // Esto nunca se ejecuta porque handleErrorResponse siempre lanza excepción
            return null;
            
        } catch (RestClientException e) {
            // Si es una de nuestras excepciones, la relanzamos tal cual
            throw e;
        } catch (Exception e) {
            // Si es otro tipo de error
            throw new RestClientException("Error al conectar con la API: " + e.getMessage(), e);
        } finally {
            // Siempre cerramos la conexión para liberar recursos
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Hace una petición POST a la URL, enviando datos en formato JSON.
     * Útil para crear nuevos recursos (comentarios, etc.) o para enviar datos.
     */
    public String post(String url, String jsonBody, String authHeader) {
        HttpURLConnection connection = null;
        
        try {
            // Abrimos la conexión igual que en GET
            connection = (HttpURLConnection) new URL(url).openConnection();
            
            // Pero ahora el método es POST (enviamos datos)
            connection.setRequestMethod("POST");
            
            // Le decimos que vamos a enviar JSON
            connection.setRequestProperty("Content-Type", "application/json");
            
            // Y que esperamos JSON de vuelta
            connection.setRequestProperty("Accept", "application/json");
            
            // Esto es importante: le decimos que vamos a escribir datos en el body
            connection.setDoOutput(true);
            
            // Si tenemos credenciales, las añadimos
            if (authHeader != null) {
                connection.setRequestProperty("Authorization", authHeader);
            }
            
            // Escribimos el JSON en el cuerpo de la petición
            if (jsonBody != null && !jsonBody.isEmpty()) {
                try (OutputStream os = connection.getOutputStream()) {
                    // Convertimos el String a bytes y lo enviamos
                    os.write(jsonBody.getBytes("UTF-8"));
                    os.flush();
                }
            }
            
            // Miramos qué nos ha respondido el servidor
            int responseCode = connection.getResponseCode();
            
            // Códigos 200-299 significan éxito (200 OK, 201 Created, etc.)
            if (responseCode >= 200 && responseCode < 300) {
                return readResponse(connection);
            }
            
            // Si hay error, lo manejamos igual que en GET
            handleErrorResponse(responseCode);
            
            return null;
            
        } catch (RestClientException e) {
            throw e;
        } catch (Exception e) {
            throw new RestClientException("Error al enviar datos a la API: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Lee el cuerpo de la respuesta HTTP línea por línea y lo devuelve como String.
     * Es un método auxiliar para no repetir código en get() y post().
     */
    private String readResponse(HttpURLConnection connection) throws Exception {
        // Usamos BufferedReader porque es más eficiente para leer texto
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), "UTF-8"))) {
            
            // StringBuilder para ir juntando las líneas
            StringBuilder response = new StringBuilder();
            String line;
            
            // Leemos línea por línea hasta que no haya más
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            return response.toString();
        }
    }

    /**
     * Convierte los códigos de error HTTP en excepciones de nuestra aplicación.
     * Así el resto del código puede hacer catch de excepciones específicas.
     */
    private void handleErrorResponse(int responseCode) {
        switch (responseCode) {
            case 401:
                // 401 = No autorizado (credenciales incorrectas o falta login)
                throw new UnauthorizedException("No tienes permiso para acceder. Comprueba tus credenciales.");
            case 404:
                // 404 = No encontrado (el recurso no existe)
                throw new NotFoundException("El recurso que buscas no existe.");
            default:
                // Cualquier otro error
                throw new RestClientException("Error de la API. Código HTTP: " + responseCode);
        }
    }
}
