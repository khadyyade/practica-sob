package service;

import authn.Secured;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.GenericEntity;
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
            // Limpiar capabilities vacías que envía el test de NetBeans
            if (capabilities != null) {
                capabilities.removeIf(c -> c == null || c.trim().isEmpty());
            }
            
            // No filters (findAll)
            if ((capabilities == null || capabilities.isEmpty()) && (provider == null || provider.trim().isEmpty())) {
                List<Model> models = em.createNamedQuery("Model.findAll", Model.class).getResultList();
                GenericEntity<List<Model>> entity = new GenericEntity<List<Model>>(models) {};
                return Response.ok(entity).build();
            }

            // Only provider (findByProvider)
            if ((capabilities == null || capabilities.isEmpty()) && provider != null && !provider.trim().isEmpty()) {
                List<Model> models = em.createNamedQuery("Model.findByProvider", Model.class)
                        .setParameter("provider", provider)
                        .getResultList();
                GenericEntity<List<Model>> entity = new GenericEntity<List<Model>>(models) {};
                return Response.ok(entity).build();
            }

            // Hay capabilities (JPQL dinámico)
            if (capabilities != null && !capabilities.isEmpty()) {
                if (capabilities.size() > 2) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Maximum 2 capabilities allowed")
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
                GenericEntity<List<Model>> entity = new GenericEntity<List<Model>>(models) {};
                return Response.ok(entity).build();
            }

            // Fallback
            List<Model> models = em.createNamedQuery("Model.findAll", Model.class).getResultList();
            GenericEntity<List<Model>> entity = new GenericEntity<List<Model>>(models) {};
            return Response.ok(entity).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    /**
     * GET /models/{id}
     * 
     * @param id ID del modelo
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
                        .entity("Model not found")
                        .build();
            }

            // Si el modelo es privado, requiere autenticación
            if (model.isIsPrivate() && (authHeader == null || authHeader.isEmpty())) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .header("WWW-Authenticate", "Basic realm=\"practica-sob\"")
                        .entity("Authentication required for private models")
                        .build();
            }

            return Response.ok(model).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
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
                        .entity("Model payload is required")
                        .build();
            }

            if (model.getName() == null || model.getName().trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Model name is required")
                        .build();
            }

            if (model.getProvider() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Provider is required")
                        .build();
            }

            // Si el provider solo tiene nombre, intentar buscarlo en la BD
            if (model.getProvider().getId() == null && model.getProvider().getName() != null) {
                // Buscar provider por nombre
                TypedQuery<model.entities.Provider> query = em.createQuery(
                    "SELECT p FROM Provider p WHERE LOWER(p.name) = LOWER(:name)", 
                    model.entities.Provider.class);
                query.setParameter("name", model.getProvider().getName());
                
                try {
                    model.entities.Provider existingProvider = query.getSingleResult();
                    model.setProvider(existingProvider);
                } catch (Exception e) {
                    // Provider no existe
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Provider '" + model.getProvider().getName() + "' does not exist")
                            .build();
                }
            }

            // Si la license solo tiene nombre, intentar buscarla
            if (model.getLicense() != null && model.getLicense().getId() == null && model.getLicense().getName() != null) {
                TypedQuery<model.entities.License> query = em.createQuery(
                    "SELECT l FROM License l WHERE LOWER(l.name) = LOWER(:name)", 
                    model.entities.License.class);
                query.setParameter("name", model.getLicense().getName());
                
                try {
                    model.entities.License existingLicense = query.getSingleResult();
                    model.setLicense(existingLicense);
                } catch (Exception e) {
                    // License no existe, dejar que sea null
                    model.setLicense(null);
                }
            }

            // Persist
            super.create(model);
            URI location = uriInfo.getAbsolutePathBuilder().path(model.getId().toString()).build();
            return Response.created(location).entity(model).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
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
            if (model == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Model payload is required")
                        .build();
            }

            Model existing = super.find(id);
            if (existing == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Model not found")
                        .build();
            }

            // Validar que se proporcione al menos un campo para actualizar
            boolean hasValidField = false;
            
            if (model.getName() != null && !model.getName().trim().isEmpty()) {
                hasValidField = true;
            } else if (model.getName() != null && model.getName().trim().isEmpty()) {
                // Si el nombre está presente pero vacío, es un error
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Model name cannot be empty")
                        .build();
            }
            
            if (model.getSummary() != null) {
                hasValidField = true;
            }
            
            if (model.getDescription() != null) {
                hasValidField = true;
            }
            
            if (model.getVersion() != null) {
                hasValidField = true;
            }
            
            if (model.getProvider() != null) {
                hasValidField = true;
                if (model.getProvider().getName() == null || model.getProvider().getName().trim().isEmpty()) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Provider name cannot be empty if provider is specified")
                            .build();
                }
                
                // Buscar el provider si solo tiene nombre
                if (model.getProvider().getId() == null) {
                    TypedQuery<model.entities.Provider> query = em.createQuery(
                        "SELECT p FROM Provider p WHERE LOWER(p.name) = LOWER(:name)", 
                        model.entities.Provider.class);
                    query.setParameter("name", model.getProvider().getName());
                    
                    try {
                        model.entities.Provider existingProvider = query.getSingleResult();
                        model.setProvider(existingProvider);
                    } catch (Exception e) {
                        return Response.status(Response.Status.BAD_REQUEST)
                                .entity("Provider '" + model.getProvider().getName() + "' does not exist")
                                .build();
                    }
                }
            }
            
            if (model.getLicense() != null) {
                hasValidField = true;
            }
            
            if (model.getCapabilities() != null && !model.getCapabilities().isEmpty()) {
                hasValidField = true;
            }

            // Si no hay ningún campo válido para actualizar
            if (!hasValidField) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("At least one valid field must be provided for update")
                        .build();
            }

            model.setId(id);
            super.edit(model);
            return Response.ok(model).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
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
                        .entity("Model not found")
                        .build();
            }

            super.remove(existing);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
