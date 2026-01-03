package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.ModelDTO;
import deim.urv.cat.homework2.model.UserSession;
import deim.urv.cat.homework2.service.ModelService;
import deim.urv.cat.homework2.service.CommentService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 * Controlador para mostrar el detalle de un modelo.
 * 
 * RESPONSABILIDADES:
 * - Vista pública: información básica del modelo (sin autenticación)
 * - Vista privada: información completa + licencias + comentarios (requiere autenticación)
 * - Redireccionar a login si intenta acceder a vista privada sin autenticación
 * - Guardar URL de retorno para volver después del login
 * 
 * ASIGNADO A: Persona A (vista pública) + Persona B (vista privada y redirección)
 */
@Controller
@Path("/model")
public class ModelDetailController {

    @Inject
    private Models models;
    
    @Inject
    private ModelService modelService;
    
    @Inject
    private CommentService commentService;
    
    @Inject
    private UserSession userSession;

    /**
     * GET /model/{id}
     * Vista pública del modelo (sin autenticación).
     */
    @GET
    @Path("{id}")
    public String showPublicDetail(@PathParam("id") Long modelId) {
        
        try {
            // Obtener el modelo por ID (sin autenticación, solo información pública)
            // Pasamos null como authHeader para obtener solo la información pública
            ModelDTO model = modelService.getModelById(modelId, null);
            
            // Manejar caso de modelo no encontrado
            if (model == null) {
                models.put("errorCode", "404");
                models.put("errorMessage", "El modelo solicitado no existe");
                return "Error404.jsp";
            }
            
            // Añadir el modelo al contexto MVC para la vista
            models.put("model", model);
            
            // Indicar que es vista pública (útil para la JSP)
            models.put("isPublicView", true);
            
        } catch (Exception e) {
            // En caso de error del servidor o de red
            models.put("errorCode", "500");
            models.put("errorMessage", "Error al cargar el modelo: " + e.getMessage());
            return "Error404.jsp";
        }
        
        // Retornar la vista pública del modelo
        return "modelDetail.jsp";
    }

    /**
     * GET /model/{id}/private
     * Vista privada del modelo (requiere autenticación).
     * Si el usuario no está autenticado, redirige a /login.
     */
    @GET
    @Path("{id}/private")
    public Response showPrivateDetail(@PathParam("id") Long modelId) {
        
        // TODO: Verificar si el usuario está autenticado
        // if (!userSession.isAuthenticated()) {
        //     // Guardar URL de retorno en la sesión
        //     userSession.setReturnUrl("/model/" + modelId + "/private");
        //     // Redirigir a login
        //     return Response.seeOther(URI.create("/login")).build();
        // }
        
        // TODO: Llamar a modelService.getPrivateModelDetails(modelId, userSession.getAuthHeader())
        // para obtener información completa (incluyendo licencias, provider, etc.)
        
        // TODO: Llamar a commentService.getComments(modelId, userSession.getAuthHeader())
        // para obtener los comentarios del modelo
        
        // TODO: Añadir modelo y comentarios al contexto MVC
        // models.put("model", model);
        // models.put("comments", comments);
        
        // TODO: Manejar errores HTTP (401 Unauthorized, 404 Not Found, 500 Server Error)
        
        // TODO: Retornar la vista privada
        // return Response.ok("privateDetail.jsp").build();
        
        return Response.ok("privateDetail.jsp").build();
    }

    /**
     * POST /model/{id}/comment (opcional)
     * Permite añadir un comentario al modelo (requiere autenticación).
     */
    // @POST
    // @Path("{id}/comment")
    // public Response addComment(@PathParam("id") Long modelId, @FormParam("text") String commentText) {
    //     TODO: Implementar si el enunciado lo requiere
    //     return Response.seeOther(URI.create("/model/" + modelId + "/private")).build();
    // }
}
