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
 * Servicio REST para gestionar Customer
 * 
 * 1. GET /customer (Lista todos los customers)
 *    - Devolver id y username de cada customer (consultar clase Credentials)
 *    - USERNAME de cada uno: customer.getCredentials().getUsername()
 * 
 * 2. GET /customer/{id} (Detalles de un cliente concreto)
 *    - Se devuelve solo el USERNAME con ese id: customer.getCredentials().getUsername()
 *    - Para hacer que hacer que se pueda ir descubriendo los datos añadimos link HATEOAS
 *    - Le tr
 *      "links": { "model": "/models/{id}" }
 * 
 * 3. PUT /customer/{id} → Actualiza cliente (OPCIONAL, requiere @Secured)
 *    - Solo permite actualizar lastViewedModel
 * 
 * IMPORTANTE: 
 * - Customer tiene FK a Credentials (@OneToOne)
 * - Para username usar: customer.getCredentials().getUsername()
 * - NUNCA devolver passwords
 * 
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
     * Devolvemos todos los customers
     * 
     * No consumimos ningún valor
     * Pruducimos un archivo JSON con todos los campos de CUSTOMER
     * jakarta.json
     * 
     *  @NamedQuery(
     *      name = "Customer.findAll",
     *      query = "SELECT c FROM Customer c"
     *  )
     * 
     * @return Lista JSON/XML de todos los customers
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Customer> findAll() {

        List<Customer> customers = em.createNamedQuery("Customer.findAll", Customer.class).getResultList();
        
        return customers;

    }

    /**
     * GET /customer/{id}
     * 
     * Obtiene los detalles de un cliente concreto
     * Además le vamos mostrando dinamicamente la pagina web (HATEOAS)
     *   incluimos un link al último modelo visitado
     * 
     * Consumimos el id que queremos consultar
     * Producimos un JSON como este (también devuelve el correo)
     * 
     * {
     *   "id": 1,
     *   "username": "sob",
     *   "links": {
     *     "model": "/rest/api/v1/models/3"
     *   }
     * }
     * 
     * Si quisieramos una respuesta fácil podríamos hacer algo como esto:
     * 
     * return Response.ok().entity(super.find(id)).build();Ç
     * 
     * Pero no hay manera de añadir mes campos (para HATEOAS) como el links
     * Para generar este JSON he utilizado JsonObjectBuilder
     * 
     * 
     * @param id del customer
     * @return JSON del cliente + HATEOAS (404 si no existe)
     */

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id) {

        // Buscar customer
        Customer customer = super.find(id);
        
        // Si no existe, devolvemos 404 (Not Found)
        if (customer == null)
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\": \"Customer con id " + id + " no encontrado\"}").build();
        
        // Construir JSON
        // Empezamos con los campos id y username (que está en credenciales)
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("id", customer.getId())
                .add("username", customer.getUsername());
        
        // Añadir email si existe
        if (customer.getEmail() != null) {
            builder.add("email", customer.getEmail());
        }
        
        // Añadimos el campo link
        Long ultimoModeloId = customer.getUltimoModeloVisitadoId();
        
        if (ultimoModeloId != null) {
            // Construir el objeto "links" con el enlace al modelo
            JsonObject links = Json.createObjectBuilder().add("model", "/rest/api/v1/models/" + ultimoModeloId).build();
            
            // Añadimos el link
            builder.add("links", links);
        }
        
        // Construir la respuesta
        JsonObject response = builder.build();
        
        // Devolver la respuesta (200 OK)
        return Response.ok(response).build();
    }

    /**
     * PUT /customer/{id}
     * Función adicional 
     * 
     * Modifica los datos del customer con identificador ${id}
     * Hay que estar autentificado
     * NO permite cambiar username ni credentials
     * Cambia otros campos como ultimoModeloVisitadoId o email
     * 
     * Ejemplo de JSON a enviar:
     * {
     *   "ultimoModeloVisitadoId": 3
     * }
     * 
     * Consumimos el id que queremos modificar
     * Produciomos una respuesta de tipo JSON (si se ha producido error)
     * 
     * @param id ID del costumer
     * @param inputJson es el valor que vamos a modificar
     * @return Response 204 No Content si OK, o error
     */

    @PUT
    @Path("{id}")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response edit(@PathParam("id") Long id, JsonObject inputJson) {
        
        // Buscar customer
        Customer existing = super.find(id);
        
        // Si no existe, devolver 404 Not Found
        if (existing == null)
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\": \"Customer con id " + id + " no encontrado\"}").build();
        
        // Variable para controlar si se modificó algo
        boolean modificado = false;
        
        // El JSON puede contener varios cambios (saber que valor contiene)
        for (String key : inputJson.keySet()) {
            switch (key) {
            case "email":
                
                // Obtener mail a modificar
                String nuevoEmail = inputJson.getString("email");
                existing.setEmail(nuevoEmail);
                modificado = true;
                break;
                
            case "ultimoModeloVisitadoId":

                // Obtener modelo a modificar
                Long modeloId = inputJson.getJsonNumber("ultimoModeloVisitadoId").longValue();
                model.entities.Model modelo = em.find(model.entities.Model.class, modeloId);
                
                if (modelo == null)
                    return Response.status(Response.Status.NOT_FOUND).entity("{\"error\": \"Model con id " + modeloId + " no encontrado\"}").build();
                
                existing.setUltimoModeloVisitado(modelo);
                modificado = true;
                break;
                
            default:
                break;
            }
        }
        
        // Si se modificó algo, persistir cambios
        if (modificado) {
            // super.edit() para actualizar la entidad
            super.edit(existing);
            // Devolver 204 No Content (correcto)
            return Response.noContent().build();
        } else {
            // Rrror 400 Bad Request (no se ha modificado nada)
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\": \"Debes proporcionar 'email' o 'ultimoModeloVisitadoId' en el JSON\"}").build();
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
