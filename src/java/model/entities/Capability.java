package model.entities;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.json.bind.annotation.JsonbTransient;

@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Capability.findAll", query = "SELECT c FROM Capability c ORDER BY c.name"),
    @NamedQuery(name = "Capability.findByName", query = "SELECT c FROM Capability c WHERE LOWER(c.name) = LOWER(:name)")
})
public class Capability implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "Capability_Gen", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Capability_Gen")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    
    @jakarta.xml.bind.annotation.XmlTransient
    @ManyToMany(mappedBy = "capabilities", fetch = FetchType.LAZY)
    private List<Model> models;

    //Constructor vacio
    public Capability() {
    }

    public Capability(String name) {
        this.name = name;
    }

    //Setters y getters
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

    @XmlTransient
    @JsonbTransient
    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Capability)) {
            return false;
        }
        Capability other = (Capability) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Capability[ id=" + id + ", name=" + name + " ]";
    }
}
