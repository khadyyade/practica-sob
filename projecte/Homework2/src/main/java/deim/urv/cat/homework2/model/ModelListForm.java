package deim.urv.cat.homework2.model;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean de formulario para los filtros de búsqueda de modelos.
 * 
 * RESPONSABILIDADES:
 * - Almacenar los criterios de filtrado según la API REST de Homework1
 * - Ser utilizado por HomeController para aplicar filtros
 * - Pre-rellenar el formulario de búsqueda en la vista
 * 
 * FILTROS DISPONIBLES EN LA API (GET /models):
 * - capability: Lista de capabilities (máximo 2)
 * - provider: Nombre del proveedor
 * 
 * NOTA: La API NO soporta filtro por precio (maxPrice), solo capabilities y provider.
 * 
 */
@Named
@RequestScoped
public class ModelListForm implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<String> capabilities;  // Máximo 2 capabilities
    private String provider;

    // ==================== CONSTRUCTORES ====================

    public ModelListForm() {
        this.capabilities = new ArrayList<>();
    }

    // ==================== MÉTODOS DE UTILIDAD ====================

    /**
     * Verifica si hay algún filtro activo.
     * 
     * @return true si hay filtros, false si está vacío
     */
    public boolean hasFilters() {
        return (capabilities != null && !capabilities.isEmpty()) ||
               (provider != null && !provider.trim().isEmpty());
    }

    /**
     * Construye el query string para la API REST.
     * 
     * Formato esperado:
     *   ?capability=NLP&capability=Vision&provider=OpenAI
     * 
     * @return Query string (sin el "?" inicial)
     */
    public String toQueryString() {
        StringBuilder query = new StringBuilder();
        
        // Añadir capabilities (se pueden repetir)
        if (capabilities != null && !capabilities.isEmpty()) {
            for (String cap : capabilities) {
                if (cap != null && !cap.trim().isEmpty()) {
                    if (query.length() > 0) {
                        query.append("&");
                    }
                    query.append("capability=").append(cap.trim());
                }
            }
        }
        
        // Añadir provider
        if (provider != null && !provider.trim().isEmpty()) {
            if (query.length() > 0) {
                query.append("&");
            }
            query.append("provider=").append(provider.trim());
        }
        
        return query.toString();
    }

    /**
     * Limpia todos los filtros.
     */
    public void clear() {
        this.capabilities.clear();
        this.provider = null;
    }

    // ==================== GETTERS Y SETTERS ====================

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }

    /**
     * Añade una capability a la lista (máximo 2).
     * 
     * @param capability Capability a añadir
     * @return true si se añadió, false si ya hay 2
     */
    public boolean addCapability(String capability) {
        if (capabilities.size() >= 2) {
            return false; // Máximo 2 capabilities
        }
        capabilities.add(capability);
        return true;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
