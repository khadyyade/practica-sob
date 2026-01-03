# ENDPOINTS DE LA API REST - HOMEWORK1
## Referencia para implementar los clientes REST en Homework2

**Base URL:** `http://localhost:8080/Homework1/rest/api/v1`

---

## 📋 ÍNDICE
1. [Modelos (Models)](#modelos-models)
2. [Clientes (Customers)](#clientes-customers)
3. [Comentarios (Comments)](#comentarios-comments)
4. [Autenticación](#autenticación)

---

## 1. MODELOS (Models)

### 1.1 Listar Modelos
```http
GET /models
```

**Query Parameters (opcionales):**
- `capability` (String, repetible): Filtrar por capabilities (máximo 2)
  - Ejemplo: `?capability=NLP&capability=Vision`
- `provider` (String): Filtrar por proveedor
  - Ejemplo: `?provider=OpenAI`

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "name": "GPT-4",
    "summary": "Advanced language model",
    "description": "...",
    "isPrivate": false,
    "version": "4.0",
    "trainingDate": "2023-03-14",
    "lastUpdateDate": "2024-01-15",
    "provider": {
      "id": 1,
      "name": "OpenAI",
      "country": "USA"
    },
    "capabilities": [
      {"id": 1, "name": "NLP"},
      {"id": 2, "name": "Text Generation"}
    ],
    "license": {
      "id": 1,
      "name": "Proprietary",
      "description": "Commercial license"
    }
  }
]
```

**Errores:**
- `400 Bad Request`: Si se envían más de 2 capabilities
- `500 Internal Server Error`: Error del servidor

**Ejemplos de uso:**
```bash
# Todos los modelos
GET /models

# Modelos con capability "NLP"
GET /models?capability=NLP

# Modelos con 2 capabilities
GET /models?capability=NLP&capability=Vision

# Modelos de un proveedor específico
GET /models?provider=OpenAI

# Combinación de filtros
GET /models?capability=NLP&provider=OpenAI
```

---

### 1.2 Obtener Modelo por ID
```http
GET /models/{id}
```

**Path Parameters:**
- `id` (Long): ID del modelo

**Headers (opcional):**
- `Authorization`: `Basic <base64(username:password)>` (si el modelo es privado)

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "name": "GPT-4",
  "summary": "Advanced language model",
  "description": "Detailed description...",
  "isPrivate": false,
  "version": "4.0",
  "trainingDate": "2023-03-14",
  "lastUpdateDate": "2024-01-15",
  "provider": {
    "id": 1,
    "name": "OpenAI",
    "country": "USA"
  },
  "capabilities": [
    {"id": 1, "name": "NLP"}
  ],
  "license": {
    "id": 1,
    "name": "Proprietary"
  }
}
```

**Errores:**
- `401 Unauthorized`: Si el modelo es privado (isPrivate=true) y no se envía autenticación
  - Header: `WWW-Authenticate: Basic realm="practica-sob"`
  - Body: `"Authentication required for private models"`
- `404 Not Found`: Modelo no existe
  - Body: `"Model not found"`
- `500 Internal Server Error`: Error del servidor

**Comportamiento importante:**
- Si `isPrivate=false` → NO requiere autenticación
- Si `isPrivate=true` → Requiere `Authorization` header

---

### 1.3 Crear Modelo (requiere autenticación)
```http
POST /models
```

**Headers:**
- `Authorization`: `Basic <base64(username:password)>` (OBLIGATORIO - @Secured)
- `Content-Type`: `application/json` o `application/xml`

**Body:**
```json
{
  "name": "Nuevo Modelo",
  "summary": "Resumen corto",
  "description": "Descripción detallada",
  "version": "1.0",
  "isPrivate": false,
  "provider": {
    "name": "OpenAI"
  },
  "capabilities": [
    {"name": "NLP"}
  ],
  "license": {
    "name": "MIT"
  }
}
```

**Validaciones:**
- `name`: Obligatorio, no vacío
- `provider`: Obligatorio, debe existir en la BD (se busca por nombre)
- `license`: Si se especifica, debe existir en la BD (se busca por nombre)

**Respuesta exitosa (201 Created):**
```json
{
  "id": 10,
  "name": "Nuevo Modelo",
  ...
}
```

**Errores:**
- `400 Bad Request`: Datos inválidos (falta name, provider, etc.)
- `401 Unauthorized`: Sin autenticación
- `500 Internal Server Error`: Error del servidor

---

## 2. CLIENTES (Customers)

### 2.1 Listar Clientes
```http
GET /customer
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "username": "sob",
    "telefono": "123456789",
    "ultimoModeloVisitado": {
      "id": 3,
      "nombre": "GPT-4",
      "link": "/rest/api/v1/models/3"
    }
  },
  {
    "id": 2,
    "username": "demo",
    "telefono": null,
    "ultimoModeloVisitado": null
  }
]
```

**NOTA:** El JSON se construye manualmente en el servidor, NO se exponen passwords.

---

### 2.2 Obtener Cliente por ID
```http
GET /customer/{id}
```

**Path Parameters:**
- `id` (Long): ID del cliente

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "username": "sob",
  "telefono": "123456789",
  "ultimoModeloVisitado": {
    "id": 3,
    "nombre": "GPT-4",
    "link": "/rest/api/v1/models/3"
  }
}
```

**Errores:**
- `404 Not Found`: Cliente no existe
  - Body: `{"error": "Customer con id 1 no encontrado"}`

**Uso para autenticación:**
Este endpoint **NO tiene @Secured**, pero puedes usarlo para validar credenciales:
- Si envías `Authorization` header con credenciales válidas → 200 OK
- Si envías credenciales inválidas → 401 Unauthorized (manejado por RESTRequestFilter)

---

### 2.3 Actualizar Cliente (requiere autenticación)
```http
PUT /customer/{id}
```

**Headers:**
- `Authorization`: `Basic <base64(username:password)>` (OBLIGATORIO - @Secured)
- `Content-Type`: `application/json`

**Body:**
```json
{
  "telefono": "987654321",
  "ultimoModeloVisitadoId": 5
}
```

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "username": "sob",
  "telefono": "987654321",
  ...
}
```

**Errores:**
- `401 Unauthorized`: Sin autenticación
- `404 Not Found`: Cliente no existe

---

## 3. COMENTARIOS (Comments)

**NOTA IMPORTANTE:** La entidad `Comment` existe en el código (CommentFacadeREST) pero **NO existe** la clase `Comment.java` en `model/entities/`. Esto puede causar errores 500. Verificar antes de usar.

### 3.1 Listar Comentarios
```http
GET /comment
```

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "text": "Excelente modelo",
    "createdAt": "2024-12-30T10:00:00Z",
    "customer": {...},
    "model": {...}
  }
]
```

---

### 3.2 Obtener Comentario por ID (requiere autenticación)
```http
GET /comment/{id}
```

**Headers:**
- `Authorization`: `Basic <base64(username:password)>` (OBLIGATORIO - @Secured)

**Path Parameters:**
- `id` (Long): ID del comentario

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "text": "...",
  "customer": {...},
  "model": {...}
}
```

