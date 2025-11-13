# 📚 Ejemplos de Código - Referencia Rápida

Este documento contiene ejemplos de código que podéis consultar mientras completáis los TODOs.

---

## 🔹 Ejemplo: Campo con anotaciones JPA

```java
// Campo simple
private String name;

// Campo obligatorio (NOT NULL)
@NotNull(message = "Name cannot be null")
@Column(nullable = false)
private String name;

// Campo con longitud máxima
@Column(length = 500)
private String summary;

// Campo único
@Column(unique = true)
private String username;

// Campo booleano con valor por defecto
@Column(nullable = false)
private boolean isPrivate = false;
```

---

## 🔹 Ejemplo: Lista con @ElementCollection

```java
// Lista de Strings almacenada en tabla separada
@ElementCollection(fetch = FetchType.EAGER)
@CollectionTable(name = "model_capabilities")
private List<String> capabilities;

// En el constructor, inicializar como ArrayList vacío:
public Model() {
    this.capabilities = new ArrayList<>();
    this.inputTypes = new ArrayList<>();
}
```

---

## 🔹 Ejemplo: Campo Date con @Temporal

```java
import java.util.Date;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Temporal(TemporalType.DATE)
private Date trainingDate;

// Para crear fechas en DataInitializer:
import java.text.SimpleDateFormat;

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
model.setTrainingDate(sdf.parse("2024-07-01"));
```

---

## 🔹 Ejemplo: Relación @ManyToOne

```java
// En Customer.java - un cliente puede ver muchos modelos, pero solo guardamos el último
@ManyToOne
private Model lastViewedModel;

// Para evitar serialización circular en JSON/XML:
import jakarta.xml.bind.annotation.XmlTransient;

@XmlTransient
public Model getLastViewedModel() {
    return lastViewedModel;
}
```

---

## 🔹 Ejemplo: NamedQueries

```java
@NamedQueries({
    @NamedQuery(
        name = "Model.findAll",
        query = "SELECT m FROM Model m ORDER BY m.name"
    ),
    @NamedQuery(
        name = "Model.findByProvider",
        query = "SELECT m FROM Model m WHERE LOWER(m.provider) = LOWER(:provider) ORDER BY m.name"
    )
})
public class Model implements Serializable {
    // ...
}

// Uso en el servicio REST:
List<Model> models = em.createNamedQuery("Model.findAll", Model.class)
    .getResultList();

// Con parámetros:
List<Model> models = em.createNamedQuery("Model.findByProvider", Model.class)
    .setParameter("provider", "OpenAI")
    .getResultList();
```

---

## 🔹 Ejemplo: Construir JPQL dinámico

```java
// Para filtrar por capabilities (MEMBER OF)
StringBuilder jpql = new StringBuilder("SELECT m FROM Model m WHERE ");

// Primera capability
jpql.append(":capability0 MEMBER OF m.capabilities");

// Si hay segunda capability
if (capabilities.size() > 1) {
    jpql.append(" AND :capability1 MEMBER OF m.capabilities");
}

// Si hay provider
if (provider != null) {
    jpql.append(" AND LOWER(m.provider) = LOWER(:provider)");
}

jpql.append(" ORDER BY m.name");

// Crear query
TypedQuery<Model> query = em.createQuery(jpql.toString(), Model.class);

// Asignar parámetros
query.setParameter("capability0", capabilities.get(0));
if (capabilities.size() > 1) {
    query.setParameter("capability1", capabilities.get(1));
}
if (provider != null) {
    query.setParameter("provider", provider);
}

// Ejecutar
List<Model> models = query.getResultList();
```

---

## 🔹 Ejemplo: Validaciones en REST

```java
// Validar campo obligatorio
if (model.getName() == null || model.getName().trim().isEmpty()) {
    return Response.status(Response.Status.BAD_REQUEST)
            .entity("{\"error\": \"Model name is required\"}")
            .build();
}

// Validar valor numérico
if (model.getMaxContextTokens() != null && model.getMaxContextTokens() <= 0) {
    return Response.status(Response.Status.BAD_REQUEST)
            .entity("{\"error\": \"maxContextTokens must be positive\"}")
            .build();
}

// Validar que no haya más de 2 capabilities
if (capabilities.size() > 2) {
    return Response.status(Response.Status.BAD_REQUEST)
            .entity("{\"error\": \"Maximum 2 capabilities allowed\"}")
            .build();
}
```

---

## 🔹 Ejemplo: Respuestas HTTP

```java
// 200 OK con entidad
return Response.ok(model).build();

// 201 Created con Location header
URI location = uriInfo.getAbsolutePathBuilder()
        .path(model.getId().toString())
        .build();
return Response.created(location)
        .entity(model)
        .build();

// 204 No Content (para DELETE exitoso)
return Response.noContent().build();

// 400 Bad Request
return Response.status(Response.Status.BAD_REQUEST)
        .entity("{\"error\": \"Mensaje de error\"}")
        .build();

// 401 Unauthorized (sin autenticación)
return Response.status(Response.Status.UNAUTHORIZED)
        .entity("{\"error\": \"Authentication required\"}")
        .build();

// 404 Not Found
return Response.status(Response.Status.NOT_FOUND)
        .entity("{\"error\": \"Model not found\"}")
        .build();

// 500 Internal Server Error
return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity("{\"error\": \"" + e.getMessage() + "\"}")
        .build();
```

