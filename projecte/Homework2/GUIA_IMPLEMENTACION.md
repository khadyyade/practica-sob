# HOMEWORK2 - GUÍA COMPLETA DE IMPLEMENTACIÓN
## Práctica 2 de Sistemes Oberts - Jakarta MVC 2.0

---

## 📋 ÍNDICE
1. [Conceptos Fundamentales](#1-conceptos-fundamentales)
2. [Arquitectura del Sistema](#2-arquitectura-del-sistema)
3. [Organización del Código](#3-organización-del-código)
4. [División del Trabajo](#4-división-del-trabajo)
5. [Flujos Principales](#5-flujos-principales)
6. [Errores Comunes](#6-errores-comunes)
7. [Checklist de Implementación](#7-checklist-de-implementación)

---

## 1. CONCEPTOS FUNDAMENTALES

### 1.1 Diferencia entre Código Base y Enunciado

#### **CÓDIGO BASE (UserService / Ejemplo técnico)**
- ✅ Es un **ejemplo didáctico** de cómo usar Jakarta MVC 2.0
- ✅ Muestra **patrones de arquitectura**: Controllers, Services, DTOs
- ✅ Demuestra cómo llamar a una API REST desde MVC
- ✅ Es una **referencia técnica**, NO funcional
- ❌ **NO define** qué debe hacer la aplicación
- ❌ **NO especifica** las pantallas, flujos ni requisitos

#### **ENUNCIADO OFICIAL (PDF)**
- ✅ Define **QUÉ** debe hacer la aplicación
- ✅ Especifica **pantallas** obligatorias
- ✅ Describe **flujos de navegación**
- ✅ Establece **restricciones de acceso** (público/privado)
- ✅ Es la **fuente de verdad** para los requisitos
- ✅ Define el **comportamiento esperado**

### 1.2 Regla de Oro
> **El código base te enseña CÓMO construir.**  
> **El enunciado te dice QUÉ construir.**

---

## 2. ARQUITECTURA DEL SISTEMA

### 2.1 Separación de Responsabilidades

```
┌─────────────────────────────────────────────────────────────────┐
│                   NAVEGADOR (Cliente HTTP)                      │
└────────────────────┬────────────────────────────────────────────┘
                     │ HTML Forms, HTTP GET/POST
                     ▼
┌─────────────────────────────────────────────────────────────────┐
│                HOMEWORK2 (Jakarta MVC 2.0)                      │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │ CAPA: CONTROLLERS (Navegación y Lógica de Presentación) │  │
│  │ - HomeController: Listado de modelos                     │  │
│  │ - ModelDetailController: Detalle público/privado         │  │
│  │ - LoginController: Autenticación                         │  │
│  │ - ErrorController: Manejo de errores                     │  │
│  └───────────────┬───────────────────────────────────────────┘  │
│                  │                                               │
│  ┌───────────────▼───────────────────────────────────────────┐  │
│  │ CAPA: MODEL (Estado de la Aplicación - CDI Beans)       │  │
│  │ - UserSession: Sesión autenticada (@SessionScoped)       │  │
│  │ - ModelListForm: Filtros de búsqueda (@RequestScoped)   │  │
│  │ - DTOs: ModelDTO, CommentDTO, CustomerDTO (POJOs)       │  │
│  └───────────────┬───────────────────────────────────────────┘  │
│                  │                                               │
│  ┌───────────────▼───────────────────────────────────────────┐  │
│  │ CAPA: SERVICE (Clientes REST - @ApplicationScoped)      │  │
│  │ - ModelService: GET /models, /models/{id}                │  │
│  │ - CommentService: GET /comments, POST /comments          │  │
│  │ - CustomerService: Autenticación HTTP Basic              │  │
│  │ - RestClientHelper: HttpURLConnection, Authorization     │  │
│  └───────────────┬───────────────────────────────────────────┘  │
└──────────────────┼─────────────────────────────────────────────┘
                   │ HTTP REST API Calls (JSON/XML)
                   ▼
┌─────────────────────────────────────────────────────────────────┐
│           HOMEWORK1 (API REST - NO MODIFICAR)                   │
│  - Recursos REST: /models, /comments, /customers                │
│  - Autenticación: HTTP Basic (RESTRequestFilter)                │
│  - Persistencia: JPA + EntityManager + Derby DB                 │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 Comunicación entre Capas

**CORRECTO** ✅:
```
Usuario → Controller → Service → HTTP → Homework1 API
```

**INCORRECTO** ❌:
```
Usuario → Controller → JPA → Base de Datos  (NO USAR JPA EN HOMEWORK2)
Usuario → JSP → Lógica de negocio  (NO PONER LÓGICA EN VISTAS)
```

---

## 3. ORGANIZACIÓN DEL CÓDIGO

### 3.1 Estructura de Packages

```
src/main/java/
└── deim/urv/cat/homework2/
    ├── controller/
    │   ├── HomeController.java                (PERSONA A)
    │   ├── ModelDetailController.java         (PERSONA A + B)
    │   ├── LoginController.java               (PERSONA B)
    │   └── ErrorController.java               (PERSONA B)
    │
    ├── model/
    │   ├── UserSession.java                   (PERSONA B)
    │   ├── ModelListForm.java                 (PERSONA A)
    │   ├── ModelDTO.java                      (PERSONA A)
    │   ├── CommentDTO.java                    (PERSONA A)
    │   └── CustomerDTO.java                   (PERSONA B)
    │
    └── service/
        ├── ModelService.java                  (PERSONA A)
        ├── CommentService.java                (PERSONA B)
        ├── CustomerService.java               (PERSONA B)
        └── RestClientHelper.java              (PERSONA B, compartida)
```

### 3.2 Descripción de Clases

#### **CONTROLLERS (MVC Controllers)**
| Clase | Responsabilidad | Rutas |
|-------|----------------|-------|
| `HomeController` | Listado de modelos, aplicar filtros | `GET /` |
| `ModelDetailController` | Vista pública/privada de modelo | `GET /model/{id}`, `GET /model/{id}/private` |
| `LoginController` | Login, logout, validación | `GET /login`, `POST /login`, `GET /logout` |
| `ErrorController` | Páginas de error personalizadas | `GET /error` |

#### **MODEL (CDI Beans & DTOs)**
| Clase | Tipo | Scope | Responsabilidad |
|-------|------|-------|----------------|
| `UserSession` | Bean | `@SessionScoped` | Almacenar credenciales, estado autenticación |
| `ModelListForm` | Bean | `@RequestScoped` | Formulario de filtros de búsqueda |
| `ModelDTO` | DTO | - | Representación de un modelo (POJO) |
| `CommentDTO` | DTO | - | Representación de un comentario (POJO) |
| `CustomerDTO` | DTO | - | Representación de un cliente (POJO) |

#### **SERVICE (REST Clients)**
| Clase | Responsabilidad |
|-------|----------------|
| `ModelService` | Llamadas HTTP a `/models` (GET listado, GET detalle) |
| `CommentService` | Llamadas HTTP a `/comments` (GET, POST) |
| `CustomerService` | Autenticación contra `/customers` (HTTP Basic) |
| `RestClientHelper` | Utilidad HTTP (HttpURLConnection, headers, manejo errores) |

---

## 4. DIVISIÓN DEL TRABAJO

### 4.1 Persona A - Gestión de Modelos

**RESPONSABILIDADES:**
1. Listado de modelos con filtros
2. Vista pública de un modelo
3. DTOs de modelos y comentarios
4. Cliente REST para modelos

**CLASES ASIGNADAS:**
- ✅ `HomeController.java`
- ✅ `ModelDetailController.java` (parte pública)
- ✅ `ModelListForm.java`
- ✅ `ModelDTO.java`
- ✅ `CommentDTO.java`
- ✅ `ModelService.java`

**JSPs:**
- `index.jsp` (listado de modelos)
- `modelDetail.jsp` (vista pública)

**TAREAS:**
1. Implementar `HomeController.showHomePage()`:
   - Leer parámetros de filtro (topic, capability, maxPrice)
   - Llamar a `modelService.getModels(filterForm)`
   - Pasar lista de modelos a la vista
   
2. Implementar `ModelService.getModels()`:
   - Construir URL con query params
   - Hacer llamada HTTP GET usando `RestClientHelper`
   - Parsear JSON a `List<ModelDTO>`
   
3. Implementar `ModelDetailController.showPublicDetail()`:
   - Obtener modelo por ID
   - Renderizar vista pública
   
4. Completar atributos de `ModelDTO` según la API REST

---

### 4.2 Persona B - Autenticación y Seguridad

**RESPONSABILIDADES:**
1. Sistema de login/logout
2. Gestión de sesión autenticada
3. Vista privada de modelos (con redirección)
4. Clientes REST para comentarios y clientes

**CLASES ASIGNADAS:**
- ✅ `LoginController.java`
- ✅ `ModelDetailController.java` (parte privada)
- ✅ `ErrorController.java`
- ✅ `UserSession.java`
- ✅ `CustomerDTO.java`
- ✅ `CustomerService.java`
- ✅ `CommentService.java`
- ✅ `RestClientHelper.java`

**JSPs:**
- `login.jsp`
- `privateDetail.jsp`
- `Error404.jsp`

**TAREAS:**
1. Implementar `LoginController.processLogin()`:
   - Validar credenciales con `customerService.authenticate()`
   - Guardar en `UserSession` si válido
   - Redirigir a URL de retorno
   
2. Implementar `UserSession.getAuthHeader()`:
   - Generar header HTTP Basic (Base64)
   
3. Implementar `ModelDetailController.showPrivateDetail()`:
   - Verificar autenticación
   - Redirigir a login si no autenticado
   - Obtener detalles completos con autenticación
   
4. Implementar `RestClientHelper.get()` y `.post()`:
   - HttpURLConnection
   - Añadir header Authorization
   - Manejar códigos HTTP (200, 401, 404, 500)

---

### 4.3 Coordinación entre Persona A y B

**INTERFACES COMPARTIDAS:**
1. `RestClientHelper` - Persona B crea, Persona A usa
2. `UserSession` - Persona B crea, ambos usan
3. DTOs - Persona A crea `ModelDTO`, `CommentDTO`; Persona B crea `CustomerDTO`

**PUNTOS DE INTEGRACIÓN:**
- Persona A usa `RestClientHelper.get()` en `ModelService`
- Persona A consulta `UserSession.isAuthenticated()` en `ModelDetailController`
- Persona B usa `ModelDTO` en `ModelDetailController.showPrivateDetail()`

---

## 5. FLUJOS PRINCIPALES

### 5.1 Flujo 1: Listado de Modelos (Sin Autenticación)

```
1. Usuario accede a: http://localhost:8080/Homework2/

2. HomeController.showHomePage()
   ├─ Lee query params: ?topic=NLP&maxPrice=100
   ├─ Actualiza ModelListForm con los filtros
   └─ Llama a ModelService.getModels(filterForm)

3. ModelService.getModels()
   ├─ Construye URL: http://localhost:8080/Homework1/api/models?topic=NLP&maxPrice=100
   ├─ RestClientHelper.get(url, null) → Sin autenticación
   └─ Parsea JSON → List<ModelDTO>

4. Controller añade lista al modelo MVC
   └─ models.put("models", modelList)

5. Renderiza: index.jsp
   └─ <c:forEach items="${models}" var="model">
       <a href="/model/${model.id}">${model.name}</a>
     </c:forEach>
```

---

### 5.2 Flujo 2: Vista Pública de un Modelo

```
1. Usuario hace clic en un modelo → /model/123

2. ModelDetailController.showPublicDetail(123)
   └─ Llama a ModelService.getModelById(123)

3. ModelService.getModelById()
   ├─ URL: http://localhost:8080/Homework1/api/models/123
   ├─ RestClientHelper.get(url, null)
   └─ Parsea JSON → ModelDTO

4. Si modelo no existe (404) → Redirigir a /error?code=404
5. Si existe → Renderiza modelDetail.jsp
```

---

### 5.3 Flujo 3: Intento de Acceso a Detalle Privado (Sin Autenticación)

```
1. Usuario intenta acceder a: /model/123/private

2. ModelDetailController.showPrivateDetail(123)
   └─ Verifica: userSession.isAuthenticated()
   
3. Si NO autenticado:
   ├─ userSession.setReturnUrl("/model/123/private")
   └─ return Response.seeOther(URI.create("/login")).build()

4. Renderiza: login.jsp
   └─ Muestra formulario de login
```

---

### 5.4 Flujo 4: Login Exitoso y Retorno

```
1. Usuario envía formulario de login (POST /login)
   └─ username=john&password=secret

2. LoginController.processLogin()
   └─ Valida: customerService.authenticate("john", "secret")

3. CustomerService.authenticate()
   ├─ Genera authHeader: "Basic am9objpzZWNyZXQ="
   ├─ RestClientHelper.get("/customers/me", authHeader)
   └─ Si respuesta es 200 → true, si 401 → false

4. Si login exitoso:
   ├─ userSession.setUsername("john")
   ├─ userSession.setPassword("secret")
   ├─ userSession.setAuthenticated(true)
   ├─ returnUrl = userSession.getReturnUrl() → "/model/123/private"
   └─ return Response.seeOther(URI.create(returnUrl)).build()

5. Redirige a: /model/123/private (ahora autenticado)
```

---

### 5.5 Flujo 5: Vista Privada de un Modelo (Autenticado)

```
1. Usuario autenticado accede a: /model/123/private

2. ModelDetailController.showPrivateDetail(123)
   └─ Verifica: userSession.isAuthenticated() → true

3. Obtiene detalles completos:
   ├─ ModelService.getPrivateModelDetails(123, userSession.getAuthHeader())
   └─ CommentService.getComments(123, userSession.getAuthHeader())

4. ModelService.getPrivateModelDetails()
   ├─ URL: http://localhost:8080/Homework1/api/models/123
   ├─ RestClientHelper.get(url, "Basic am9objpzZWNyZXQ=")
   └─ Parsea JSON completo → ModelDTO (con licencias, provider, etc.)

5. CommentService.getComments()
   ├─ URL: http://localhost:8080/Homework1/api/comments?modelId=123
   ├─ RestClientHelper.get(url, authHeader)
   └─ Parsea JSON → List<CommentDTO>

6. Controller añade al modelo:
   ├─ models.put("model", model)
   └─ models.put("comments", comments)

7. Renderiza: privateDetail.jsp
```

---

## 6. ERRORES COMUNES A EVITAR

### ❌ ERROR 1: Usar JPA en Homework2
```java
// INCORRECTO - NO HACER ESTO
import jakarta.persistence.EntityManager;
import model.entities.Model; // Entidad de Homework1

@Inject
private EntityManager em;

public List<Model> getModels() {
    return em.createQuery("SELECT m FROM Model m").getResultList();
}
```

✅ **CORRECTO:**
```java
// Usar cliente HTTP
public List<ModelDTO> getModels() {
    String url = API_BASE_URL + "/models";
    String json = restClient.get(url, null);
    return parseJsonToModelList(json);
}
```

---

### ❌ ERROR 2: Compartir Entidades JPA entre Proyectos
```java
// INCORRECTO
import model.entities.Model; // De Homework1
```

✅ **CORRECTO:**
```java
// Crear DTO propio
public class ModelDTO implements Serializable {
    private Long id;
    private String name;
    // ...
}
```

---

### ❌ ERROR 3: Lógica de Negocio en JSP
```jsp
<!-- INCORRECTO -->
<%
    String user = request.getParameter("username");
    String pass = request.getParameter("password");
    if (authenticate(user, pass)) {
        session.setAttribute("user", user);
    }
%>
```

✅ **CORRECTO:**
```java
// LoginController.java
@POST
public Response processLogin(@FormParam("username") String username,
                             @FormParam("password") String password) {
    if (customerService.authenticate(username, password)) {
        userSession.setUsername(username);
        return Response.seeOther(URI.create("/")).build();
    }
    // ...
}
```

---

### ❌ ERROR 4: No Manejar Errores HTTP
```java
// INCORRECTO
public ModelDTO getModelById(Long id) {
    String json = restClient.get(API_BASE_URL + "/models/" + id, null);
    return parseJsonToModel(json); // ¿Y si devuelve 404?
}
```

✅ **CORRECTO:**
```java
public ModelDTO getModelById(Long id) {
    try {
        String json = restClient.get(API_BASE_URL + "/models/" + id, null);
        return parseJsonToModel(json);
    } catch (NotFoundException e) {
        return null; // O lanzar excepción personalizada
    }
}
```

---

### ❌ ERROR 5: Mezclar Responsabilidades
```java
// INCORRECTO - Controller haciendo llamadas HTTP directamente
@GET
@Path("/")
public String showHomePage() {
    HttpURLConnection conn = (HttpURLConnection) new URL(API_URL).openConnection();
    // ... código HTTP aquí
}
```

✅ **CORRECTO:**
```java
@GET
@Path("/")
public String showHomePage() {
    List<ModelDTO> models = modelService.getModels(filterForm);
    this.models.put("models", models);
    return "index.jsp";
}
```

---

## 7. CHECKLIST DE IMPLEMENTACIÓN

### 7.1 Persona A - Modelos

#### Fase 1: Setup
- [ ] Completar atributos de `ModelDTO` según la API REST de Homework1
- [ ] Completar atributos de `CommentDTO`
- [ ] Implementar `ModelListForm.toQueryString()`

#### Fase 2: Service Layer
- [ ] Implementar `ModelService.getModels()` con filtros
- [ ] Implementar parseo JSON → `List<ModelDTO>`
- [ ] Implementar `ModelService.getModelById()`
- [ ] Implementar `ModelService.getPrivateModelDetails()` (con auth)
- [ ] Manejar excepciones HTTP (404, 500)

#### Fase 3: Controller Layer
- [ ] Implementar `HomeController.showHomePage()`
- [ ] Leer query params y actualizar `ModelListForm`
- [ ] Pasar lista de modelos a la vista
- [ ] Implementar `ModelDetailController.showPublicDetail()`
- [ ] Manejar error 404 (modelo no encontrado)

#### Fase 4: Vistas
- [ ] Crear `index.jsp` con listado de modelos
- [ ] Añadir formulario de filtros
- [ ] Crear `modelDetail.jsp` con información pública

---

### 7.2 Persona B - Autenticación

#### Fase 1: Setup
- [ ] Completar `UserSession` con método `getAuthHeader()`
- [ ] Implementar `UserSession.clearSession()`
- [ ] Completar atributos de `CustomerDTO`

#### Fase 2: Service Layer (HTTP Client)
- [ ] Implementar `RestClientHelper.get()`:
  - [ ] HttpURLConnection
  - [ ] Añadir header Authorization
  - [ ] Leer respuesta HTTP
  - [ ] Manejar códigos 200, 401, 404, 500
- [ ] Implementar `RestClientHelper.post()`
- [ ] Crear excepciones personalizadas (`UnauthorizedException`, `NotFoundException`)

#### Fase 3: Service Layer (Clientes REST)
- [ ] Implementar `CustomerService.authenticate()`
- [ ] Implementar `CustomerService.getAuthenticatedCustomer()`
- [ ] Implementar `CommentService.getComments()`
- [ ] Implementar `CommentService.addComment()` (opcional)

#### Fase 4: Controller Layer
- [ ] Implementar `LoginController.showLoginForm()`
- [ ] Implementar `LoginController.processLogin()`:
  - [ ] Validar credenciales
  - [ ] Guardar en `UserSession`
  - [ ] Redirigir a URL de retorno
- [ ] Implementar `LoginController.logout()`
- [ ] Implementar `ModelDetailController.showPrivateDetail()`:
  - [ ] Verificar autenticación
  - [ ] Redirigir a login si no autenticado
  - [ ] Obtener detalles con auth
- [ ] Implementar `ErrorController.showError()`

#### Fase 5: Vistas
- [ ] Crear `login.jsp` con formulario
- [ ] Mostrar mensajes de error
- [ ] Crear `privateDetail.jsp` con detalles completos
- [ ] Actualizar `Error404.jsp`

---

### 7.3 Integración y Pruebas

#### Testing Manual
- [ ] **Flujo 1:** Listado sin filtros → `/`
- [ ] **Flujo 2:** Listado con filtros → `/?topic=NLP&maxPrice=100`
- [ ] **Flujo 3:** Vista pública → `/model/1`
- [ ] **Flujo 4:** Intento de acceso privado sin login → `/model/1/private` → Redirige a `/login`
- [ ] **Flujo 5:** Login exitoso → Redirige a URL guardada
- [ ] **Flujo 6:** Login fallido → Muestra error
- [ ] **Flujo 7:** Vista privada autenticado → Muestra licencias, comentarios
- [ ] **Flujo 8:** Logout → Limpia sesión, redirige a `/`

#### Manejo de Errores
- [ ] Modelo no encontrado (404) → Página de error
- [ ] Credenciales inválidas (401) → Mensaje en login
- [ ] Error del servidor (500) → Página de error genérica
- [ ] Timeout de red → Mensaje amigable

---

## 8. RECURSOS ADICIONALES

### 8.1 Configuración de la API REST (Config.properties)
```properties
# src/main/resources/Config.properties
api.base.url=http://localhost:8080/Homework1/api
```

### 8.2 Ejemplo de Parseo JSON (usando Jakarta JSON-B)
```java
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

private List<ModelDTO> parseJsonToModelList(String json) {
    try (Jsonb jsonb = JsonbBuilder.create()) {
        return jsonb.fromJson(json, new ArrayList<ModelDTO>(){}.getClass().getGenericSuperclass());
    }
}
```

### 8.3 Ejemplo de Header Authorization
```java
String credentials = "john:secret";
String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
String authHeader = "Basic " + encodedCredentials;
// Resultado: "Basic am9objpzZWNyZXQ="
```

---

## 9. RESUMEN FINAL

### ✅ LO QUE SÍ DEBES HACER
1. Implementar cliente HTTP REST usando `HttpURLConnection`
2. Crear DTOs propios (no compartir entidades JPA)
3. Usar CDI beans para gestionar estado (@SessionScoped, @RequestScoped)
4. Separar responsabilidades: Controller → Service → HTTP
5. Manejar errores HTTP (401, 404, 500)
6. Usar HTTP Basic Authentication para endpoints protegidos
7. Seguir el patrón MVC mostrado en el ejemplo técnico
8. Cumplir los requisitos del enunciado oficial

### ❌ LO QUE NO DEBES HACER
1. Usar JPA, EntityManager, @PersistenceContext en Homework2
2. Importar entidades de Homework1 (`model.entities.*`)
3. Poner lógica de negocio en JSPs
4. Hacer llamadas HTTP directamente en Controllers
5. Compartir código fuente entre Homework1 y Homework2
6. Modificar Homework1 (API REST ya está completo)
7. Asumir que la API siempre responde 200 OK

---

**¡IMPORTANTE!** Los errores de compilación actuales son **normales** porque faltan las dependencias de Jakarta en el `pom.xml`. Una vez configurado Maven, todo compilará correctamente.

**SIGUIENTE PASO:** Implementar los TODOs comentados en cada clase siguiendo este orden:
1. Persona B: `RestClientHelper` (base para todo)
2. Persona A: `ModelService.getModels()`
3. Persona A: `HomeController.showHomePage()`
4. Persona B: `CustomerService.authenticate()`
5. Persona B: `LoginController.processLogin()`
6. Continuar con el checklist...

---

**Fecha de creación:** 30 de diciembre de 2025  
**Versión:** 1.0  
**Autores:** Esqueleto generado para Práctica 2 SOB