**Errores:**
- `401 Unauthorized`: Sin autenticación
- `404 Not Found`: Comentario no existe

---

### 3.3 Crear Comentario
```http
POST /comment
```

**Headers:**
- `Content-Type`: `application/json` o `application/xml`

**Body:**
```json
{
  "text": "Este es mi comentario",
  "customer": {
    "id": 1
  },
  "model": {
    "id": 2
  }
}
```

**Respuesta exitosa (201 Created):**
Sin body (void).

**NOTA:** Este endpoint **NO tiene @Secured**, pero debería usarse con autenticación.

---

## 4. AUTENTICACIÓN

### 4.1 Mecanismo de Autenticación
Homework1 usa **HTTP Basic Authentication** implementada en `RESTRequestFilter.java`.

**Formato del header:**
```
Authorization: Basic <base64(username:password)>
```

**Ejemplo:**
```bash
# Credenciales: sob:sob
# Base64("sob:sob") = c29iOnNvYg==

Authorization: Basic c29iOnNvYg==
```

**Generación en Java:**
```java
import java.util.Base64;
import java.nio.charset.StandardCharsets;

String username = "sob";
String password = "sob";
String credentials = username + ":" + password;
String encodedCredentials = Base64.getEncoder()
    .encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
String authHeader = "Basic " + encodedCredentials;
```