---

## 🔹 Ejemplo: Construir JSON manualmente (HATEOAS)

```java
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

// Construir objeto JSON
JsonObjectBuilder builder = Json.createObjectBuilder()
        .add("id", customer.getId())
        .add("username", customer.getUsername());

// Añadir campos opcionales
if (customer.getDisplayName() != null) {
    builder.add("displayName", customer.getDisplayName());
}

if (customer.getEmail() != null) {
    builder.add("email", customer.getEmail());
}

// Añadir objeto anidado (links)
if (customer.getLastViewedModelId() != null) {
    JsonObject links = Json.createObjectBuilder()
            .add("model", "/models/" + customer.getLastViewedModelId())
            .build();
    builder.add("links", links);
}

// Construir JSON final
JsonObject response = builder.build();

// Devolver respuesta
return Response.ok(response).build();
```

Resultado JSON:
```json
{
  "id": 1,
  "username": "sob",
  "displayName": "Usuario SOB de Prueba",
  "email": "sob@urv.cat",
  "links": {
    "model": "/models/3"
  }
}
```

---

## 🔹 Ejemplo: Try-Catch en NamedQuery

```java
// Buscar usuario existente
try {
    Credentials existing = em.createNamedQuery("Credentials.findUser", Credentials.class)
        .setParameter("username", "sob")
        .getSingleResult();
    
    // Si llega aquí, el usuario existe
    System.out.println("Usuario 'sob' ya existe");
    
} catch (NoResultException e) {
    // No existe, crearlo
    Credentials credentials = new Credentials();
    credentials.setUsername("sob");
    credentials.setPassword("sob");
    em.persist(credentials);
    System.out.println("✓ Usuario creado");
}
```

---

## 🔹 Ejemplo: Crear entidad y persistir

```java
// Crear modelo
Model model = new Model();
model.setName("GPT-4.1-mini");
model.setProvider("OpenAI");
model.setSummary("Modelo optimizado...");
model.setCapabilities(Arrays.asList("chat-completion", "code-generation"));
model.setLicense("Custom");
model.setMaxContextTokens(32768);
model.setInputTypes(Arrays.asList("text"));
model.setOutputTypes(Arrays.asList("text"));
model.setPrivate(true);
model.setLogoUrl("https://openai.com/favicon.ico");
model.setLastVersion("2025-06-10");

// Persistir
em.persist(model);
```

---

## 🔹 Ejemplo: Getters y Setters (NetBeans)

En NetBeans, para generar getters/setters automáticamente:

1. **Clic derecho** en el editor → **Insert Code** (o `Alt+Insert`)
2. Seleccionar **Getter and Setter**
3. Marcar todos los campos
4. Clic en **Generate**

Ejemplo generado:
```java
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
```

---

## 🔹 Ejemplo: hashCode() y equals()

```java
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
```

---

## 🔹 Ejemplo: toString()

```java
@Override
public String toString() {
    return "Model[ id=" + id + ", name=" + name + ", provider=" + provider + " ]";
}
```

---

## 🔹 Ejemplo: Actualizar entidad (PUT)

```java
// Buscar entidad existente
Customer existing = super.find(id);

if (existing == null) {
    return Response.status(Response.Status.NOT_FOUND)
            .entity("{\"error\": \"Customer not found\"}")
            .build();
}

// Actualizar SOLO campos que vengan en la petición
if (customer.getDisplayName() != null) {
    existing.setDisplayName(customer.getDisplayName());
}

if (customer.getEmail() != null) {
    existing.setEmail(customer.getEmail());
}

// Persistir cambios
super.edit(existing);

return Response.ok(existing).build();
```

---

## 🔹 Ejemplo completo: DataInitializer con try-catch

```java
private void createTestUser() {
    try {
        // Intentar buscar
        em.createNamedQuery("Credentials.findUser", Credentials.class)
            .setParameter("username", "sob")
            .getSingleResult();
        
        System.out.println("  Usuario 'sob' ya existe");
        
    } catch (Exception e) {
        // No existe, crearlo
        Credentials credentials = new Credentials();
        credentials.setUsername("sob");
        credentials.setPassword("sob");
        em.persist(credentials);
        System.out.println("  ✓ Usuario de prueba creado: sob/sob");
    }
}
```

---

## 🔹 Imports comunes que necesitaréis

```java
// Para entidades JPA
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

// Para servicios REST
import jakarta.ejb.Stateless;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

// Para autenticación
import authn.Secured;

// Para listas
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

// Para JSON manual
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

// Para fechas
import java.util.Date;
import java.text.SimpleDateFormat;

// Para DataInitializer
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
```

---

## 🎯 Consejos Generales

1. **Compilar frecuentemente**: Después de cada cambio, hacer **Clean and Build** en NetBeans
2. **Ver errores**: La pestaña **Output** muestra errores de compilación
3. **Debugging**: Usar `System.out.println()` para debug
4. **Logs del servidor**: Ver pestaña **GlassFish Server** en NetBeans para logs en tiempo real
5. **Probar con curl**: Usar comandos del README.md para probar endpoints

---

**¡Consultad este documento cuando tengáis dudas sobre sintaxis!** 🚀
