# Práctica SOB - Catálogo de Modelos LLM

## ⚠️ IMPORTANTE: Esta es una práctica para APRENDER

Los archivos contienen **TODOs** que debéis completar vosotros mismos.  
**NO está todo el código hecho**, solo la estructura básica.

### 📁 Archivos con TODOs para completar:

- ✏️ `src/java/model/entities/Model.java` - **PERSONA A**
- ✏️ `src/java/model/entities/Customer.java` - **PERSONA B**
- ✏️ `src/java/service/ModelFacadeREST.java` - **PERSONA A**
- ✏️ `src/java/service/CustomerFacadeREST.java` - **PERSONA B**
- ✏️ `src/java/authn/DataInitializer.java` - **AMBOS**

### 📚 Documentos de ayuda:

- **`EJEMPLOS_CODIGO.md`** ← ⭐ Consultad este archivo con ejemplos de sintaxis completos

---

## 📋 División de Trabajo

### **PERSONA A → TODO sobre MODELS**
Responsable del recurso completo de modelos LLM (entidad + servicio REST + queries)

**Archivos a completar:**
1. `src/java/model/entities/Model.java`
   - Añadir todos los campos con anotaciones JPA
   - Crear getters/setters
   - Implementar hashCode, equals, toString
   
2. `src/java/service/ModelFacadeREST.java`
   - Implementar GET /models (con filtros)
   - Implementar GET /models/{id} (verificar isPrivate)
   - Implementar POST /models (con validaciones)
   - (OPCIONAL) PUT y DELETE

3. `src/java/authn/DataInitializer.java`
   - Método `createSampleModels()` - crear 4 modelos de ejemplo

### **PERSONA B → TODO sobre CUSTOMERS**  
Responsable del recurso completo de clientes/usuarios (entidad + servicio REST)

**Archivos a completar:**
1. `src/java/model/entities/Customer.java`
   - Añadir todos los campos con anotaciones JPA
   - Crear getters/setters
   - Implementar hashCode, equals, toString
   
2. `src/java/service/CustomerFacadeREST.java`
   - Implementar GET /customer (lista todos)
   - Implementar GET /customer/{id} (con HATEOAS)
   - (OPCIONAL) PUT /customer/{id}

3. `src/java/authn/DataInitializer.java`
   - Método `createSampleCustomers()` - crear 2 clientes

### **AMBOS**
- Completar método `createTestUser()` en `DataInitializer.java`
- Probar endpoints con curl/Postman
- Documentar pruebas realizadas

---

## 🚀 Pasos para Empezar

### 1️⃣ Abrir el Proyecto en NetBeans
```bash
# Desde la terminal, navegar a la carpeta del proyecto
cd /home/darkgarcilaso/URV/SOB/practica-sob

# Abrir NetBeans y seleccionar "Open Project"
# Navegar a esta carpeta y abrir
```

### 2️⃣ Completar los TODOs

**Orden recomendado para PERSONA A:**
1. Completar `Model.java` (entidad con todos los campos)
2. Compilar y verificar que no hay errores
3. Implementar `createSampleModels()` en `DataInitializer.java`
4. Implementar `findAll()` en `ModelFacadeREST.java`
5. Implementar `find()` en `ModelFacadeREST.java`
6. Implementar `create()` en `ModelFacadeREST.java`
7. (Opcional) Implementar PUT y DELETE

**Orden recomendado para PERSONA B:**
1. Completar `Customer.java` (entidad con todos los campos)
2. Compilar y verificar que no hay errores
3. Implementar `createSampleCustomers()` en `DataInitializer.java`
4. Implementar `findAll()` en `CustomerFacadeREST.java`
5. Implementar `find()` con HATEOAS en `CustomerFacadeREST.java`
6. (Opcional) Implementar PUT

**AMBOS juntos:**
- Implementar `createTestUser()` en `DataInitializer.java`

### 3️⃣ Compilar frecuentemente
En NetBeans:
- **Clic derecho** en el proyecto → **Clean and Build**
- O desde terminal:
```bash
ant clean
ant compile
```

**Consejo**: Compilad después de cada cambio importante para detectar errores pronto.

### 4️⃣ Desplegar y Probar
En NetBeans:
- **Clic derecho** en el proyecto → **Deploy**
- Mirar los logs en la pestaña **GlassFish Server**

Buscar el mensaje:
```
✓ DataInitializer: Datos de prueba creados correctamente
```

Si ves errores, revisar los TODOs pendientes.

