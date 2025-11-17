#!/bin/bash

# =============================================================================
# BATERÍA DE TESTS PARA LA API REST DE MODELS
# =============================================================================
# Este script ejecuta una serie de tests sobre la API REST para verificar
# que todos los endpoints de modelos funcionan correctamente.
# 
# REQUISITOS:
# - Servidor GlassFish corriendo en localhost:8080
# - Base de datos poblada (ejecutar install.jsp primero)
# - curl y jq instalados
#
# USO:
#   chmod +x test_models_api.sh
#   ./test_models_api.sh
# =============================================================================

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuración
BASE_URL="http://localhost:8080/practica-sob/rest/api/v1"
INSTALL_URL="http://localhost:8080/practica-sob/install.jsp"

# Contadores
TESTS_PASSED=0
TESTS_FAILED=0
TESTS_TOTAL=0

# Función para imprimir encabezados
print_header() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

# Función para imprimir resultado del test
print_result() {
    local test_name="$1"
    local result="$2"
    local details="$3"
    
    TESTS_TOTAL=$((TESTS_TOTAL + 1))
    
    if [ "$result" = "PASS" ]; then
        echo -e "${GREEN}✓ PASS${NC} - $test_name"
        [ -n "$details" ] && echo -e "  ${YELLOW}→${NC} $details"
        TESTS_PASSED=$((TESTS_PASSED + 1))
    else
        echo -e "${RED}✗ FAIL${NC} - $test_name"
        [ -n "$details" ] && echo -e "  ${RED}→${NC} $details"
        TESTS_FAILED=$((TESTS_FAILED + 1))
    fi
}

# Función para verificar si el servidor está corriendo
check_server() {
    print_header "VERIFICANDO SERVIDOR"
    
    if curl -s -f "$BASE_URL/models" > /dev/null 2>&1; then
        echo -e "${GREEN}✓${NC} Servidor GlassFish está corriendo"
        return 0
    else
        echo -e "${RED}✗${NC} Servidor GlassFish no está accesible"
        echo -e "${YELLOW}→${NC} Asegúrate de que GlassFish está corriendo y la aplicación está desplegada"
        exit 1
    fi
}

# Función para verificar dependencias
check_dependencies() {
    print_header "VERIFICANDO DEPENDENCIAS"
    
    local all_ok=true
    
    if command -v curl > /dev/null 2>&1; then
        echo -e "${GREEN}✓${NC} curl está instalado"
    else
        echo -e "${RED}✗${NC} curl no está instalado"
        all_ok=false
    fi
    
    if command -v jq > /dev/null 2>&1; then
        echo -e "${GREEN}✓${NC} jq está instalado"
    else
        echo -e "${YELLOW}!${NC} jq no está instalado (opcional, mejora la visualización)"
    fi
    
    if [ "$all_ok" = false ]; then
        exit 1
    fi
}

# Función para poblar la base de datos
populate_database() {
    print_header "POBLANDO BASE DE DATOS"
    
    echo "Ejecutando install.jsp..."
    local response=$(curl -s "$INSTALL_URL")
    
    if echo "$response" | grep -q "INSERT"; then
        echo -e "${GREEN}✓${NC} Base de datos poblada correctamente"
    else
        echo -e "${YELLOW}!${NC} No se pudo verificar la población de datos"
    fi
    
    # Esperar un momento para que los datos se persistan
    sleep 2
}

# =============================================================================
# TESTS
# =============================================================================

