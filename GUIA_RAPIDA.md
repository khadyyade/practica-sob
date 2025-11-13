# 🎯 GUÍA RÁPIDA - Por dónde empezar

## Para PERSONA A (responsable de MODELS)

### 1️⃣ Primer paso: Completar Model.java
📁 Archivo: `src/java/model/entities/Model.java`

**Qué hacer:**
- Buscar todos los comentarios `// TODO PERSONA A:`
- Añadir los campos que faltan (id, name, provider, capabilities, etc.)
- Crear getters y setters (NetBeans: clic derecho → Insert Code → Getter and Setter)
- Implementar hashCode, equals y toString

**Cómo saber si está bien:**
- NetBeans → Clean and Build
- Si compila sin errores → ✅ Continuar al paso 2

### 2️⃣ Segundo paso: Crear modelos de ejemplo
📁 Archivo: `src/java/authn/DataInitializer.java`

**Qué hacer:**
- Ir al método `createSampleModels()`
- Seguir las instrucciones del TODO
- Crear 4 modelos: 2 privados (GPT-4, Claude) y 2 públicos (Mistral, LLaMA)

**Consultar:** `EJEMPLOS_CODIGO.md` sección "Crear entidad y persistir"

### 3️⃣ Tercer paso: Implementar endpoints REST
📁 Archivo: `src/java/service/ModelFacadeREST.java`

**Orden de implementación:**
1. Método `findAll()` - listar con filtros
2. Método `find()` - obtener por ID
3. Método `create()` - crear modelo

**Probar cada método con curl después de implementarlo** (comandos en README.md)

---

## Para PERSONA B (responsable de CUSTOMERS)

### 1️⃣ Primer paso: Completar Customer.java
📁 Archivo: `src/java/model/entities/Customer.java`

**Qué hacer:**
- Buscar todos los comentarios `// TODO PERSONA B:`
- Añadir los campos que faltan (id, username, displayName, email, lastViewedModel)
- Crear getters y setters
- Implementar hashCode, equals y toString

**Cómo saber si está bien:**
- NetBeans → Clean and Build
- Si compila sin errores → ✅ Continuar al paso 2

### 2️⃣ Segundo paso: Crear clientes de ejemplo
📁 Archivo: `src/java/authn/DataInitializer.java`

**Qué hacer:**
- Ir al método `createSampleCustomers()`
- Seguir las instrucciones del TODO
- Crear 2 clientes (sob y demo)

### 3️⃣ Tercer paso: Implementar endpoints REST
📁 Archivo: `src/java/service/CustomerFacadeREST.java`

**Orden de implementación:**
1. Método `findAll()` - listar clientes
2. Método `find()` - obtener con HATEOAS

**Probar cada método con curl** (comandos en README.md)

---

## Para AMBOS

### Usuario de prueba sob/sob
📁 Archivo: `src/java/authn/DataInitializer.java`

**Qué hacer:**
- Implementar método `createTestUser()`
- Crear usuario con username="sob" y password="sob"

**Consultar:** `EJEMPLOS_CODIGO.md` sección "Try-Catch en NamedQuery"

---

## 🔧 Comandos útiles

### Compilar
```bash
# En la terminal
cd /home/darkgarcilaso/URV/SOB/practica-sob
ant clean
ant compile
```

O en NetBeans: **Clic derecho → Clean and Build**

### Desplegar
En NetBeans: **Clic derecho → Deploy**

### Probar endpoint (ejemplo)
```bash
# Listar modelos
curl http://localhost:8080/practica-sob/rest/api/v1/models

# Crear modelo (con autenticación)
curl -i -X POST -u sob:sob \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","provider":"Test","summary":"Test"}' \
  http://localhost:8080/practica-sob/rest/api/v1/models
```

---

## 📚 Documentos de ayuda

1. **EJEMPLOS_CODIGO.md** ← Sintaxis completa con ejemplos
2. **README.md** ← Guía completa con todos los endpoints
3. Los archivos existentes como ejemplo:
   - `Comment.java` - Ejemplo de entidad
   - `CommentFacadeREST.java` - Ejemplo de servicio REST

---

## ✅ Checklist rápido

### PERSONA A:
- [ ] Abrir `Model.java` y completar TODOs
- [ ] Compilar (debe salir sin errores)
- [ ] Implementar `createSampleModels()`
- [ ] Implementar `createTestUser()` (con PERSONA B)
- [ ] Implementar `findAll()` en `ModelFacadeREST`
- [ ] Implementar `find()` en `ModelFacadeREST`
- [ ] Implementar `create()` en `ModelFacadeREST`
- [ ] Desplegar y probar con curl

### PERSONA B:
- [ ] Abrir `Customer.java` y completar TODOs
- [ ] Compilar (debe salir sin errores)
- [ ] Implementar `createSampleCustomers()`
- [ ] Implementar `createTestUser()` (con PERSONA A)
- [ ] Implementar `findAll()` en `CustomerFacadeREST`
- [ ] Implementar `find()` con HATEOAS en `CustomerFacadeREST`
- [ ] Desplegar y probar con curl

---

## 🚨 Errores comunes

### "cannot find symbol"
→ Falta declarar un campo o crear getter/setter

### "Table not found"
→ Entidad incompleta o con errores de sintaxis

### Endpoint devuelve 501 "NOT_IMPLEMENTED"
→ No has completado el método en el servicio REST

### No aparecen datos al listar
→ `DataInitializer` no está implementado o tiene errores

---

## 💡 Consejo final

**Trabajad en orden:**
1. Primero las entidades (Model.java / Customer.java)
2. Luego DataInitializer
3. Después los servicios REST
4. Finalmente las pruebas

**Probad frecuentemente:**
- Compilad después de cada cambio
- Desplegad cuando terminéis un método
- Probad con curl inmediatamente

**Consultad los ejemplos:**
- `EJEMPLOS_CODIGO.md` tiene TODO lo que necesitáis

---

¡Éxito! 🚀
