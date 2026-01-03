# TODO LIST - HOMEWORK2
## Lista de tareas pendientes organizadas por persona

---

## 👤 PERSONA A - Gestión de Modelos

### 📦 FASE 1: Preparación de DTOs
- [ ] **ModelDTO.java**
  - [ ] Añadir atributo `String topic`
  - [ ] Añadir atributo `List<String> capabilities`
  - [ ] Añadir atributo `String providerName` (para vista privada)
  - [ ] Añadir atributo `List<LicenseDTO> licenses` (para vista privada)
  - [ ] Crear getters y setters para todos los atributos nuevos
  - [ ] (Opcional) Crear `LicenseDTO` si la API devuelve licencias

- [ ] **CommentDTO.java**
  - [ ] Verificar que los atributos coincidan con la API REST
  - [ ] (Opcional) Añadir `Long modelId` si es necesario

- [ ] **ModelListForm.java**
  - [ ] Implementar método `hasFilters()`
  - [ ] Implementar método `toQueryString()` para construir URL params

---

### 🔧 FASE 2: Capa de Servicio (ModelService)
- [ ] **ModelService.getModels()**
  - [ ] Construir URL base: `http://localhost:8080/Homework1/api/models`
  - [ ] Añadir query params usando `filters.toQueryString()`
  - [ ] Llamar a `restClient.get(url, null)` (sin autenticación)
  - [ ] Parsear respuesta JSON a `List<ModelDTO>` usando Jakarta JSON-B o Gson
  - [ ] Manejar excepciones de red y HTTP (try-catch)
  - [ ] Retornar lista vacía si hay error

- [ ] **ModelService.getModelById()**
  - [ ] Construir URL: `API_BASE_URL + "/models/" + modelId`
  - [ ] Llamar a `restClient.get(url, null)`
  - [ ] Parsear JSON a `ModelDTO`
  - [ ] Manejar error 404 (NotFoundException) → retornar `null`
  - [ ] Manejar error 500 → lanzar excepción o retornar `null`

- [ ] **ModelService.getPrivateModelDetails()**
  - [ ] Construir URL: `API_BASE_URL + "/models/" + modelId`
  - [ ] Llamar a `restClient.get(url, authHeader)` con autenticación
  - [ ] Parsear JSON completo (incluye provider, licencias, etc.)
  - [ ] Manejar error 401 (Unauthorized) → lanzar excepción
  - [ ] Manejar error 404 (Not Found) → retornar `null`

- [ ] **Métodos auxiliares de parseo JSON**
  - [ ] Implementar `parseJsonToModelList(String json)`
  - [ ] Implementar `parseJsonToModel(String json)`
  - [ ] Decidir biblioteca JSON: Jakarta JSON-B, Gson o Jackson

---

### 🎮 FASE 3: Capa de Controlador
- [ ] **HomeController.showHomePage()**
  - [ ] Actualizar `filterForm` con los parámetros recibidos:
    - [ ] `filterForm.setTopic(topic)`
    - [ ] `filterForm.setCapability(capability)`
    - [ ] `filterForm.setMaxPrice(maxPrice)`
  - [ ] Llamar a `modelService.getModels(filterForm)`
  - [ ] Añadir lista al modelo MVC: `models.put("models", modelList)`
  - [ ] Añadir formulario de filtros: `models.put("filters", filterForm)`
  - [ ] Envolver en try-catch para manejar errores de red
  - [ ] Si hay error, añadir mensaje: `models.put("error", "Error al cargar modelos")`
  - [ ] Retornar `"index.jsp"`

- [ ] **ModelDetailController.showPublicDetail()**
  - [ ] Llamar a `modelService.getModelById(modelId)`
  - [ ] Verificar si `model == null`:
    - [ ] Si es null → `models.put("error", "Model not found")`
    - [ ] Retornar `"error.jsp"` o redirigir a `/error?code=404`
  - [ ] Si existe → `models.put("model", model)`
  - [ ] Retornar `"modelDetail.jsp"`

---

### 🖼️ FASE 4: Vistas JSP
- [ ] **index.jsp** (Listado de modelos)
  - [ ] Crear formulario de filtros:
    - [ ] Input para `topic`
    - [ ] Input para `capability`
    - [ ] Input para `maxPrice`
    - [ ] Botón de submit
  - [ ] Mostrar lista de modelos:
    - [ ] `<c:forEach items="${models}" var="model">`
    - [ ] Mostrar `model.name`, `model.price`, `model.description`
    - [ ] Link a detalle: `<a href="/model/${model.id}">Ver detalles</a>`
  - [ ] Mostrar mensaje si la lista está vacía
  - [ ] Mostrar mensaje de error si existe: `${error}`

