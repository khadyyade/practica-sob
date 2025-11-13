package model.entities;

import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * Entidad que representa un cliente/usuario registrado del catálogo
 * 
 * TODO PERSONA B: Completar esta entidad con los siguientes campos:
 * - username (String, obligatorio, único)
 * - displayName (String)
 * - email (String, validar con @Email)
 * - lastViewedModel (relación @ManyToOne con Model)
 * 
 * IMPORTANTE: NO añadir campo password aquí
 * Las contraseñas se gestionan en la entidad Credentials (authn/Credentials.java)
 * 
 * HINT: Mirar Comment.java como ejemplo de relación @ManyToOne
 */
@Entity
@XmlRootElement
@NamedQueries({
    // TODO PERSONA B: Añadir NamedQuery "Customer.findAll" que devuelva todos ordenados por username
    // TODO PERSONA B: Añadir NamedQuery "Customer.findByUsername" que filtre por username exacto
})
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // TODO PERSONA B: Añadir campo id con anotaciones @Id, @SequenceGenerator y @GeneratedValue
    
    // TODO PERSONA B: Añadir campo username (String, NOT NULL, UNIQUE)
    // Usar @Column(unique = true, nullable = false)
    
    // TODO PERSONA B: Añadir campo displayName (String)
    
    // TODO PERSONA B: Añadir campo email (String)
    // Usar @Email(message = "Email should be valid")
    
    // TODO PERSONA B: Añadir campo lastViewedModel (relación @ManyToOne con Model)
    // Usar @XmlTransient para evitar serialización circular

    // TODO PERSONA B: Crear constructor vacío
    
    // TODO PERSONA B: Crear constructor con username

    // TODO PERSONA B: Crear getters y setters para TODOS los campos
    
    /**
     * Método helper para obtener el ID del último modelo visto
     * Útil para construir el link HATEOAS en el servicio REST
     */
    public Long getLastViewedModelId() {
        // TODO PERSONA B: Retornar lastViewedModel.getId() si lastViewedModel no es null, sino null
        return null;
    }

    @Override
    public int hashCode() {
        // TODO PERSONA B: Implementar hashCode basado en el id
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        // TODO PERSONA B: Implementar equals comparando ids
        return false;
    }

    @Override
    public String toString() {
        // TODO PERSONA B: Retornar String con formato "Customer[ id=X, username=Y ]"
        return "Customer[ id=? ]";
    }
}