# TEST 1: GET /models (listar todos los modelos)
test_get_all_models() {
    print_header "TEST 1: GET /models - Listar todos los modelos"
    
    local response=$(curl -s "$BASE_URL/models")
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/models")
    
    # Verificar código HTTP 200
    if [ "$http_code" = "200" ]; then
        print_result "GET /models retorna HTTP 200" "PASS" "Código: $http_code"
    else
        print_result "GET /models retorna HTTP 200" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que es un array JSON
    if echo "$response" | jq -e '. | type == "array"' > /dev/null 2>&1; then
        print_result "Respuesta es un array JSON" "PASS"
    else
        print_result "Respuesta es un array JSON" "FAIL" "No es un array válido"
        return
    fi
    
    # Verificar que hay al menos 3 modelos
    local count=$(echo "$response" | jq '. | length')
    if [ "$count" -ge 3 ]; then
        print_result "Contiene al menos 3 modelos" "PASS" "Total: $count modelos"
    else
        print_result "Contiene al menos 3 modelos" "FAIL" "Total: $count modelos"
    fi
    
    # Verificar estructura del primer modelo
    local has_id=$(echo "$response" | jq -e '.[0] | has("id")' 2>/dev/null)
    local has_name=$(echo "$response" | jq -e '.[0] | has("name")' 2>/dev/null)
    local has_provider=$(echo "$response" | jq -e '.[0] | has("provider")' 2>/dev/null)
    local has_capabilities=$(echo "$response" | jq -e '.[0] | has("capabilities")' 2>/dev/null)
    
    if [ "$has_id" = "true" ] && [ "$has_name" = "true" ]; then
        print_result "Modelos tienen campos id y name" "PASS"
    else
        print_result "Modelos tienen campos id y name" "FAIL"
    fi
    
    if [ "$has_provider" = "true" ]; then
        print_result "Modelos tienen campo provider" "PASS"
    else
        print_result "Modelos tienen campo provider" "FAIL"
    fi
    
    if [ "$has_capabilities" = "true" ]; then
        print_result "Modelos tienen campo capabilities" "PASS"
    else
        print_result "Modelos tienen campo capabilities" "FAIL"
    fi
    
    # Verificar que capabilities es un array
    local caps_is_array=$(echo "$response" | jq -e '.[0].capabilities | type == "array"' 2>/dev/null)
    if [ "$caps_is_array" = "true" ]; then
        print_result "Capabilities es un array" "PASS"
    else
        print_result "Capabilities es un array" "FAIL"
    fi
    
    # Mostrar respuesta real
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 2: GET /models?provider={name} (filtrar por proveedor)
test_get_models_by_provider() {
    print_header "TEST 2: GET /models?provider=OpenAI - Filtrar por proveedor"
    
    local provider="OpenAI"
    local response=$(curl -s "$BASE_URL/models?provider=$provider")
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/models?provider=$provider")
    
    # Verificar código HTTP 200
    if [ "$http_code" = "200" ]; then
        print_result "GET /models?provider retorna HTTP 200" "PASS" "Código: $http_code"
    else
        print_result "GET /models?provider retorna HTTP 200" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que es un array
    if echo "$response" | jq -e '. | type == "array"' > /dev/null 2>&1; then
        print_result "Respuesta es un array JSON" "PASS"
    else
        print_result "Respuesta es un array JSON" "FAIL"
        return
    fi
    
    # Verificar que hay al menos 1 modelo
    local count=$(echo "$response" | jq '. | length')
    if [ "$count" -ge 1 ]; then
        print_result "Contiene al menos 1 modelo de OpenAI" "PASS" "Total: $count modelos"
    else
        print_result "Contiene al menos 1 modelo de OpenAI" "FAIL" "Total: $count modelos"
    fi
    
    # Verificar que todos los modelos son de OpenAI
    local all_openai=$(echo "$response" | jq -e 'all(.provider.name == "OpenAI")' 2>/dev/null)
    if [ "$all_openai" = "true" ]; then
        print_result "Todos los modelos son de OpenAI" "PASS" "✓ Filtro funciona correctamente"
    else
        print_result "Todos los modelos son de OpenAI" "FAIL" "⚠ Filtro no funciona correctamente"
    fi
    
    # Mostrar nombres de modelos
    local model_names=$(echo "$response" | jq -r '.[].name' 2>/dev/null | tr '\n' ', ' | sed 's/,$//')
    if [ -n "$model_names" ]; then
        echo -e "  ${YELLOW}→${NC} Modelos encontrados: $model_names"
    fi
    
    # Mostrar respuesta real
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 3: GET /models?capability={name} (filtrar por 1 capability)
test_get_models_by_capability() {
    print_header "TEST 3: GET /models?capability=chat-completion - Filtrar por capability"
    
    local capability="chat-completion"
    local response=$(curl -s "$BASE_URL/models?capability=$capability")
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/models?capability=$capability")
    
    # Verificar código HTTP 200
    if [ "$http_code" = "200" ]; then
        print_result "GET /models?capability retorna HTTP 200" "PASS" "Código: $http_code"
    else
        print_result "GET /models?capability retorna HTTP 200" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que hay resultados
    local count=$(echo "$response" | jq '. | length' 2>/dev/null)
    if [ "$count" -ge 1 ]; then
        print_result "Contiene modelos con capability $capability" "PASS" "Total: $count modelos"
    else
        print_result "Contiene modelos con capability $capability" "FAIL" "Total: $count modelos"
    fi
    
    # Verificar que todos tienen la capability
    local all_have_cap=$(echo "$response" | jq -e --arg cap "$capability" 'all(.capabilities | map(.name) | contains([$cap]))' 2>/dev/null)
    if [ "$all_have_cap" = "true" ]; then
        print_result "Todos los modelos tienen la capability" "PASS" "✓ Filtro correcto"
    else
        print_result "Todos los modelos tienen la capability" "FAIL" "⚠ Filtro incorrecto"
    fi
    
    # Mostrar respuesta real
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 4: GET /models con 2 capabilities
test_get_models_by_two_capabilities() {
    print_header "TEST 4: GET /models con 2 capabilities - Filtro múltiple"
    
    local cap1="chat-completion"
    local cap2="code-generation"
    local response=$(curl -s "$BASE_URL/models?capability=$cap1&capability=$cap2")
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/models?capability=$cap1&capability=$cap2")
    
    # Verificar código HTTP 200
    if [ "$http_code" = "200" ]; then
        print_result "GET /models con 2 capabilities retorna HTTP 200" "PASS" "Código: $http_code"
    else
        print_result "GET /models con 2 capabilities retorna HTTP 200" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que es un array
    if echo "$response" | jq -e '. | type == "array"' > /dev/null 2>&1; then
        print_result "Respuesta es un array JSON" "PASS"
    else
        print_result "Respuesta es un array JSON" "FAIL"
        return
    fi
    
    # Mostrar cuántos modelos tienen ambas capabilities
    local count=$(echo "$response" | jq '. | length' 2>/dev/null)
    echo -e "  ${YELLOW}→${NC} Modelos con ambas capabilities: $count"
    
    if [ "$count" -ge 1 ]; then
        # Verificar que todos tienen ambas capabilities
        local all_have_both=$(echo "$response" | jq -e --arg cap1 "$cap1" --arg cap2 "$cap2" 'all(.capabilities | map(.name) | (contains([$cap1]) and contains([$cap2])))' 2>/dev/null)
        if [ "$all_have_both" = "true" ]; then
            print_result "Modelos tienen ambas capabilities" "PASS" "✓ Filtro AND funciona"
        else
            print_result "Modelos tienen ambas capabilities" "FAIL" "⚠ Filtro AND no funciona"
        fi
    fi
    
    # Mostrar respuesta real
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 5: GET /models con 3+ capabilities (debe retornar 400)
test_get_models_too_many_capabilities() {
    print_header "TEST 5: GET /models con 3+ capabilities - Validación límite (400)"
    
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/models?capability=chat-completion&capability=code-generation&capability=text-to-image")
    
    if [ "$http_code" = "400" ]; then
        print_result "Retorna HTTP 400 para 3+ capabilities" "PASS" "Código: $http_code (✓ Validación correcta)"
    else
        print_result "Retorna HTTP 400 para 3+ capabilities" "FAIL" "Código: $http_code (esperado: 400)"
    fi
    
    # Verificar el mensaje de error
    local response=$(curl -s "$BASE_URL/models?capability=chat-completion&capability=code-generation&capability=text-to-image")
    local error_msg=$(echo "$response" | jq -r '.error' 2>/dev/null)
    
    if echo "$error_msg" | grep -qi "maximum.*2.*capabilities"; then
        print_result "Mensaje de error apropiado" "PASS" "Mensaje: $error_msg"
    else
        print_result "Mensaje de error apropiado" "FAIL" "Mensaje: $error_msg"
    fi
    
    # Mostrar respuesta real
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 6: GET /models/{id} - Obtener modelo público sin autenticación
test_get_public_model() {
    print_header "TEST 6: GET /models/{id} - Modelo público sin auth"
    
    # Primero obtener un modelo público
    local all_models=$(curl -s "$BASE_URL/models")
    local public_model_id=$(echo "$all_models" | jq -r '.[] | select(.isPrivate == false) | .id' 2>/dev/null | head -1)
    
    if [ -z "$public_model_id" ] || [ "$public_model_id" = "null" ]; then
        print_result "Encontrar modelo público" "FAIL" "No hay modelos públicos"
        return
    fi
    
    local response=$(curl -s "$BASE_URL/models/$public_model_id")
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/models/$public_model_id")
    
    # Verificar código HTTP 200
    if [ "$http_code" = "200" ]; then
        print_result "GET modelo público retorna HTTP 200" "PASS" "Código: $http_code, ID: $public_model_id"
    else
        print_result "GET modelo público retorna HTTP 200" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que es un objeto
    if echo "$response" | jq -e '. | type == "object"' > /dev/null 2>&1; then
        print_result "Respuesta es un objeto JSON" "PASS"
    else
        print_result "Respuesta es un objeto JSON" "FAIL"
        return
    fi
    
    # Verificar campos
    local name=$(echo "$response" | jq -r '.name' 2>/dev/null)
    local is_private=$(echo "$response" | jq -r '.isPrivate' 2>/dev/null)
    
    if [ -n "$name" ] && [ "$name" != "null" ]; then
        print_result "Modelo tiene nombre" "PASS" "Nombre: $name"
    else
        print_result "Modelo tiene nombre" "FAIL"
    fi
    
    if [ "$is_private" = "false" ]; then
        print_result "Modelo es público" "PASS" "isPrivate: false"
    else
        print_result "Modelo es público" "FAIL" "isPrivate: $is_private"
    fi
    
    # Mostrar respuesta real
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 7: GET /models/{id} - Modelo privado sin autenticación (debe retornar 401)
test_get_private_model_unauthorized() {
    print_header "TEST 7: GET /models/{id} - Modelo privado sin auth (401)"
    
    # Primero obtener un modelo privado
    local all_models=$(curl -s "$BASE_URL/models")
    local private_model_id=$(echo "$all_models" | jq -r '.[] | select(.isPrivate == true) | .id' 2>/dev/null | head -1)
    
    if [ -z "$private_model_id" ] || [ "$private_model_id" = "null" ]; then
        print_result "Encontrar modelo privado" "FAIL" "No hay modelos privados"
        return
    fi
    
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/models/$private_model_id")
    
    if [ "$http_code" = "401" ]; then
        print_result "Retorna HTTP 401 sin autenticación" "PASS" "Código: $http_code (✓ Protegido), ID: $private_model_id"
    else
        print_result "Retorna HTTP 401 sin autenticación" "FAIL" "Código: $http_code (⚠ No protegido)"
    fi
    
    # Mostrar respuesta real (mensaje de error)
    local response=$(curl -s "$BASE_URL/models/$private_model_id")
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 8: GET /models/{id} - Modelo privado con autenticación
test_get_private_model_authorized() {
    print_header "TEST 8: GET /models/{id} - Modelo privado con auth (200)"
    
    # Obtener un modelo privado
    local all_models=$(curl -s "$BASE_URL/models")
    local private_model_id=$(echo "$all_models" | jq -r '.[] | select(.isPrivate == true) | .id' 2>/dev/null | head -1)
    
    if [ -z "$private_model_id" ] || [ "$private_model_id" = "null" ]; then
        print_result "Encontrar modelo privado" "FAIL" "No hay modelos privados"
        return
    fi
    
    local response=$(curl -s -u sob:sob "$BASE_URL/models/$private_model_id")
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" -u sob:sob "$BASE_URL/models/$private_model_id")
    
    if [ "$http_code" = "200" ]; then
        print_result "GET modelo privado con auth retorna HTTP 200" "PASS" "Código: $http_code, ID: $private_model_id"
    else
        print_result "GET modelo privado con auth retorna HTTP 200" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que es el modelo correcto
    local id=$(echo "$response" | jq -r '.id' 2>/dev/null)
    local name=$(echo "$response" | jq -r '.name' 2>/dev/null)
    
    if [ "$id" = "$private_model_id" ]; then
        print_result "ID correcto en respuesta" "PASS" "Modelo: $name"
    else
        print_result "ID correcto en respuesta" "FAIL"
    fi
    
    # Mostrar respuesta real
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 9: GET /models/{id} - ID inexistente (debe retornar 404)
test_get_model_not_found() {
    print_header "TEST 9: GET /models/{id} - ID inexistente (404)"
    
    local model_id=99999
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/models/$model_id")
    
    if [ "$http_code" = "404" ]; then
        print_result "Retorna HTTP 404 para ID inexistente" "PASS" "Código: $http_code"
    else
        print_result "Retorna HTTP 404 para ID inexistente" "FAIL" "Código: $http_code (esperado: 404)"
    fi
    
    # Mostrar respuesta real (mensaje de error)
    local response=$(curl -s "$BASE_URL/models/$model_id")
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 10: POST /models - Crear modelo sin autenticación (debe retornar 401)
test_post_model_unauthorized() {
    print_header "TEST 10: POST /models - Sin autenticación (401)"
    
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" \
        -X POST \
        -H "Content-Type: application/json" \
        -d '{"name": "Test Model", "provider": {"name": "TestProvider"}}' \
        "$BASE_URL/models")
    
    if [ "$http_code" = "401" ]; then
        print_result "Retorna HTTP 401 sin autenticación" "PASS" "Código: $http_code (✓ Protegido)"
    else
        print_result "Retorna HTTP 401 sin autenticación" "FAIL" "Código: $http_code (⚠ No protegido)"
    fi
    
    # Mostrar respuesta real (mensaje de error)
    local response=$(curl -s \
        -X POST \
        -H "Content-Type: application/json" \
        -d '{"name": "Test Model", "provider": {"name": "TestProvider"}}' \
        "$BASE_URL/models")
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 11: POST /models - Crear modelo con autenticación
test_post_model_create() {
    print_header "TEST 11: POST /models - Crear modelo (autenticado)"
    
    local json_data='{
        "name": "Test Model Created",
        "provider": {"id": 1, "name": "OpenAI"},
        "summary": "Modelo de prueba",
        "description": "Descripción del modelo de prueba",
        "license": {"id": 1, "name": "Proprietary"},
        "isPrivate": false,
        "version": "1.0"
    }'
    
    local response=$(curl -s -u sob:sob \
        -X POST \
        -H "Content-Type: application/json" \
        -d "$json_data" \
        "$BASE_URL/models")
    
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" -u sob:sob \
        -X POST \
        -H "Content-Type: application/json" \
        -d "$json_data" \
        "$BASE_URL/models")
    
    if [ "$http_code" = "201" ]; then
        print_result "POST retorna HTTP 201 (Created)" "PASS" "Código: $http_code"
    else
        print_result "POST retorna HTTP 201 (Created)" "FAIL" "Código: $http_code"
        echo -e "  ${RED}→${NC} Respuesta: $response"
        return
    fi
    
    # Verificar que la respuesta contiene el modelo creado
    local created_id=$(echo "$response" | jq -r '.id' 2>/dev/null)
    local created_name=$(echo "$response" | jq -r '.name' 2>/dev/null)
    
    if [ -n "$created_id" ] && [ "$created_id" != "null" ]; then
        print_result "Modelo creado tiene ID" "PASS" "ID: $created_id, Nombre: $created_name"
    else
        print_result "Modelo creado tiene ID" "FAIL"
    fi
    
    # Mostrar respuesta real
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 12: POST /models - Validación de campos obligatorios (debe retornar 400)
test_post_model_validation() {
    print_header "TEST 12: POST /models - Validación campos obligatorios (400)"
    
    # Intentar crear modelo sin nombre
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" -u sob:sob \
        -X POST \
        -H "Content-Type: application/json" \
        -d '{"provider": {"name": "Test"}}' \
        "$BASE_URL/models")
    
    if [ "$http_code" = "400" ]; then
        print_result "Retorna HTTP 400 sin nombre" "PASS" "Código: $http_code (✓ Validación correcta)"
    else
        print_result "Retorna HTTP 400 sin nombre" "FAIL" "Código: $http_code (esperado: 400)"
    fi
    
    # Intentar crear modelo sin provider
    local http_code2=$(curl -s -o /dev/null -w "%{http_code}" -u sob:sob \
        -X POST \
        -H "Content-Type: application/json" \
        -d '{"name": "Test Model"}' \
        "$BASE_URL/models")
    
    if [ "$http_code2" = "400" ]; then
        print_result "Retorna HTTP 400 sin provider" "PASS" "Código: $http_code2 (✓ Validación correcta)"
    else
        print_result "Retorna HTTP 400 sin provider" "FAIL" "Código: $http_code2 (esperado: 400)"
    fi
    
    # Mostrar respuesta real del primer caso (sin nombre)
    local response=$(curl -s -u sob:sob \
        -X POST \
        -H "Content-Type: application/json" \
        -d '{"provider": {"name": "Test"}}' \
        "$BASE_URL/models")
    echo -e "\n${YELLOW}Respuesta real del servidor (sin nombre):${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 13: Combinación de filtros (provider + capability)
test_get_models_combined_filters() {
    print_header "TEST 13: GET /models?provider=X&capability=Y - Filtros combinados"
    
    local provider="OpenAI"
    local capability="chat-completion"
    local response=$(curl -s "$BASE_URL/models?provider=$provider&capability=$capability")
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/models?provider=$provider&capability=$capability")
    
    if [ "$http_code" = "200" ]; then
        print_result "GET con filtros combinados retorna HTTP 200" "PASS" "Código: $http_code"
    else
        print_result "GET con filtros combinados retorna HTTP 200" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que los resultados cumplen ambos criterios
    local count=$(echo "$response" | jq '. | length' 2>/dev/null)
    echo -e "  ${YELLOW}→${NC} Modelos encontrados: $count"
    
    if [ "$count" -ge 1 ]; then
        local all_match=$(echo "$response" | jq -e --arg prov "$provider" --arg cap "$capability" 'all((.provider.name == $prov) and (.capabilities | map(.name) | contains([$cap])))' 2>/dev/null)
        if [ "$all_match" = "true" ]; then
            print_result "Todos cumplen ambos filtros" "PASS" "✓ Filtros combinados funcionan"
        else
            print_result "Todos cumplen ambos filtros" "FAIL" "⚠ Filtros no funcionan correctamente"
        fi
    fi
    
    # Mostrar respuesta real
    echo -e "\n${YELLOW}Respuesta real del servidor:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# =============================================================================
# EJECUCIÓN DE TESTS
# =============================================================================

main() {
    clear
    echo -e "${BLUE}"
    echo "╔════════════════════════════════════════════════════════════════╗"
    echo "║                                                                ║"
    echo "║         BATERÍA DE TESTS - API REST MODELS                     ║"
    echo "║                                                                ║"
    echo "╚════════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
    
    # Verificaciones previas
    check_dependencies
    check_server
    
    # Preguntar si poblar la base de datos
    echo -e "\n${YELLOW}¿Deseas repoblar la base de datos antes de ejecutar los tests?${NC}"
    echo -e "${YELLOW}Esto ejecutará install.jsp y reiniciará todos los datos.${NC}"
    read -p "Repoblar BD? (s/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[SsYy]$ ]]; then
        populate_database
    fi
    
    # Ejecutar tests
    test_get_all_models
    test_get_models_by_provider
    test_get_models_by_capability
    test_get_models_by_two_capabilities
    test_get_models_too_many_capabilities
    test_get_public_model
    test_get_private_model_unauthorized
    test_get_private_model_authorized
    test_get_model_not_found
    test_post_model_unauthorized
    test_post_model_create
    test_post_model_validation
    test_get_models_combined_filters
    
    # Resumen final
    print_header "RESUMEN DE RESULTADOS"
    
    echo -e "Total de tests ejecutados: ${BLUE}$TESTS_TOTAL${NC}"
    echo -e "Tests exitosos: ${GREEN}$TESTS_PASSED${NC}"
    echo -e "Tests fallidos: ${RED}$TESTS_FAILED${NC}"
    
    if [ $TESTS_FAILED -eq 0 ]; then
        echo -e "\n${GREEN}╔════════════════════════════════════════╗${NC}"
        echo -e "${GREEN}║  ✓ TODOS LOS TESTS PASARON             ║${NC}"
        echo -e "${GREEN}╚════════════════════════════════════════╝${NC}\n"
        exit 0
    else
        echo -e "\n${RED}╔════════════════════════════════════════╗${NC}"
        echo -e "${RED}║  ✗ ALGUNOS TESTS FALLARON              ║${NC}"
        echo -e "${RED}╚════════════════════════════════════════╝${NC}\n"
        exit 1
    fi
}

# Ejecutar script principal
main