- [ ] **modelDetail.jsp** (Vista pública)
  - [ ] Mostrar información básica del modelo:
    - [ ] Nombre (`${model.name}`)
    - [ ] Descripción (`${model.description}`)
    - [ ] Precio (`${model.price}`)
    - [ ] Topic (`${model.topic}`)
    - [ ] Capabilities (`${model.capabilities}`)
  - [ ] Link a vista privada: `<a href="/model/${model.id}/private">Ver detalles completos (requiere login)</a>`
  - [ ] Botón de volver: `<a href="/">Volver al listado</a>`

---

## 👤 PERSONA B - Autenticación y Seguridad

### 📦 FASE 1: Preparación de Beans y DTOs
- [ ] **UserSession.java**
  - [ ] Implementar método `getAuthHeader()`:
    - [ ] Verificar `authenticated == true`
    - [ ] Construir string: `username + ":" + password`
    - [ ] Codificar en Base64: `Base64.getEncoder().encodeToString(credentials.getBytes())`
    - [ ] Retornar: `"Basic " + encodedCredentials`
  - [ ] Implementar método `clearSession()`:
    - [ ] `username = null`
    - [ ] `password = null`
    - [ ] `authenticated = false`
    - [ ] `returnUrl = null`
    - [ ] `loginError = null`
  - [ ] Implementar método `clearReturnUrl()`:
    - [ ] `returnUrl = null`

- [ ] **CustomerDTO.java**
  - [ ] Verificar que los atributos coincidan con la API REST
  - [ ] Añadir atributos adicionales si la API los devuelve

---

### 🔧 FASE 2: Cliente HTTP (RestClientHelper)
- [ ] **RestClientHelper.get()**
  - [ ] Abrir conexión: `HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection()`
  - [ ] Configurar método: `connection.setRequestMethod("GET")`
  - [ ] Añadir header: `connection.setRequestProperty("Accept", "application/json")`
  - [ ] Si `authHeader != null`: `connection.setRequestProperty("Authorization", authHeader)`
  - [ ] Obtener código de respuesta: `int responseCode = connection.getResponseCode()`
  - [ ] Si `200 <= responseCode < 300`:
    - [ ] Leer body con `BufferedReader`
    - [ ] Retornar respuesta como String
  - [ ] Si `responseCode == 401`: lanzar `UnauthorizedException`
  - [ ] Si `responseCode == 404`: lanzar `NotFoundException`
  - [ ] Otros errores: lanzar `RestClientException`

- [ ] **RestClientHelper.post()**
  - [ ] Configurar conexión similar a GET
  - [ ] Añadir headers:
    - [ ] `Content-Type: application/json`
    - [ ] `Accept: application/json`
  - [ ] `connection.setDoOutput(true)`
  - [ ] Escribir body: `OutputStream os = connection.getOutputStream()`
  - [ ] `os.write(jsonBody.getBytes("UTF-8"))`
  - [ ] Leer respuesta y manejar códigos igual que GET

- [ ] **Excepciones personalizadas**
  - [ ] Ya están creadas (`RestClientException`, `UnauthorizedException`, `NotFoundException`)
  - [ ] Verificar que se usen correctamente en los métodos

---

### 🔧 FASE 3: Servicios REST (CustomerService y CommentService)
- [ ] **CustomerService.authenticate()**
  - [ ] Construir credenciales: `username + ":" + password`
  - [ ] Codificar en Base64
  - [ ] Crear authHeader: `"Basic " + encodedCredentials`
  - [ ] Construir URL: `API_BASE_URL + "/customers/me"` (o endpoint que verifique auth)
  - [ ] Llamar a `restClient.get(url, authHeader)` en try-catch
  - [ ] Si respuesta exitosa (sin excepción) → retornar `true`
  - [ ] Si lanza `UnauthorizedException` → retornar `false`
  - [ ] Manejar otras excepciones (red, timeout) → retornar `false` o lanzar

- [ ] **CustomerService.getAuthenticatedCustomer()**
  - [ ] Construir URL: `API_BASE_URL + "/customers/me"`
  - [ ] Llamar a `restClient.get(url, authHeader)`
  - [ ] Parsear JSON a `CustomerDTO`
  - [ ] Manejar error 401 → retornar `null`