---

### 4.2 Endpoints Protegidos (@Secured)
Los siguientes endpoints **requieren** autenticación:
- `POST /models` (crear modelo)
- `PUT /customer/{id}` (actualizar cliente)
- `GET /comment/{id}` (obtener comentario por ID)

---

### 4.3 Validar Credenciales
Para validar si las credenciales son correctas, puedes:

**Opción 1: Probar con endpoint protegido**
```http
GET /comment/1
Authorization: Basic <credentials>
```
- Si devuelve `200 OK` o `404 Not Found` → credenciales válidas
- Si devuelve `401 Unauthorized` → credenciales inválidas

**Opción 2: Probar con endpoint público usando headers**
```http
GET /customer/1
Authorization: Basic <credentials>
```
- Si devuelve `200 OK` → credenciales válidas (el filtro las procesó correctamente)
- Si devuelve `401 Unauthorized` → credenciales inválidas

---

## 5. RESUMEN DE ENDPOINTS PARA HOMEWORK2

### ModelService.java
```java
// Listar modelos (con filtros opcionales)
GET /models?capability=NLP&provider=OpenAI

// Obtener modelo por ID (público o privado)
GET /models/{id}
Authorization: Basic ... (solo si isPrivate=true)
```

### CustomerService.java
```java
// Validar credenciales (autenticar)
GET /comment/1  // O cualquier endpoint @Secured
Authorization: Basic ...

// Obtener cliente por ID
GET /customer/{id}

// Listar todos los clientes
GET /customer
```

### CommentService.java
```java
// Listar comentarios
GET /comment

// Obtener comentario por ID (requiere auth)
GET /comment/{id}
Authorization: Basic ...

// Crear comentario
POST /comment
Content-Type: application/json
Body: {"text": "...", "customer": {"id": 1}, "model": {"id": 2}}
```

---

## 6. CÓDIGOS DE ESTADO HTTP

| Código | Significado | Cuándo ocurre |
|--------|-------------|---------------|
| 200 OK | Éxito | GET exitoso |
| 201 Created | Recurso creado | POST exitoso |
| 400 Bad Request | Datos inválidos | Validación fallida |
| 401 Unauthorized | No autenticado | Falta Authorization header o credenciales inválidas |
| 404 Not Found | No existe | Recurso no encontrado |
| 500 Internal Server Error | Error del servidor | Excepción en el servidor |

---

## 7. USUARIOS DE PRUEBA

Según el código de Homework1, los usuarios por defecto son:

| Username | Password | ID (aproximado) |
|----------|----------|-----------------|
| sob      | sob      | 1               |
| demo     | demo     | 2               |

**NOTA:** Verificar en `install.jsp` o en la base de datos los usuarios exactos.

---

## 8. NOTAS IMPORTANTES

### ⚠️ Limitaciones de la API
1. **Máximo 2 capabilities** en el filtro de modelos
2. **No existe endpoint `/customer/me`** → Implementar buscando por username
3. **Comment.java no existe** → Puede causar errores 500 en endpoints de comentarios
4. **Provider y License deben existir** → Al crear modelos, buscar por nombre

### ✅ Buenas Prácticas
1. Siempre manejar errores 401, 404, 500
2. Usar try-catch en llamadas HTTP
3. Validar respuestas antes de parsear JSON
4. Limpiar capabilities vacías antes de enviar
5. Codificar correctamente el header Authorization (Base64)

---

**Fecha de creación:** 30 de diciembre de 2025  
**Versión:** 1.0  
**Basado en:** Homework1 - Análisis del código fuente