---

## 🔍 Endpoints a Implementar

Base URL: `http://localhost:8080/<nombre-contexto>/rest/api/v1`

### PERSONA A - Endpoints de MODELS

#### 1. Listar todos los modelos
```bash
GET /rest/api/v1/models
```
Probar:
```bash
curl http://localhost:8080/practica-sob/rest/api/v1/models
```

#### 2. Filtrar por capacidad (1 o 2)
```bash
GET /rest/api/v1/models?capability=chat-completion
GET /rest/api/v1/models?capability=chat-completion&capability=code-generation
```

#### 3. Filtrar por proveedor
```bash
GET /rest/api/v1/models?provider=OpenAI
```

#### 4. Combinación de filtros
```bash
GET /rest/api/v1/models?capability=chat-completion&provider=OpenAI
```

#### 5. Obtener modelo por ID (público - sin auth)
```bash
GET /rest/api/v1/models/3
```

#### 6. Obtener modelo privado (requiere auth)
```bash
# Sin auth - debe fallar con 401
curl -i http://localhost:8080/practica-sob/rest/api/v1/models/1

# Con auth - debe funcionar
curl -i -u sob:sob http://localhost:8080/practica-sob/rest/api/v1/models/1
```

#### 7. Crear modelo (requiere auth)
```bash
curl -i -X POST -u sob:sob \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Gemini Pro",
    "provider": "Google",
    "summary": "Modelo de Google",
    "capabilities": ["chat-completion"],
    "license": "Custom",
    "maxContextTokens": 32000,
    "isPrivate": true
  }' \
  http://localhost:8080/practica-sob/rest/api/v1/models
```

### PERSONA B - Endpoints de CUSTOMERS

#### 1. Listar todos los clientes
```bash
GET /rest/api/v1/customer
```
Probar:
```bash
curl http://localhost:8080/practica-sob/rest/api/v1/customer
```

#### 2. Obtener cliente por ID (con HATEOAS)
```bash
GET /rest/api/v1/customer/1
```
Probar:
```bash
curl http://localhost:8080/practica-sob/rest/api/v1/customer/1
```

Debe devolver JSON con `links`:
```json
{
  "id": 1,
  "username": "sob",
  "displayName": "Usuario SOB de Prueba",
  "email": "sob@urv.cat",
  "links": {
    "model": "/models/1"
  }
}
```

---

## 🧪 Casos de Prueba Recomendados

### Para PERSONA A (Models):

✅ **Casos positivos:**
1. Listar todos → debe devolver 4 modelos
2. Filtrar por capability="chat-completion" → debe devolver todos (4)
3. Filtrar por provider="OpenAI" → debe devolver GPT-4 (1)
4. GET modelo público (Mistral) sin auth → 200 OK
5. GET modelo privado (GPT-4) con auth → 200 OK
6. POST crear modelo con auth → 201 Created

❌ **Casos negativos:**
1. Filtrar con 3+ capabilities → 400 Bad Request
2. GET modelo privado sin auth → 401 Unauthorized
3. GET modelo inexistente (id=999) → 404 Not Found
4. POST sin auth → 401 Unauthorized
5. POST con name vacío → 400 Bad Request
6. POST con maxContextTokens negativo → 400 Bad Request

### Para PERSONA B (Customers):

✅ **Casos positivos:**
1. Listar todos → debe devolver 2 clientes
2. GET customer por id=1 → 200 OK con JSON
3. Verificar que JSON incluye `links` si hay lastViewedModel

❌ **Casos negativos:**
1. GET customer inexistente (id=999) → 404 Not Found
2. Verificar que NUNCA aparece password en respuesta

---

## ✅ Checklist de Implementación

### PERSONA A:
- [ ] Completar campos en `Model.java`
- [ ] Añadir NamedQueries en `Model.java`
- [ ] Crear getters/setters en `Model.java`
- [ ] Implementar `createSampleModels()` en `DataInitializer.java`
- [ ] Implementar `findAll()` con filtros en `ModelFacadeREST.java`
- [ ] Implementar `find()` verificando isPrivate en `ModelFacadeREST.java`
- [ ] Implementar `create()` con validaciones en `ModelFacadeREST.java`
- [ ] Probar todos los endpoints con curl
- [ ] Documentar casos de prueba