- [ ] **CommentService.getComments()**
  - [ ] Construir URL: `API_BASE_URL + "/comments?modelId=" + modelId` (ajustar según tu API)
  - [ ] Llamar a `restClient.get(url, authHeader)`
  - [ ] Parsear JSON a `List<CommentDTO>` usando método auxiliar
  - [ ] Manejar error 401 (no autenticado)
  - [ ] Manejar error 404 (modelo no encontrado)

- [ ] **CommentService.addComment()** (OPCIONAL)
  - [ ] Construir URL: `API_BASE_URL + "/comments"`
  - [ ] Construir JSON body: `{"modelId": modelId, "text": "commentText"}`
  - [ ] Llamar a `restClient.post(url, jsonBody, authHeader)`
  - [ ] Verificar código de respuesta (201 Created)
  - [ ] Retornar `true` si éxito, `false` si error

- [ ] **Métodos auxiliares de parseo JSON**
  - [ ] Implementar `parseJsonToCommentList(String json)`
  - [ ] Implementar `parseJsonToCustomer(String json)`

---

### 🎮 FASE 4: Capa de Controlador
- [ ] **LoginController.showLoginForm()**
  - [ ] Verificar si ya está autenticado: `if (userSession.isAuthenticated())`
    - [ ] Si sí → redirigir a `"redirect:/"`
  - [ ] Añadir mensaje de error si existe: `models.put("error", userSession.getLoginError())`
  - [ ] Retornar `"login.jsp"`

- [ ] **LoginController.processLogin()**
  - [ ] Validar que username y password no estén vacíos
  - [ ] Si vacíos → `models.put("error", "Username and password are required")` → retornar `"login.jsp"`
  - [ ] Llamar a `customerService.authenticate(username, password)`
  - [ ] Si válido:
    - [ ] `userSession.setUsername(username)`
    - [ ] `userSession.setPassword(password)`
    - [ ] `userSession.setAuthenticated(true)`
    - [ ] Obtener URL de retorno: `String returnUrl = userSession.getReturnUrl()`
    - [ ] Si `returnUrl == null` → `returnUrl = "/"`
    - [ ] `userSession.clearReturnUrl()`
    - [ ] Redirigir: `return Response.seeOther(URI.create(returnUrl)).build()`
  - [ ] Si inválido:
    - [ ] `models.put("error", "Invalid username or password")`
    - [ ] Retornar `"login.jsp"`

- [ ] **LoginController.logout()**
  - [ ] Llamar a `userSession.clearSession()`
  - [ ] Redirigir a `return Response.seeOther(URI.create("/")).build()`

- [ ] **ModelDetailController.showPrivateDetail()**
  - [ ] Verificar autenticación: `if (!userSession.isAuthenticated())`
    - [ ] `userSession.setReturnUrl("/model/" + modelId + "/private")`
    - [ ] Redirigir a login: `return Response.seeOther(URI.create("/login")).build()`
  - [ ] Llamar a `modelService.getPrivateModelDetails(modelId, userSession.getAuthHeader())`
  - [ ] Llamar a `commentService.getComments(modelId, userSession.getAuthHeader())`
  - [ ] Manejar excepciones:
    - [ ] `UnauthorizedException` → limpiar sesión, redirigir a login
    - [ ] `NotFoundException` → `models.put("error", "Model not found")`, retornar error.jsp
  - [ ] Añadir al modelo:
    - [ ] `models.put("model", model)`
    - [ ] `models.put("comments", comments)`
  - [ ] Retornar `"privateDetail.jsp"`

- [ ] **ErrorController.showError()**
  - [ ] Añadir código de error: `models.put("errorCode", errorCode != null ? errorCode : 500)`
  - [ ] Añadir mensaje: `models.put("errorMessage", errorMessage != null ? errorMessage : "An unexpected error occurred")`
  - [ ] Switch según código:
    - [ ] 404 → `models.put("errorTitle", "Resource Not Found")`
    - [ ] 401 → `models.put("errorTitle", "Unauthorized")`
    - [ ] 500 → `models.put("errorTitle", "Internal Server Error")`
  - [ ] Retornar `"Error404.jsp"`

---

### 🖼️ FASE 5: Vistas JSP
- [ ] **login.jsp**
  - [ ] Crear formulario: `<form method="POST" action="/login">`
    - [ ] Input para username: `<input type="text" name="username" required>`
    - [ ] Input para password: `<input type="password" name="password" required>`
    - [ ] Botón submit: `<button type="submit">Login</button>`
  - [ ] Mostrar mensaje de error si existe: `<c:if test="${not empty error}"><div class="error">${error}</div></c:if>`
  - [ ] Link a página principal: `<a href="/">Volver sin login</a>`

