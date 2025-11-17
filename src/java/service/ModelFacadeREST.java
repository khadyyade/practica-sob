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
import model.entities.Capability;

import java.net.URI;
import java.util.List;

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
     * @param capabilities Lista de capabilities    
     * @param provider Proveedor opcional
     * @return Response con lista de modelos o error
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response findAll(
            @QueryParam("capability") List<String> capabilities,
            @QueryParam("provider") String provider) {
        try {
            // No filters (findAll)
            if ((capabilities == null || capabilities.isEmpty()) && (provider == null || provider.trim().isEmpty())) {
                List<Model> models = em.createNamedQuery("Model.findAll", Model.class).getResultList();
                return Response.ok(models).build();
            }

            // Only provider (findByProvider)
            if ((capabilities == null || capabilities.isEmpty()) && provider != null && !provider.trim().isEmpty()) {
                List<Model> models = em.createNamedQuery("Model.findByProvider", Model.class)
                        .setParameter("provider", provider)
                        .getResultList();
                return Response.ok(models).build();
            }

            // Hay capabilities (JPQL dinámico)
            if (capabilities != null && !capabilities.isEmpty()) {
                if (capabilities.size() > 2) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": \"Maximum 2 capabilities allowed\"}")
                            .build();
                }

                StringBuilder jpql = new StringBuilder("SELECT DISTINCT m FROM Model m ");
                if (capabilities.size() == 1) {
                    jpql.append("JOIN m.capabilities c0 WHERE LOWER(c0.name) = LOWER(:cap0)");
                    if (provider != null && !provider.trim().isEmpty()) {
                        jpql.append(" AND LOWER(m.provider.name) = LOWER(:provider)");
                    }
                } else {
                    // si hay dos capacidades se hacen dos joins
                    jpql.append("JOIN m.capabilities c0 JOIN m.capabilities c1 WHERE LOWER(c0.name) = LOWER(:cap0) AND LOWER(c1.name) = LOWER(:cap1)");
                    if (provider != null && !provider.trim().isEmpty()) {
                        jpql.append(" AND LOWER(m.provider.name) = LOWER(:provider)");
                    }
                }
                jpql.append(" ORDER BY m.name");

                TypedQuery<Model> query = em.createQuery(jpql.toString(), Model.class);
                // set capability params
                query.setParameter("cap0", capabilities.get(0));
                if (capabilities.size() > 1) {
                    query.setParameter("cap1", capabilities.get(1));
                }
                if (provider != null && !provider.trim().isEmpty()) {
                    query.setParameter("provider", provider);
                }

                List<Model> models = query.getResultList();
                return Response.ok(models).build();
            }

            // Fallback
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * GET /models/{id}
     * 
     * @param id ID del modelo
     * @param authHeader Header de autorización 
     * @return Response con modelo o error
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response find(@PathParam("id") Long id, @HeaderParam("Authorization") String authHeader) {
        try {
            Model model = super.find(id);
            if (model == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Model not found\"}")
                        .build();
            }

            // If private and no auth header -> 401
            if (model.isIsPrivate() && (authHeader == null || authHeader.trim().isEmpty())) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\": \"Authentication required\"}")
                        .build();
            }

            return Response.ok(model).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * POST /models
     * 
     * @param model Modelo a crear (desde JSON)
     * @return Response 201 Created o error
     */
    @POST
    @Secured
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createModel(Model model) {
        try {
            if (model == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Model payload is required\"}")
                        .build();
            }

            if (model.getName() == null || model.getName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Model name is required\"}")
                        .build();
            }

            if (model.getProvider() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"Provider is required\"}")
                        .build();
            }

            // Persist
            super.create(model);
            URI location = uriInfo.getAbsolutePathBuilder().path(model.getId().toString()).build();
            return Response.created(location).entity(model).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * PUT /models/{id}
     */
    @PUT
    @Path("{id}")
    @Secured
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response edit(@PathParam("id") Long id, Model model) {
        try {
            Model existing = super.find(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Model not found\"}")
                        .build();
            }

            model.setId(id);
            super.edit(model);
            return Response.ok(model).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    /**
     * DELETE /models/{id}
     */
    @DELETE
    @Path("{id}")
    @Secured
    public Response remove(@PathParam("id") Long id) {
        try {
            Model existing = super.find(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\": \"Model not found\"}")
                        .build();
            }

            super.remove(existing);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
