package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement

public class Model implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @SequenceGenerator(name="Model_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Model_Gen") 
    private Long id;
    private String name;
    private Provider provider;
    private String summary;
    private String description;
    private List<Capability> capabilities;
    private License license;
    private boolean isPrivate;
    @Temporal(TemporalType.DATE)
    private Date trainingDate;
    @Temporal(TemporalType.DATE)
    private Date lastUpdateDate;
    private String version;

 //-------------------------------------Constructor--------------------------------- 
    public Model (){
        this.isPrivate = false;
    }
//--------------------------------Getters and Setters ------------------------------

//--ID------------------------------------------------------------------------------
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

//--Name----------------------------------------------------------------------------
    public String getName() {
        return name;
    }  
    public void setName(String name) {
        this.name = name;
    }

//--Provider-------------------------------------------------------------------------
    public Provider getProvider() {
        return provider;
    }
    public void setProvider(Provider provider) {
        this.provider = provider;
    }

//--Summary-------------------------------------------------------------------------
    public String getSummary() {
        return summary;
    }
    public void setSummary(String summary) {
        this.summary = summary;
    }

//--Description-------------------------------------------------------------------------
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

//--Capabilities-------------------------------------------------------------------------
    public List<Capability> getCapabilities() {
        return capabilities;
    }
    public void setCapabilities(List<Capability> capabilities) {
        this.capabilities = capabilities;
    }

//--License-------------------------------------------------------------------------   
    public License getLicense() {
        return license;
    }
    public void setLicense(License license) {
        this.license = license;
    }

//--IsPrivate-------------------------------------------------------------------------
    public boolean isIsPrivate() {
        return isPrivate;
    }
    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

//--TrainingDate-------------------------------------------------------------------------
    public Date getTrainingDate() {
        return trainingDate;
    }
    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }

//--LastUpdateDate-------------------------------------------------------------------------
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

//--Version-------------------------------------------------------------------------
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }

}
