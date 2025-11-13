package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * Entidad que representa un modelo de lenguaje (LLM)
 * 
 * TODO PERSONA A: Completar esta entidad con los siguientes campos:
 * - name (String, obligatorio)
 * - provider (String, obligatorio)
 * - summary (String, resumen corto 20-30 palabras)
 * - description (String, descripción larga)
 * - capabilities (List<String>, usar @ElementCollection)
 * - license (String)
 * - maxContextTokens (Integer)
 * - inputTypes (List<String>, usar @ElementCollection)
 * - outputTypes (List<String>, usar @ElementCollection)
 * - isPrivate (boolean)
 * - logoUrl (String)
 * - lastVersion (String)
 * - trainingDate (Date, usar @Temporal)
 * - lastUpdateDate (Date, usar @Temporal)
 * - versions (List<String>, opcional)
 * 
 * HINT: Mirar Topic.java y Comment.java como ejemplo
 */
@Entity
@XmlRootElement
@NamedQueries({
    // TODO PERSONA A: Añadir NamedQuery "Model.findAll" que devuelva todos los modelos ordenados por name
    // TODO PERSONA A: Añadir NamedQuery "Model.findByProvider" que filtre por provider (case-insensitive)
})
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // TODO PERSONA A: Añadir campo id con anotaciones @Id, @SequenceGenerator y @GeneratedValue
    // Usar el patrón: @SequenceGenerator(name="Model_Gen", allocationSize=1)
    
    // TODO PERSONA A: Añadir campo name (String, NOT NULL)
    
    // TODO PERSONA A: Añadir campo provider (String, NOT NULL)
    
    // TODO PERSONA A: Añadir campo summary (String, max 500 caracteres)
    
    // TODO PERSONA A: Añadir campo description (String, max 2000 caracteres)
    
    // TODO PERSONA A: Añadir campo capabilities (List<String>)
    // Usar @ElementCollection(fetch = FetchType.EAGER)
    // Usar @CollectionTable(name = "model_capabilities")
    
    // TODO PERSONA A: Añadir campo license (String)
    
    // TODO PERSONA A: Añadir campo maxContextTokens (Integer)
    
    // TODO PERSONA A: Añadir campo inputTypes (List<String>)
    
    // TODO PERSONA A: Añadir campo outputTypes (List<String>)
    
    // TODO PERSONA A: Añadir campo isPrivate (boolean, default false)
    
    // TODO PERSONA A: Añadir campo logoUrl (String)
    
    // TODO PERSONA A: Añadir campo lastVersion (String)
    
    // TODO PERSONA A: Añadir campo trainingDate (Date)
    // Usar @Temporal(TemporalType.DATE)
    
    // TODO PERSONA A: Añadir campo lastUpdateDate (Date)
    
    // TODO PERSONA A: Añadir campo versions (List<String>, opcional)

    // TODO PERSONA A: Crear constructor vacío
    // Inicializar las listas (capabilities, inputTypes, outputTypes, versions) como ArrayList vacíos

    // TODO PERSONA A: Crear getters y setters para TODOS los campos
    // Tip: En NetBeans: Clic derecho → Insert Code → Getter and Setter

    @Override
    public int hashCode() {
        // TODO PERSONA A: Implementar hashCode basado en el id
        return 0;
    }

    @Override
    public boolean equals(Object object) {
        // TODO PERSONA A: Implementar equals comparando ids
        // Ver ejemplo en Topic.java
        return false;
    }

    @Override
    public String toString() {
        // TODO PERSONA A: Retornar String con formato "Model[ id=X, name=Y, provider=Z ]"
        return "Model[ id=? ]";
    }
}