### PERSONA B:
- [ ] Completar campos en `Customer.java`
- [ ] Añadir NamedQueries en `Customer.java`
- [ ] Crear getters/setters en `Customer.java`
- [ ] Implementar `createSampleCustomers()` en `DataInitializer.java`
- [ ] Implementar `findAll()` en `CustomerFacadeREST.java`
- [ ] Implementar `find()` con HATEOAS en `CustomerFacadeREST.java`
- [ ] Probar todos los endpoints con curl
- [ ] Verificar que NO se devuelve password
- [ ] Documentar casos de prueba

### AMBOS:
- [ ] Implementar `createTestUser()` en `DataInitializer.java`
- [ ] Compilar sin errores
- [ ] Desplegar aplicación
- [ ] Verificar logs: "✓ DataInitializer: Datos de prueba creados correctamente"
- [ ] Crear colección Postman (opcional)
- [ ] Documentar para el PDF final

---

## 🐛 Solución de Problemas

### Error: "cannot find symbol" en campos
- ❌ Falta declarar el campo en la clase
- ✅ Declarar el campo con su tipo y anotaciones

### Error: "cannot find symbol" en getters/setters
- ❌ No has creado los getters/setters
- ✅ Usar NetBeans: **Clic derecho → Insert Code → Getter and Setter**

### Error de compilación en NamedQueries
- ❌ Falta cerrar comillas o paréntesis
- ✅ Verificar sintaxis en `EJEMPLOS_CODIGO.md`

### La aplicación se despliega pero no hay datos
- ❌ Los métodos en `DataInitializer.java` están vacíos (solo System.out.println)
- ✅ Completar los TODOs en `createTestUser()`, `createSampleModels()`, `createSampleCustomers()`

### Error: "Table MODEL not found"
- ❌ La entidad `Model.java` no está completa o tiene errores
- ✅ Verificar que todos los campos tienen anotaciones JPA correctas

### Endpoint devuelve "NOT_IMPLEMENTED"
- ❌ No has implementado el método en el servicio REST
- ✅ Completar los TODOs en `ModelFacadeREST.java` o `CustomerFacadeREST.java`

### No funciona el filtro de capabilities
- ❌ La query JPQL está mal construida
- ✅ Ver ejemplo completo en `EJEMPLOS_CODIGO.md` sección "Construir JPQL dinámico"

---

## 📚 Recursos de Ayuda

### Dentro del proyecto:
- **EJEMPLOS_CODIGO.md** ← ⭐ Ejemplos completos de sintaxis
- Archivos de ejemplo: `Comment.java`, `Topic.java`, `CommentFacadeREST.java`
- `AbstractFacade.java` - métodos heredados (create, edit, remove, find)

### Documentación externa:
- [JAX-RS Tutorial](https://docs.oracle.com/javaee/7/tutorial/jaxrs.htm)
- [JPA Annotations](https://docs.oracle.com/javaee/7/tutorial/persistence-intro.htm)
- [HTTP Status Codes](https://developer.mozilla.org/en-US/docs/Web/HTTP/Status)

### Herramientas:
- **NetBeans**: Auto-completar código con Ctrl+Space
- **curl**: Probar endpoints desde terminal
- **Postman**: Cliente REST visual (opcional)

---

## 🎯 Consejos para Trabajar Bien

1. **Comunicación**: Avisad al compañero cuando terminéis una parte
2. **Git**: Haced commits frecuentes con mensajes claros
3. **Compilar**: Compilad después de cada cambio importante
4. **Probar**: Probad cada endpoint después de implementarlo
5. **Logs**: Mirad siempre los logs del servidor para debug
6. **Ejemplos**: Consultad `EJEMPLOS_CODIGO.md` cuando tengáis dudas
7. **No copiar-pegar ciegamente**: Entended cada línea que escribís

---

## 📧 Coordinación

**PERSONA A** (Models):
- Avisar cuando `Model.java` esté completo y compile
- Avisar cuando los endpoints de models funcionen
- Probar creando modelos desde Postman/curl

**PERSONA B** (Customers):
- Avisar cuando `Customer.java` esté completo y compile
- Avisar cuando los endpoints de customers funcionen
- Probar que HATEOAS funciona correctamente

**Reunión de integración**:
- Cuando ambos terminen, probar juntos toda la aplicación
- Crear pruebas completas (cliente REST o Postman)
- Preparar documentación para el PDF

---

**¡Ánimo con la práctica! Aprenderéis mucho implementándolo vosotros mismos. 🚀**

**Si tenéis dudas sobre sintaxis, consultad `EJEMPLOS_CODIGO.md`**
