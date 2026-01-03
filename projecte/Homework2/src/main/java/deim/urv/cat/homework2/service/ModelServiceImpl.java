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
 * Implementación del servicio de Model.
 * 
 * Esta clase implementa la interfaz ModelService y contiene
 * toda la lógica real para comunicarse con la API REST de Homework1.
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
public class ModelServiceImpl implements ModelService {

    @Inject
    private RestClientHelper restClient;

    // URL base de la API REST de Homework1
    // Formato: http://localhost:8080/{context-root}/rest/api/v1
    private static final String API_BASE_URL = "http://localhost:8080/Homework1/rest/api/v1";

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ModelDTO> getModels(ModelListForm filters) {
        System.out.println("========== ModelServiceImpl.getModels() EJECUTADO ==========");
        try {
            // Construir la URL con query parameters
            String url = API_BASE_URL + "/models";
            if (filters != null && filters.hasFilters()) {
                url += "?" + filters.toQueryString();
            }
            
            System.out.println("URL de la API: " + url);
            
            // Hacer llamada HTTP GET usando restClient (sin autenticación para listado)
            String jsonResponse = restClient.get(url, null);
            
            System.out.println("Respuesta JSON recibida: " + (jsonResponse != null ? jsonResponse.substring(0, Math.min(200, jsonResponse.length())) + "..." : "null"));
            
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
                
                System.out.println("Modelos parseados: " + models.size());
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
     * {@inheritDoc}
     */
    @Override
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
     * {@inheritDoc}
     */
    @Override
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
