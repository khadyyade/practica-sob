package service;

import authn.Secured;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import model.entities.Customer;

import java.util.List;

/**
 * Servicio REST para gestionar clientes/usuarios
 * 
 * TODO PERSONA B: Implementar los siguientes endpoints:
 * 
 * 1. GET /customer → Lista todos los clientes (SIN passwords)
 * 
 * 2. GET /customer/{id} → Detalles del cliente
 *    - Construir JSON manualmente con Jakarta JSON-P
 *    - Añadir link HATEOAS si tiene lastViewedModel:
 *      "links": { "model": "/models/{id}" }
 * 
 * 3. PUT /customer/{id} → Actualiza cliente (OPCIONAL, requiere @Secured)
 * 
 * IMPORTANTE: Los clientes NO tienen campo password.
 * Las contraseñas están en la tabla Credentials (authn/Credentials.java)
 * 
 * HINTS:
 * - Usar NamedQuery "Customer.findAll" para listar
 * - Para JSON manual: Json.createObjectBuilder().add("campo", valor).build()
 * - Mirar ModelFacadeREST.java como referencia
 */
@Stateless
@Path("customer")
public class CustomerFacadeREST extends AbstractFacade<Customer> {

    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    @Context
    private UriInfo uriInfo;

    public CustomerFacadeREST() {
        super(Customer.class);
    }

    /**
     * GET /customer
     * 
     * TODO PERSONA B: Implementar listado de clientes
     * 
     * Pasos:
     * 1. Ejecutar NamedQuery "Customer.findAll" para obtener lista
     * 2. Devolver Response.ok(customers).build()
     * 
     * Nota: Como Customer no tiene campo password, se puede devolver directamente
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response findAll() {
        // TODO PERSONA B: Implementar listado
        
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("{\"error\": \"TODO: Implementar findAll\"}")
                .build();
    }

    /**
     * GET /customer/{id}
     * 
     * TODO PERSONA B: Implementar obtención con HATEOAS
     * 
     * Pasos:
     * 1. Buscar customer con super.find(id)
     * 2. Si customer == null → devolver 404 Not Found
     * 3. Construir JSON manualmente:
     *    JsonObjectBuilder builder = Json.createObjectBuilder()
     *        .add("id", customer.getId())
     *        .add("username", customer.getUsername());
     * 4. Añadir campos opcionales si existen (displayName, email)
     * 5. Si customer.getLastViewedModelId() != null:
     *    JsonObject links = Json.createObjectBuilder()
     *        .add("model", "/models/" + customer.getLastViewedModelId())
     *        .build();
     *    builder.add("links", links);
     * 6. JsonObject response = builder.build();
     * 7. Devolver Response.ok(response).build()
     * 
     * @param id ID del cliente
     * @return Response con JSON del cliente + HATEOAS
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id) {
        // TODO PERSONA B: Implementar búsqueda con HATEOAS
        
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("{\"error\": \"TODO: Implementar find con HATEOAS\"}")
                .build();
    }

    /**
     * PUT /customer/{id}
     * OPCIONAL
     * 
     * TODO PERSONA B (OPCIONAL): Implementar actualización
     * 
     * Pasos:
     * 1. Buscar customer existente con super.find(id)
     * 2. Si no existe → 404 Not Found
     * 3. Actualizar SOLO campos permitidos (NO actualizar username):
     *    - Si customer.getDisplayName() != null → existing.setDisplayName(...)
     *    - Si customer.getEmail() != null → existing.setEmail(...)
     *    - Si customer.getLastViewedModel() != null → existing.setLastViewedModel(...)
     * 4. Persistir con super.edit(existing)
     * 5. Devolver Response.ok(existing).build()
     * 
     * @param id ID del cliente
     * @param customer Datos a actualizar
     * @return Response con cliente actualizado o error
     */
    @PUT
    @Path("{id}")
    @Secured
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response edit(@PathParam("id") Long id, Customer customer) {
        // TODO PERSONA B (OPCIONAL): Implementar actualización
        
        return Response.status(Response.Status.NOT_IMPLEMENTED)
                .entity("{\"error\": \"TODO OPCIONAL: Implementar edit\"}")
                .build();
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
