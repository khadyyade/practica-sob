package authn;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import model.entities.Customer;
import model.entities.Model;

import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * Inicializador de datos para la aplicación
 * Crea el usuario de prueba sob/sob y algunos modelos de ejemplo
 * 
 * TODO AMBOS: Completar los métodos para insertar datos de prueba
 * 
 * Este singleton se ejecuta automáticamente al arrancar la aplicación (@Startup)
 * y llama al método init() una sola vez (@PostConstruct)
 */
@Singleton
@Startup
public class DataInitializer {

    @PersistenceContext(unitName = "Homework1PU")
    private EntityManager em;

    @PostConstruct
    public void init() {
        try {
            createTestUser();
            createSampleModels();
            createSampleCustomers();
            System.out.println("✓ DataInitializer: Datos de prueba creados correctamente");
        } catch (Exception e) {
            System.err.println("✗ Error al inicializar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * TODO AMBOS: Crear el usuario de prueba sob/sob
     * 
     * Pasos:
     * 1. Intentar buscar usuario existente con:
     *    em.createNamedQuery("Credentials.findUser", Credentials.class)
     *      .setParameter("username", "sob")
     *      .getSingleResult();
     * 2. Si existe (no lanza excepción) → imprimir "Usuario 'sob' ya existe"
     * 3. Si NO existe (lanza excepción) → crear:
     *    Credentials credentials = new Credentials();
     *    credentials.setUsername("sob");
     *    credentials.setPassword("sob");
     *    em.persist(credentials);
     *    Imprimir "✓ Usuario de prueba creado: sob/sob"
     */
    private void createTestUser() {
        // TODO AMBOS: Implementar creación de usuario sob/sob
        System.out.println("  TODO: Crear usuario sob/sob");
    }

    /**
     * TODO PERSONA A: Crear 3-4 modelos de ejemplo
     * 
     * Pasos:
     * 1. Comprobar si ya hay modelos:
     *    Long count = em.createQuery("SELECT COUNT(m) FROM Model m", Long.class).getSingleResult();
     *    Si count > 0 → return (ya hay datos)
     * 
     * 2. Crear al menos 2 modelos privados (isPrivate=true) y 2 públicos (isPrivate=false)
     * 
     * Ejemplo de modelo:
     *    Model gpt4 = new Model();
     *    gpt4.setName("GPT-4.1-mini");
     *    gpt4.setProvider("OpenAI");
     *    gpt4.setSummary("Modelo optimizado para conversaciones rápidas...");
     *    gpt4.setCapabilities(Arrays.asList("chat-completion", "code-generation"));
     *    gpt4.setLicense("Custom");
     *    gpt4.setMaxContextTokens(32768);
     *    gpt4.setPrivate(true);
     *    gpt4.setLogoUrl("https://openai.com/favicon.ico");
     *    // ... más campos
     *    em.persist(gpt4);
     * 
     * Sugerencias de modelos:
     * - GPT-4 (OpenAI, privado)
     * - Claude (Anthropic, privado)
     * - Mistral (Mistral AI, público, Apache 2.0)
     * - LLaMA (Meta, público)
     * 
     * 3. Imprimir mensaje de confirmación
     */
    private void createSampleModels() {
        // TODO PERSONA A: Implementar creación de modelos de ejemplo
        System.out.println("  TODO: Crear modelos de ejemplo");
    }

    /**
     * TODO PERSONA B: Crear 2 clientes de ejemplo
     * 
     * Pasos:
     * 1. Comprobar si ya hay clientes:
     *    Long count = em.createQuery("SELECT COUNT(c) FROM Customer c", Long.class).getSingleResult();
     *    Si count > 0 → return
     * 
     * 2. Crear al menos 2 clientes:
     *    Customer customer1 = new Customer();
     *    customer1.setUsername("sob");
     *    customer1.setDisplayName("Usuario SOB de Prueba");
     *    customer1.setEmail("sob@urv.cat");
     *    em.persist(customer1);
     * 
     *    Customer customer2 = new Customer();
     *    customer2.setUsername("demo");
     *    customer2.setDisplayName("Usuario Demo");
     *    customer2.setEmail("demo@urv.cat");
     *    em.persist(customer2);
     * 
     * 3. Imprimir mensaje de confirmación
     */
    private void createSampleCustomers() {
        // TODO PERSONA B: Implementar creación de clientes de ejemplo
        System.out.println("  TODO: Crear clientes de ejemplo");
    }
}
