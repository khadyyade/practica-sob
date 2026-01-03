package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.CommentDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Servicio para interactuar con el endpoint /comments de Homework1 (API REST).
 * 
 * RESPONSABILIDADES:
 * - Obtener comentarios de un modelo (requiere autenticación)
 * - Añadir nuevos comentarios (requiere autenticación)
 * - Convertir respuestas JSON/XML a objetos CommentDTO
 * - Manejar errores HTTP (401, 404, 500)
 * 
 * ASIGNADO A: Persona B
 */
@ApplicationScoped
public class CommentService {

    @Inject
    private RestClientHelper restClient;

    // URL base de la API REST de Homework1
    private static final String API_BASE_URL = "http://localhost:8080/Homework1/rest/api/v1";

    /**
     * Obtiene todos los comentarios (requiere autenticación).
     * 
     * Endpoint: GET /comment
     * 
     * NOTA: En Homework1, el endpoint base /comment devuelve TODOS los comentarios.
     * No hay filtrado por modelo en la API actual.
     * Si necesitas filtrar por modelo, deberás hacerlo en el cliente después de obtener todos.
     * 
     * @param authHeader Header de Authorization (HTTP Basic) - OPCIONAL según diseño
     * @return Lista de todos los comentarios
     */
    public List<CommentDTO> getAllComments(String authHeader) {
        
        // TODO: Construir la URL del endpoint
        // String url = API_BASE_URL + "/comment";
        
        // TODO: Hacer llamada HTTP GET con autenticación (si es necesario)
        // try {
        //     String jsonResponse = restClient.get(url, authHeader);
        //     
        //     // Parsear JSON a List<CommentDTO>
        //     try (Jsonb jsonb = JsonbBuilder.create()) {
        //         Type listType = new TypeToken<List<CommentDTO>>(){}.getType();
        //         List<CommentDTO> comments = jsonb.fromJson(jsonResponse, listType);
        //         return comments;
        //     }
        // } catch (RestClientHelper.UnauthorizedException e) {
        //     // Sin autenticación (401)
        //     throw e;
        // } catch (Exception e) {
        //     System.err.println("Error al obtener comentarios: " + e.getMessage());
        //     return new ArrayList<>();
        // }
        
        return null; // PLACEHOLDER
    }

    /**
     * Obtiene un comentario por ID (requiere autenticación).
     * 
     * Endpoint: GET /comment/{id}
     * 
     * IMPORTANTE: Este endpoint tiene @Secured, requiere autenticación.
     * 
     * @param commentId ID del comentario
     * @param authHeader Header de Authorization (HTTP Basic) - OBLIGATORIO
     * @return Comentario o null si no existe
     */
    public CommentDTO getCommentById(Long commentId, String authHeader) {
        
        // TODO: Construir la URL del endpoint
        // String url = API_BASE_URL + "/comment/" + commentId;
        
        // TODO: Hacer llamada HTTP GET con autenticación
        // try {
        //     String jsonResponse = restClient.get(url, authHeader);
        //     
        //     try (Jsonb jsonb = JsonbBuilder.create()) {
        //         CommentDTO comment = jsonb.fromJson(jsonResponse, CommentDTO.class);
        //         return comment;
        //     }
        // } catch (RestClientHelper.UnauthorizedException e) {
        //     throw e; // Requiere login
        // } catch (RestClientHelper.NotFoundException e) {
        //     return null; // Comentario no encontrado
        // } catch (Exception e) {
        //     System.err.println("Error al obtener comentario " + commentId + ": " + e.getMessage());
        //     return null;
        // }
        
        return null; // PLACEHOLDER
    }

    /**
     * Añade un nuevo comentario (requiere autenticación).
     * 
     * Endpoint: POST /comment
     * Content-Type: application/json
     * 
     * Formato del JSON:
     * {
     *   "text": "Comentario de ejemplo",
     *   "customer": { "id": 1 },
     *   "model": { "id": 2 }
     * }
     * 
     * NOTA: Este endpoint NO tiene @Secured en Homework1, pero deberías usarlo con autenticación.
     * 
     * @param comment Objeto CommentDTO con los datos del comentario
     * @param authHeader Header de Authorization (HTTP Basic) - RECOMENDADO
     * @return true si se añadió correctamente, false en caso contrario
     */
    public boolean addComment(CommentDTO comment, String authHeader) {
        
        // TODO: Construir la URL del endpoint
        // String url = API_BASE_URL + "/comment";
        
        // TODO: Convertir CommentDTO a JSON
        // try (Jsonb jsonb = JsonbBuilder.create()) {
        //     String jsonBody = jsonb.toJson(comment);
        //     
        //     // Hacer llamada HTTP POST con autenticación
        //     String response = restClient.post(url, jsonBody, authHeader);
        //     
        //     // Si no lanza excepción, se creó correctamente
        //     return true;
        // } catch (RestClientHelper.UnauthorizedException e) {
        //     // No autenticado (401)
        //     return false;
        // } catch (Exception e) {
        //     System.err.println("Error al añadir comentario: " + e.getMessage());
        //     return false;
        // }
        
        return false; // PLACEHOLDER
    }

    // ==================== MÉTODOS AUXILIARES ====================

    // TODO: Si decides NO usar Jakarta JSON-B, implementa estos métodos con Gson o Jackson
    // private List<CommentDTO> parseJsonToCommentList(String json) {
    //     // Ejemplo con Gson:
    //     // Gson gson = new Gson();
    //     // Type listType = new TypeToken<List<CommentDTO>>(){}.getType();
    //     // return gson.fromJson(json, listType);
    //     return null;
    // }
}
