package model.entities;

import authn.Credentials;
import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

/**
 * Entidad CUSTOMER
 * 
 * Clase para guardar los datos de los customers
 * 
 * Atributos:
 * - ID
 * - username (FK a entidad CREDENCIALES @OneToOne)
 * - ultimoModeloVisitado (FK a MODELO @ManyToOne)
 * 
 * Constructores:
 * - Constructor Vacío
 * - Constructor con Credencial
 * 
 * Métodos:
 * - getters y setters (normales)
 *  - getID()
 *  - setID()
 *  - getCredentials()
 *  - setCredentials()
 *  - getUltimoModeloVisitado()
 *  - setUltimoModeloVisitado()
 * 
 * - getters (especiales)
 *  - getUsername()
 *  - getUltimoModeloVisitadoId()
 * 
 * - otros
 *  - hashCode()
 *  - equals()
 *  - toString()
 * 
 * Funcionamiento:
 * 1) CREACIÓN de CUSTOMERS
 *      En install.jsp
 *      A) Se crean Credentials
 *              "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'sob', 'sob')",
 *              "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'demo', 'demo')",
 *      
 *      B) Se crean Modelos y el resto de datos necesarios
 *              // Modelo privado 1: GPT-4 (OpenAI)
 *              "INSERT INTO " + schema + ".MODEL VALUES (NEXT VALUE FOR MODEL_GEN, 'GPT-4.1-mini', 'OpenAI', 'Modelo optimizado para conversaciones rápidas con costes reducidos', 'GPT-4.1-mini es la versión compacta diseñada para aplicaciones rápidas', 'Custom', 32768, true, 'https://openai.com/favicon.ico', '2025-06-10', '2024-07-01', '2025-07-01')",
 *              
 *              // Modelo privado 2: Claude (Anthropic)
 *              "INSERT INTO " + schema + ".MODEL VALUES (NEXT VALUE FOR MODEL_GEN, 'Claude 3.5 Sonnet', 'Anthropic', 'Modelo avanzado con capacidades superiores en razonamiento y código', 'Claude 3.5 Sonnet combina velocidad y precisión en tareas complejas', 'Custom', 200000, true, 'https://anthropic.com/favicon.ico', '2025-08-15', '2024-09-01', '2025-08-15')",
 *              
 *              // Modelo público 1: Mistral (Mistral AI)
 *              "INSERT INTO " + schema + ".MODEL VALUES (NEXT VALUE FOR MODEL_GEN, 'Mistral Large 2', 'Mistral AI', 'Modelo open-source de alto rendimiento con licencia Apache 2.0', 'Mistral Large 2 ofrece rendimiento competitivo completamente abierto', 'Apache 2.0', 128000, false, 'https://mistral.ai/favicon.ico', '2025-05-20', '2024-06-01', '2025-05-20')",
 *              
 *              // Modelo público 2: LLaMA (Meta)
 *              "INSERT INTO " + schema + ".MODEL VALUES (NEXT VALUE FOR MODEL_GEN, 'LLaMA 3.1', 'Meta', 'Modelo open-source optimizado para multilingüe y razonamiento', 'LLaMA 3.1 con mejoras en capacidades multilingües y razonamiento', 'Permissive Open Source', 128000, false, 'https://llama.meta.com/favicon.ico', '2025-04-30', '2024-05-15', '2025-04-30')",
 *      
 *      C) Insertamos Customers (vinculados a Credentials y a Modelos)
 *              "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN, 1, 3)",  // sob vio Mistral
 *              "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN, 2, NULL)"  // demo no ha visto nada todavía
 * 
 * Al acceder no hace falta identificarse como un Customer
 * curl GET http://localhost:8080/practica-sob/rest/api/v1/models/1
 * (No puede hacer operaciones @Secured)
 * 
 * Si se quiere acceder a opciones marcadas con @Secured hay que identificarse con Credentials
 * curl -u sob:sob GET http://localhost:8080/practica-sob/rest/api/v1/models/1
 * 
 * Un Customer solo existe si hay un Credentials (FK)
 * 
 * 2) ACCEDER a datos de CUSTUMER
 * 
 * Como son FK (tanto el modelo como el username) hay que hacer la llamada a la otra clase
 * 
 * 3) ULTIMO MODELO VISITADO
 * 
 * Consultar el ultimo modelo visitado es facil
 * Si accedemos sin identificar obviamente no se guardará el ultimo visitado (se ha accedido como visitante)
 * Si accedemos como auth sí que se puede guardar
 * 
 * 4) QUERYS ya definidas
 * 
 *  A) Obtener todos los customers
 *  B) Encontrar usuario por username (hay que consultar la FK que apunta a la clase Credentials)
 * 
 */


@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(
        name = "Customer.findAll",
        query = "SELECT c FROM Customer c"
    ),
    @NamedQuery(
        name = "Customer.findByUsername",
        query = "SELECT c FROM Customer c WHERE c.credentials.username = :username"
    )
})
public class Customer implements Serializable {
    private static final long serialVersionUID = 1L; // Ya venía en las dos entidades de ejmplo (Comment.java y Topic.java)
    
    // Campo ID del Customer
    @Id
    @SequenceGenerator(name = "Customer_Gen", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Customer_Gen") 
    private Long id;

    // Campo que apunta a las credenciales 
    // One to One porque un Customer solo tiene una Credencial
    @OneToOne
    @JoinColumn(name = "credentials_id", unique = true, nullable = false)
    private Credentials credentials;
    
    // Campo que apunta al ultimo modelo visitado por el customer
    // Many to One porque muchos customers pueden ver el mismo modelo
    @ManyToOne
    @JoinColumn(name = "ultimo_modelo_visitado_id")
    private Model ultimoModeloVisitado;
    
    // Campo email del customer
    @Email
    private String email;

    // Constructor vacío (requerido por JPA)
    public Customer() {
    }
    
    // Constructor con Credentials
    public Customer(Credentials credentials) {
        this.credentials = credentials;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    @XmlTransient  // Si lo quitamos se enviarán todos los datos del modelo no solo el id del modelo
    public Model getUltimoModeloVisitado() {
        return ultimoModeloVisitado;
    }

    public void setUltimoModeloVisitado(Model ultimoModeloVisitado) {
        this.ultimoModeloVisitado = ultimoModeloVisitado;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        if (credentials != null)
            return credentials.getUsername();
        else
            return null;
    }
    
    public Long getUltimoModeloVisitadoId() {
        if (ultimoModeloVisitado != null)
            return ultimoModeloVisitado.getId();
        else
            return null;
    }

    // La interfaz Serializable necesita este método
    @Override
    public int hashCode() {
        int hash;
        if (id != null)
            hash = id.hashCode();
        else
            hash = 0;
        return hash;
    }

    // La interfaz Serializable necesita este método
    @Override
    public boolean equals(Object object) {

        // Revisar si de verdad es un Customer lo que nos pasan por parametro
        if (!(object instanceof Customer)) {
            return false;
        }
        Customer other = (Customer) object;
        
        // Comparar IDs
        if ((this.id == null && other.id != null) || 
            (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;

    }

    // Muestra el texto del customer
    // ej. Customer[ id=1, username=sob ]
    @Override
    public String toString() {
        return "Customer[ id=" + id + ", username=" + getUsername() + " ]";
    }

}
