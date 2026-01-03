package deim.urv.cat.homework2.controller;

import deim.urv.cat.homework2.model.ModelListForm;
import deim.urv.cat.homework2.model.ModelDTO;
import deim.urv.cat.homework2.service.ModelService;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import java.util.List;
import java.util.ArrayList;

/**
 * Controlador principal para el listado de modelos.
 * 
 * RESPONSABILIDADES:
 * - Mostrar página principal con listado de modelos
 * - Aplicar filtros de búsqueda (topic, capability, maxPrice)
 * - Manejo de paginación (opcional)
 * 
 * ASIGNADO A: Persona A
 */
@Controller
@Path("/")
public class HomeController {

    @Inject
    private Models models;
    
    @Inject
    private ModelService modelService;
    
    @Inject
    private ModelListForm filterForm;

    /**
     * GET / 
     * Muestra el listado de modelos aplicando filtros opcionales.
     * 
     * Query params opcionales (según API de Homework1):
     *   - capability: Puede aparecer 1 o 2 veces (capability=NLP&capability=Vision)
     *   - provider: Nombre del proveedor (provider=OpenAI)
     */
    @GET
    public String showHomePage(
            @QueryParam("capability") List<String> capabilities,
            @QueryParam("provider") String provider) {
        
        System.out.println("========== HomeController.showHomePage() EJECUTADO ==========");
        System.out.println("Capabilities recibidas: " + capabilities);
        System.out.println("Provider recibido: " + provider);
        
        // Actualizar el formulario de filtros con los parámetros recibidos
        if (capabilities != null && !capabilities.isEmpty()) {
            filterForm.setCapabilities(capabilities);
        }
        if (provider != null && !provider.trim().isEmpty()) {
            filterForm.setProvider(provider);
        }
        
        // Llamar a modelService.getModels(filterForm) para obtener la lista filtrada
        try {
            System.out.println("Llamando a modelService.getModels()...");
            List<ModelDTO> modelList = modelService.getModels(filterForm);
            System.out.println("Modelos obtenidos: " + (modelList != null ? modelList.size() : "null"));
            
            // Añadir la lista de modelos al modelo MVC
            models.put("models", modelList);
            
            // Añadir el formulario de filtros al modelo para pre-rellenar el formulario
            models.put("filters", filterForm);
            
        } catch (Exception e) {
            System.err.println("ERROR en HomeController: " + e.getMessage());
            e.printStackTrace();
            // Si falla, mostrar mensaje de error y lista vacía
            models.put("error", "Error al cargar los modelos: " + e.getMessage());
            models.put("models", new ArrayList<>());
            models.put("filters", filterForm);
        }
        
        System.out.println("Retornando vista index.jsp");
        // Retornar la vista JSP correspondiente
        return "index.jsp";
    }

    /**
     * GET /about (opcional)
     * Página "Acerca de" o información adicional.
     */
    @GET
    @Path("about")
    public String showAboutPage() {
        // TODO: Implementar si el enunciado lo requiere
        return "about.jsp";
    }
}
