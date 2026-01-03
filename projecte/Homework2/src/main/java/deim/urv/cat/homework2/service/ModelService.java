package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.exception.NotFoundException;
import deim.urv.cat.homework2.exception.RestClientException;
import deim.urv.cat.homework2.exception.UnauthorizedException;
import deim.urv.cat.homework2.model.ModelDTO;
import deim.urv.cat.homework2.model.ModelListForm;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import java.util.ArrayList;
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
        try {
            // Construir la URL con query parameters
            String url = API_BASE_URL + "/models";
            if (filters != null && filters.hasFilters()) {
                url += "?" + filters.toQueryString();
            }
            
            // Hacer llamada HTTP GET usando restClient (sin autenticación para listado)
            String jsonResponse = restClient.get(url, null);
            
            // Parsear la respuesta JSON a List<ModelDTO>
            try (Jsonb jsonb = JsonbBuilder.create()) {
                // Jakarta JSON-B puede deserializar arrays directamente
                ModelDTO[] modelsArray = jsonb.fromJson(jsonResponse, ModelDTO[].class);
                
                // Convertir array a List
                List<ModelDTO> models = new ArrayList<>();
                if (modelsArray != null) {
                    for (ModelDTO model : modelsArray) {
                        models.add(model);
                    }
                }
                
                return models;
            }
        } catch (NotFoundException e) {
            // Endpoint no encontrado (404) - retornar lista vacía
            System.err.println("Endpoint /models no encontrado: " + e.getMessage());
            return new ArrayList<>();
        } catch (RestClientException e) {
            // Otros errores HTTP (500, 400, etc.)
            System.err.println("Error HTTP al obtener modelos: " + e.getMessage());
            return new ArrayList<>();
        } catch (JsonbException e) {
            // Error al parsear JSON
            System.err.println("Error al parsear JSON de modelos: " + e.getMessage());
            return new ArrayList<>();
        } catch (Exception e) {
            // Cualquier otro error (red, timeout, etc.)
            System.err.println("Error inesperado al obtener modelos: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
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
     * @throws UnauthorizedException si el modelo es privado y no hay autenticación
     */
    public ModelDTO getModelById(Long modelId, String authHeader) {
        try {
            // Construir la URL del endpoint
            String url = API_BASE_URL + "/models/" + modelId;
            
            // Hacer llamada HTTP GET (con o sin auth según si authHeader es null)
            String jsonResponse = restClient.get(url, authHeader);
            
            // Parsear JSON a ModelDTO
            try (Jsonb jsonb = JsonbBuilder.create()) {
                ModelDTO model = jsonb.fromJson(jsonResponse, ModelDTO.class);
                return model;
            }
        } catch (NotFoundException e) {
            // Modelo no encontrado (404)
            System.err.println("Modelo " + modelId + " no encontrado");
            return null;
        } catch (UnauthorizedException e) {
            // Modelo privado sin autenticación (401)
            // Re-lanzar para que el controller maneje la redirección a login
            System.err.println("Modelo " + modelId + " requiere autenticación");
            throw e;
        } catch (JsonbException e) {
            // Error al parsear JSON
            System.err.println("Error al parsear JSON del modelo " + modelId + ": " + e.getMessage());
            return null;
        } catch (Exception e) {
            // Cualquier otro error
            System.err.println("Error al obtener modelo " + modelId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
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
     * @throws UnauthorizedException si no está autenticado
     */
    public ModelDTO getPrivateModelDetails(Long modelId, String authHeader) {
        // Este método es igual que getModelById() pero SIEMPRE con autenticación
        // Simplemente delegar la llamada
        return getModelById(modelId, authHeader);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Parsea una respuesta JSON a un objeto ModelDTO.
     * 
     * @param json String JSON de la respuesta
     * @return ModelDTO o null si hay error
     */
    private ModelDTO parseJsonToModel(String json) {
        if (json == null || json.trim().isEmpty()) {
            return null;
        }
        
        try (Jsonb jsonb = JsonbBuilder.create()) {
            return jsonb.fromJson(json, ModelDTO.class);
        } catch (Exception e) {
            System.err.println("Error al parsear JSON a ModelDTO: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parsea una respuesta JSON a una lista de ModelDTO.
     * 
     * @param json String JSON de la respuesta (array)
     * @return Lista de ModelDTO o lista vacía si hay error
     */
    private List<ModelDTO> parseJsonToModelList(String json) {
        if (json == null || json.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        try (Jsonb jsonb = JsonbBuilder.create()) {
            ModelDTO[] modelsArray = jsonb.fromJson(json, ModelDTO[].class);
            
            List<ModelDTO> models = new ArrayList<>();
            if (modelsArray != null) {
                for (ModelDTO model : modelsArray) {
                    models.add(model);
                }
            }
            
            return models;
        } catch (Exception e) {
            System.err.println("Error al parsear JSON a List<ModelDTO>: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
