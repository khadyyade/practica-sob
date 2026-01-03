package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.model.ModelDTO;
import deim.urv.cat.homework2.model.ModelListForm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

/**
 * Servicio para interactuar con el endpoint /models de Homework1 (API REST).
 * 
 * RESPONSABILIDADES:
 * - Realizar llamadas HTTP GET a la API REST de modelos
 * - Convertir respuestas JSON/XML a objetos ModelDTO
 * - Aplicar filtros de búsqueda (topic, capability, maxPrice)
 * - Manejar errores HTTP (404, 500, etc.)
 * 
 * ASIGNADO A: Persona A
 */
@ApplicationScoped
public class ModelService {

    @Inject
    private RestClientHelper restClient;

    // URL base de la API REST de Homework1
    // Formato: http://localhost:8080/{context-root}/rest/api/v1
    private static final String API_BASE_URL = "http://localhost:8080/Homework1/rest/api/v1";

    /**
     * Obtiene la lista de modelos aplicando filtros opcionales.
     * 
     * Endpoint: GET /models
     * Query params opcionales:
     *   - capability: Lista de capabilities (máximo 2)
     *   - provider: Nombre del proveedor
     * 
     * @param filters Formulario con filtros (capabilities, provider)
     * @return Lista de modelos
     */
    public List<ModelDTO> getModels(ModelListForm filters) {
        
        // TODO: Construir la URL con query parameters
        // String url = API_BASE_URL + "/models";
        // if (filters != null && filters.hasFilters()) {
        //     url += "?" + filters.toQueryString();
        // }
        
        // TODO: Hacer llamada HTTP GET usando restClient
        // String jsonResponse = restClient.get(url, null); // Sin autenticación
        
        // TODO: Parsear la respuesta JSON a List<ModelDTO>
        // Usar Jakarta JSON-B (json-b-api está en Jakarta EE)
        // try (Jsonb jsonb = JsonbBuilder.create()) {
        //     Type listType = new TypeToken<List<ModelDTO>>(){}.getType();
        //     List<ModelDTO> models = jsonb.fromJson(jsonResponse, listType);
        //     return models;
        // }
        
        // TODO: Manejar excepciones (IOException, HTTP errors)
        // try {
        //     ...
        // } catch (RestClientHelper.NotFoundException e) {
        //     return new ArrayList<>();
        // } catch (Exception e) {
        //     // Log del error
        //     System.err.println("Error al obtener modelos: " + e.getMessage());
        //     return new ArrayList<>();
        // }
        
        return null; // PLACEHOLDER
    }

    /**
     * Obtiene un modelo por su ID (vista pública o privada).
     * 
     * Endpoint: GET /models/{id}
     * 
     * IMPORTANTE: Si el modelo es privado (isPrivate=true), requiere autenticación.
     * Sin autenticación, la API devuelve 401 Unauthorized.
     * 
     * @param modelId ID del modelo
     * @param authHeader Header de Authorization (puede ser null para modelos públicos)
     * @return Modelo o null si no existe
     */
    public ModelDTO getModelById(Long modelId, String authHeader) {
        
        // TODO: Construir la URL del endpoint
        // String url = API_BASE_URL + "/models/" + modelId;
        
        // TODO: Hacer llamada HTTP GET (con o sin auth según si authHeader es null)
        // try {
        //     String jsonResponse = restClient.get(url, authHeader);
        //     
        //     // Parsear JSON a ModelDTO
        //     try (Jsonb jsonb = JsonbBuilder.create()) {
        //         ModelDTO model = jsonb.fromJson(jsonResponse, ModelDTO.class);
        //         return model;
        //     }
        // } catch (RestClientHelper.NotFoundException e) {
        //     // Modelo no encontrado (404)
        //     return null;
        // } catch (RestClientHelper.UnauthorizedException e) {
        //     // Modelo privado sin autenticación (401)
        //     throw e; // Re-lanzar para que el controller maneje la redirección a login
        // } catch (Exception e) {
        //     System.err.println("Error al obtener modelo " + modelId + ": " + e.getMessage());
        //     return null;
        // }
        
        return null; // PLACEHOLDER
    }

    /**
     * Obtiene el detalle completo de un modelo (vista privada, requiere autenticación).
     * 
     * Endpoint: GET /models/{id} (con Authorization header)
     * 
     * NOTA: El mismo endpoint /models/{id} devuelve información completa si:
     *   1. El modelo es público (isPrivate=false): Sin autenticación
     *   2. El modelo es privado (isPrivate=true): Con autenticación
     * 
     * La respuesta incluye: provider, license, capabilities, etc.
     * 
     * @param modelId ID del modelo
     * @param authHeader Header de Authorization (HTTP Basic) - OBLIGATORIO
     * @return Modelo con información completa (licencias, provider, etc.)
     */
    public ModelDTO getPrivateModelDetails(Long modelId, String authHeader) {
        
        // TODO: Este método es igual que getModelById() pero SIEMPRE con autenticación
        // Se puede simplificar llamando a getModelById(modelId, authHeader)
        // return getModelById(modelId, authHeader);
        
        // O bien, implementar directamente:
        // String url = API_BASE_URL + "/models/" + modelId;
        // 
        // try {
        //     String jsonResponse = restClient.get(url, authHeader);
        //     
        //     try (Jsonb jsonb = JsonbBuilder.create()) {
        //         ModelDTO model = jsonb.fromJson(jsonResponse, ModelDTO.class);
        //         return model;
        //     }
        // } catch (RestClientHelper.UnauthorizedException e) {
        //     // Usuario no autenticado o sin permisos (401)
        //     throw e; // Controller redirigirá a login
        // } catch (RestClientHelper.NotFoundException e) {
        //     // Modelo no encontrado (404)
        //     return null;
        // } catch (Exception e) {
        //     System.err.println("Error al obtener modelo privado " + modelId + ": " + e.getMessage());
        //     return null;
        // }
        
        return null; // PLACEHOLDER
    }

    // ==================== MÉTODOS AUXILIARES ====================

    // TODO: Implementar método para parsear JSON a List<ModelDTO>
    // private List<ModelDTO> parseJsonToModelList(String json) {
    //     // Usar biblioteca de JSON (Jackson, Gson, Jakarta JSON-B)
    //     return null;
    // }

    // TODO: Implementar método para parsear JSON a ModelDTO
    // private ModelDTO parseJsonToModel(String json) {
    //     return null;
    // }
}
