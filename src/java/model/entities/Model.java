package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Model.findAll", query = "SELECT m FROM Model m ORDER BY m.name")
})
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name = "Model_Gen", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Model_Gen")
    private Long id;
    
    @NotNull
    private String name;
    
    @NotNull
    private String provider;
    
    private String summary;
    private String description;
    private String license;
    private Integer maxContextTokens;
    private boolean isPrivate;
    private String logoUrl;
    private String lastVersion;
    
    @Temporal(TemporalType.DATE)
    private Date trainingDate;
    
    @Temporal(TemporalType.DATE)
    private Date lastUpdateDate;

    public Model() {
    }

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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
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

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Integer getMaxContextTokens() {
        return maxContextTokens;
    }

    public void setMaxContextTokens(Integer maxContextTokens) {
        this.maxContextTokens = maxContextTokens;
    }

    public boolean isIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLastVersion() {
        return lastVersion;
    }

    public void setLastVersion(String lastVersion) {
        this.lastVersion = lastVersion;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Model)) {
            return false;
        }
        Model other = (Model) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Model[ id=" + id + ", name=" + name + ", provider=" + provider + " ]";
    }
}
