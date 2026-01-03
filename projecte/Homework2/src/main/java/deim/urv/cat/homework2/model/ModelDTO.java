package deim.urv.cat.homework2.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Data Transfer Object para representar un modelo de IA.
 * 
 * RESPONSABILIDADES:
 * - Representar la estructura JSON/XML devuelta por la API REST de Homework1
 * - NO es una entidad JPA, solo un POJO para transferencia de datos
 * - Puede incluir información básica (vista pública) o completa (vista privada)
 * 
 * ESTRUCTURA DE LA ENTIDAD Model EN HOMEWORK1:
 * - id (Long)
 * - name (String)
 * - provider (Provider) → ProviderDTO
 * - summary (String)
 * - description (String)
 * - capabilities (List<Capability>) → List<CapabilityDTO>
 * - license (License) → LicenseDTO
 * - isPrivate (boolean)
 * - trainingDate (Date)
 * - lastUpdateDate (Date)
 * - version (String)
 * 
 */
public class ModelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String summary;
    private String description;
    private boolean isPrivate;
    private String version;
    private Date trainingDate;
    private Date lastUpdateDate;
    
    // Relaciones (se cargan según si es vista pública o privada)
    private ProviderDTO provider;
    private LicenseDTO license;
    private List<CapabilityDTO> capabilities;

    // ==================== CONSTRUCTORES ====================

    public ModelDTO() {
    }

    public ModelDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // ==================== GETTERS Y SETTERS ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getTrainingDate() {
        return trainingDate;
    }

    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public ProviderDTO getProvider() {
        return provider;
    }

    public void setProvider(ProviderDTO provider) {
        this.provider = provider;
    }

    public LicenseDTO getLicense() {
        return license;
    }

    public void setLicense(LicenseDTO license) {
        this.license = license;
    }

    public List<CapabilityDTO> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<CapabilityDTO> capabilities) {
        this.capabilities = capabilities;
    }

    // ==================== CLASES INTERNAS PARA DTOs ANIDADOS ====================

    /**
     * DTO para Provider (proveedor del modelo)
     */
    public static class ProviderDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private Long id;
        private String name;

        public ProviderDTO() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * DTO para License (licencia del modelo)
     */
    public static class LicenseDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private Long id;
        private String name;

        public LicenseDTO() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * DTO para Capability (capacidad/habilidad del modelo)
     */
    public static class CapabilityDTO implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private Long id;
        private String name;

        public CapabilityDTO() {}

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
