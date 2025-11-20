package model.entities;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.json.bind.annotation.JsonbTransient;

@Entity
@XmlRootElement
public class License implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name="License_Gen", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "License_Gen") 
    private Long id;
    private String name;
    
    @jakarta.xml.bind.annotation.XmlTransient
    @OneToMany(mappedBy = "license", fetch = FetchType.LAZY)
    private List<Model> models;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @XmlTransient
    @JsonbTransient
    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    
}
