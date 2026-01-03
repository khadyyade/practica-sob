package deim.urv.cat.homework2.service;

import deim.urv.cat.homework2.exception.UnauthorizedException;
import deim.urv.cat.homework2.model.ModelDTO;
import deim.urv.cat.homework2.model.ModelListForm;
import java.util.List;

/**
 * Interfaz del servicio de Model.
 * 
 * Define las operaciones relacionadas con los modelos de IA.
 * Usamos una interfaz para seguir el patrón que usa el profesor
 * y poder cambiar la implementación si hace falta (por ejemplo, para tests).
 * 
 * La implementación real está en ModelServiceImpl.
 */
public interface ModelService {

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
    List<ModelDTO> getModels(ModelListForm filters);

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
    ModelDTO getModelById(Long modelId, String authHeader);

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
    ModelDTO getPrivateModelDetails(Long modelId, String authHeader);
}