- [ ] **privateDetail.jsp** (Vista privada completa)
  - [ ] Mostrar toda la información de `modelDetail.jsp` MÁS:
    - [ ] Provider: `${model.providerName}`
    - [ ] Licencias: `<c:forEach items="${model.licenses}" var="license">...</c:forEach>`
  - [ ] Mostrar comentarios:
    - [ ] `<c:forEach items="${comments}" var="comment">`
    - [ ] Mostrar `${comment.text}`, `${comment.customerName}`, `${comment.createdAt}`
  - [ ] (Opcional) Formulario para añadir comentario:
    - [ ] `<form method="POST" action="/model/${model.id}/comment">`
    - [ ] `<textarea name="text"></textarea>`
    - [ ] `<button type="submit">Añadir comentario</button>`
  - [ ] Botón de logout: `<a href="/logout">Cerrar sesión</a>`

- [ ] **Error404.jsp** (actualizar si es necesario)
  - [ ] Mostrar `${errorCode}`, `${errorTitle}`, `${errorMessage}`
  - [ ] Link a página principal: `<a href="/">Volver al inicio</a>`

---

## 🔗 TAREAS DE INTEGRACIÓN (Ambas Personas)

### Configuración del Proyecto
- [ ] **pom.xml**
  - [ ] Verificar dependencias de Jakarta EE:
    - [ ] `jakarta.mvc:jakarta.mvc-api:2.0.0`
    - [ ] `jakarta.platform:jakarta.jakartaee-api:9.0.0` (o 10.0.0)
    - [ ] `org.eclipse.krazo:krazo-jersey:2.0.0` (implementación de MVC)
    - [ ] Jakarta JSON-B: `jakarta.json.bind:jakarta.json.bind-api`
  - [ ] Verificar configuración de GlassFish/Payara

- [ ] **Config.properties**
  - [ ] Crear en `src/main/resources/`
  - [ ] Añadir: `api.base.url=http://localhost:8080/Homework1/api`
  - [ ] (Opcional) Leer desde código: `@Inject @ConfigProperty(name="api.base.url") String apiUrl`

- [ ] **beans.xml**
  - [ ] Verificar que existe en `WEB-INF/` para habilitar CDI

---

### Testing de Integración
- [ ] **Probar flujo completo sin autenticación:**
  - [ ] Acceder a `/` → debe mostrar listado
  - [ ] Aplicar filtros → `/?topic=NLP` → debe filtrar
  - [ ] Click en modelo → `/model/1` → debe mostrar detalle público
  - [ ] Click en "ver detalles completos" → debe redirigir a `/login`

- [ ] **Probar flujo completo con autenticación:**
  - [ ] Acceder a `/login` → debe mostrar formulario
  - [ ] Login con credenciales incorrectas → debe mostrar error
  - [ ] Login con credenciales correctas → debe redirigir a URL guardada
  - [ ] Acceder a `/model/1/private` → debe mostrar licencias y comentarios
  - [ ] Logout → debe limpiar sesión

- [ ] **Probar manejo de errores:**
  - [ ] Acceder a modelo inexistente: `/model/99999` → debe mostrar error 404
  - [ ] Detener Homework1 y acceder a `/` → debe mostrar error de conexión
  - [ ] Sesión expirada en vista privada → debe redirigir a login

---

## 📝 NOTAS FINALES

### Orden de Implementación Sugerido:
1. **Persona B:** `RestClientHelper` (base de todo)
2. **Persona A:** `ModelService.getModels()` + parseo JSON
3. **Persona A:** `HomeController.showHomePage()` + `index.jsp`
4. **Persona B:** `CustomerService.authenticate()`
5. **Persona B:** `LoginController` completo + `login.jsp`
6. **Persona A:** `ModelDetailController.showPublicDetail()` + `modelDetail.jsp`
7. **Persona B:** `ModelDetailController.showPrivateDetail()`
8. **Persona B:** `CommentService.getComments()`
9. **Persona A/B:** `privateDetail.jsp`
10. **Testing e integración**

### Puntos de Sincronización:
- **Después del paso 1:** Ambos tienen acceso a `RestClientHelper`
- **Después del paso 3:** Se puede probar el listado de modelos
- **Después del paso 5:** Se puede probar el login
- **Después del paso 9:** Funcionalidad completa

---

**¡Recuerda!** Comentar los TODOs completados y hacer commits frecuentes para evitar conflictos de merge.
