package service;

import authn.Secured;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import model.entities.Model;

import java.net.URI;
import java.util.List;

/**
 * Servicio REST para gestionar modelos LLM
 * 
 * TODO PERSONA A: Implementar los siguientes endpoints:
 * 
 * 1. GET /models → Lista todos los modelos (con filtros opcionales)
 *    - Query params: capability (0-2 veces), provider
 *    - Debe usar queries JPQL/NamedQueries (NO filtrar en Java)
 * 
 * 2. GET /models/{id} → Devuelve detalles completos
 *    - Si model.isPrivate() == true y no hay auth → devolver 401
 * 
 * 3. POST /models → Crea modelo (requiere @Secured)
 *    - Validar campos obligatorios
 *    - Devolver 201 Created con Location header
 * 
 * 4. PUT /models/{id} → Actualiza modelo (OPCIONAL, requiere @Secured)
 * 
 * 5. DELETE /models/{id} → Elimina modelo (OPCIONAL, requiere @Secured)
 * 
 * HINTS:
 * - Mirar CommentFacadeREST.java como ejemplo
 * - Para construir JPQL dinámico, usar StringBuilder
 * - Para múltiples capabilities usar: ":capability0 MEMBER OF m.capabilities"
 */
@Stateless
@Path("models")
public class ModelFacadeREST extends AbstractFacade<Model> {

    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    @Context
    private UriInfo uriInfo;

    public ModelFacadeREST() {
        super(Model.class);
    }

    /**
     * GET /models
     * 
     * TODO PERSONA A: Implementar listado con filtros
     * 
     * Casos a manejar:
     * 1. Sin filtros → usar NamedQuery "Model.findAll"
     * 2. Solo provider → usar NamedQuery "Model.findByProvider"
     * 3. Con capabilities (1 o 2) → construir JPQL dinámico:
     *    "SELECT m FROM Model m WHERE :capability0 MEMBER OF m.capabilities [AND :capability1 MEMBER OF m.capabilities] [AND LOWER(m.provider) = LOWER(:provider)] ORDER BY m.name"
     * 4. Validar que no haya más de 2 capabilities → devolver 400 Bad Request
     * 
     * @param capabilities Lista de capabilities (puede estar vacía)
     * @param provider Proveedor opcional
     * @return Response con lista de modelos o error
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response findAll(
            @QueryParam("capability") List<String> capabilities,
            @QueryParam("provider") String provider) {
        
        // TODO PERSONA A: Implementar la lógica de filtrado
        // 1. Verificar si capabilities es null o vacía y provider es null → usar Model.findAll
        // 2. Si solo hay provider (sin capabilities) → usar Model.findByProvider
        // 3. Si hay capabilities:
        //    - Validar que capabilities.size() <= 2
        //    - Construir query JPQL dinámica
        //    - Añadir condiciones para cada capability
        //    - Añadir condición de provider si existe
        // 4. Ejecutar query y devolver Response.ok(models)
        // 5. Capturar excepciones y devolver Response.status(500) con mensaje de error
        
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("{\"error\": \"TODO: Implementar findAll\"}")
                .build();
    }

    /**
     * GET /models/{id}
     * 
     * TODO PERSONA A: Implementar obtención de modelo por ID
     * 
     * Pasos:
     * 1. Buscar model con super.find(id)
     * 2. Si model == null → devolver 404 Not Found
     * 3. Si model.isPrivate() == true Y authHeader es null/vacío → devolver 401 Unauthorized
     * 4. Si todo OK → devolver 200 con el modelo
     * 
     * @param id ID del modelo
     * @param authHeader Header de autorización (puede ser null)
     * @return Response con modelo o error
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response find(@PathParam("id") Long id, @HeaderParam("Authorization") String authHeader) {
        // TODO PERSONA A: Implementar búsqueda por ID
        
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("{\"error\": \"TODO: Implementar find\"}")
                .build();
    }

    /**
     * POST /models
     * 
     * TODO PERSONA A: Implementar creación de modelo
     * 
     * Pasos:
     * 1. Validar que model.getName() no sea null ni vacío → 400 Bad Request
     * 2. Validar que model.getProvider() no sea null ni vacío → 400 Bad Request
     * 3. Validar que maxContextTokens (si existe) sea > 0 → 400 Bad Request
     * 4. Persistir con super.create(model)
     * 5. Construir URI del nuevo recurso: uriInfo.getAbsolutePathBuilder().path(model.getId().toString()).build()
     * 6. Devolver Response.created(location).entity(model).build()
     * 
     * @param model Modelo a crear (desde JSON)
     * @return Response 201 Created o error
     */
    @POST
    @Secured
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createModel(Model model) {
        // TODO PERSONA A: Implementar creación con validaciones
        
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("{\"error\": \"TODO: Implementar create\"}")
                .build();
    }

    /**
     * PUT /models/{id}
     * OPCIONAL
     * 
     * TODO PERSONA A (OPCIONAL): Implementar actualización
     * 
     * Pasos:
     * 1. Buscar modelo existente con super.find(id)
     * 2. Si no existe → 404 Not Found
     * 3. Asignar id al modelo recibido: model.setId(id)
     * 4. Actualizar con super.edit(model)
     * 5. Devolver 200 OK con modelo actualizado
     */
    @PUT
    @Path("{id}")
    @Secured
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response edit(@PathParam("id") Long id, Model model) {
        // TODO PERSONA A (OPCIONAL): Implementar actualización
        
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("{\"error\": \"TODO OPCIONAL: Implementar edit\"}")
                .build();
    }

    /**
     * DELETE /models/{id}
     * OPCIONAL
     * 
     * TODO PERSONA A (OPCIONAL): Implementar eliminación
     * 
     * Pasos:
     * 1. Buscar modelo con super.find(id)
     * 2. Si no existe → 404 Not Found
     * 3. Eliminar con super.remove(model)
     * 4. Devolver 204 No Content
     */
    @DELETE
    @Path("{id}")
    @Secured
    public Response remove(@PathParam("id") Long id) {
        // TODO PERSONA A (OPCIONAL): Implementar eliminación
        
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("{\"error\": \"TODO OPCIONAL: Implementar remove\"}")
                .build();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
