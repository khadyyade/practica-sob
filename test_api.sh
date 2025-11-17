#!/bin/bash

# =============================================================================
# BATERÍA DE TESTS PARA LA API REST DE CUSTOMERS
# =============================================================================
# Este script ejecuta una serie de tests sobre la API REST para verificar
# que todos los endpoints funcionan correctamente.
# 
# REQUISITOS:
# - Servidor GlassFish corriendo en localhost:8080
# - Base de datos poblada (ejecutar install.jsp primero)
# - curl y jq instalados
#Voy a crear una solución usando un EJB @Singleton que se ejecute automáticamente al inicio:


# USO:
#   chmod +x test_api.sh
#   ./test_api.sh
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
    
    if curl -s -f "$BASE_URL/customer" > /dev/null 2>&1; then
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
        echo "Respuesta: $response"
    fi
    
    # Esperar un momento para que los datos se persistan
    sleep 2
}

# =============================================================================
# TESTS
# =============================================================================

# TEST 1: GET /customer (listar todos los customers)
test_get_all_customers() {
    print_header "TEST 1: GET /customer - Listar todos los customers"
    
    local response=$(curl -s "$BASE_URL/customer")
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/customer")
    
    # Verificar código HTTP 200
    if [ "$http_code" = "200" ]; then
        print_result "GET /customer retorna HTTP 200" "PASS" "Código: $http_code"
    else
        print_result "GET /customer retorna HTTP 200" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que es un array JSON
    if echo "$response" | jq -e '. | type == "array"' > /dev/null 2>&1; then
        print_result "Respuesta es un array JSON" "PASS"
    else
        print_result "Respuesta es un array JSON" "FAIL" "No es un array válido"
        return
    fi
    
    # Verificar que hay al menos 2 customers
    local count=$(echo "$response" | jq '. | length')
    if [ "$count" -ge 2 ]; then
        print_result "Contiene al menos 2 customers" "PASS" "Total: $count customers"
    else
        print_result "Contiene al menos 2 customers" "FAIL" "Total: $count customers"
    fi
    
    # Verificar estructura del primer customer
    local has_id=$(echo "$response" | jq -e '.[0] | has("id")' 2>/dev/null)
    local has_username=$(echo "$response" | jq -e '.[0] | has("username")' 2>/dev/null)
    local has_telefono=$(echo "$response" | jq -e '.[0] | has("telefono")' 2>/dev/null)
    
    if [ "$has_id" = "true" ] && [ "$has_username" = "true" ]; then
        print_result "Customers tienen campos id y username" "PASS"
    else
        print_result "Customers tienen campos id y username" "FAIL"
    fi
    
    if [ "$has_telefono" = "true" ]; then
        print_result "Customers tienen campo telefono" "PASS"
    else
        print_result "Customers tienen campo telefono" "FAIL"
    fi
    
    # Verificar que NO contiene password
    local has_password=$(echo "$response" | jq -e '.[0] | has("password")' 2>/dev/null)
    if [ "$has_password" = "false" ] || [ "$has_password" = "null" ]; then
        print_result "NO expone el campo password (seguridad)" "PASS" "✓ Seguro"
    else
        print_result "NO expone el campo password (seguridad)" "FAIL" "⚠ VULNERABILIDAD DE SEGURIDAD"
    fi
    
    # Verificar HATEOAS (ultimoModeloVisitado)
    local has_modelo=$(echo "$response" | jq -e '.[0] | has("ultimoModeloVisitado")' 2>/dev/null)
    if [ "$has_modelo" = "true" ]; then
        local modelo_nombre=$(echo "$response" | jq -e '.[0].ultimoModeloVisitado | has("nombre")' 2>/dev/null)
        local modelo_link=$(echo "$response" | jq -e '.[0].ultimoModeloVisitado | has("link")' 2>/dev/null)
        
        if [ "$modelo_nombre" = "true" ] && [ "$modelo_link" = "true" ]; then
            print_result "HATEOAS incluye nombre y link del modelo" "PASS" "✓ Implementación completa"
        else
            print_result "HATEOAS incluye nombre y link del modelo" "FAIL"
        fi
    else
        print_result "HATEOAS presente (opcional si no hay modelo)" "PASS" "Customer sin modelo visitado"
    fi
    
    # Mostrar ejemplo de respuesta
    echo -e "\n${YELLOW}Ejemplo de respuesta:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.[0]' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 2: GET /customer/{id} (obtener un customer específico)
test_get_customer_by_id() {
    print_header "TEST 2: GET /customer/{id} - Obtener customer por ID"
    
    local customer_id=1
    local response=$(curl -s "$BASE_URL/customer/$customer_id")
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/customer/$customer_id")
    
    # Verificar código HTTP 200
    if [ "$http_code" = "200" ]; then
        print_result "GET /customer/1 retorna HTTP 200" "PASS" "Código: $http_code"
    else
        print_result "GET /customer/1 retorna HTTP 200" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que es un objeto JSON
    if echo "$response" | jq -e '. | type == "object"' > /dev/null 2>&1; then
        print_result "Respuesta es un objeto JSON" "PASS"
    else
        print_result "Respuesta es un objeto JSON" "FAIL"
        return
    fi
    
    # Verificar campos requeridos
    local id=$(echo "$response" | jq -r '.id' 2>/dev/null)
    local username=$(echo "$response" | jq -r '.username' 2>/dev/null)
    local telefono=$(echo "$response" | jq -r '.telefono' 2>/dev/null)
    
    if [ "$id" = "$customer_id" ]; then
        print_result "ID correcto en respuesta" "PASS" "ID: $id"
    else
        print_result "ID correcto en respuesta" "FAIL" "Esperado: $customer_id, Obtenido: $id"
    fi
    
    if [ -n "$username" ] && [ "$username" != "null" ]; then
        print_result "Username presente" "PASS" "Username: $username"
    else
        print_result "Username presente" "FAIL"
    fi
    
    if [ -n "$telefono" ] && [ "$telefono" != "null" ]; then
        print_result "Teléfono presente" "PASS" "Teléfono: $telefono"
    else
        print_result "Teléfono presente" "FAIL"
    fi
    
    # Verificar HATEOAS
    local modelo_nombre=$(echo "$response" | jq -r '.ultimoModeloVisitado.nombre' 2>/dev/null)
    local modelo_link=$(echo "$response" | jq -r '.ultimoModeloVisitado.link' 2>/dev/null)
    
    if [ -n "$modelo_nombre" ] && [ "$modelo_nombre" != "null" ]; then
        print_result "HATEOAS incluye nombre del modelo" "PASS" "Modelo: $modelo_nombre"
    fi
    
    if [ -n "$modelo_link" ] && [ "$modelo_link" != "null" ]; then
        print_result "HATEOAS incluye link del modelo" "PASS" "Link: $modelo_link"
    fi
    
    # Mostrar respuesta completa
    echo -e "\n${YELLOW}Respuesta completa:${NC}"
    if command -v jq > /dev/null 2>&1; then
        echo "$response" | jq '.' 2>/dev/null || echo "$response"
    else
        echo "$response"
    fi
}

# TEST 3: GET /customer/{id} con ID inexistente (debe retornar 404)
test_get_customer_not_found() {
    print_header "TEST 3: GET /customer/{id} - ID inexistente (404)"
    
    local customer_id=99999
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" "$BASE_URL/customer/$customer_id")
    
    if [ "$http_code" = "404" ]; then
        print_result "Retorna HTTP 404 para ID inexistente" "PASS" "Código: $http_code"
    else
        print_result "Retorna HTTP 404 para ID inexistente" "FAIL" "Código: $http_code (esperado: 404)"
    fi
}

# TEST 4: PUT /customer/{id} sin autenticación (debe retornar 401)
test_put_customer_unauthorized() {
    print_header "TEST 4: PUT /customer/{id} - Sin autenticación (401)"
    
    local customer_id=1
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" \
        -X PUT \
        -H "Content-Type: application/json" \
        -d '{"telefono": "+34600000000"}' \
        "$BASE_URL/customer/$customer_id")
    
    if [ "$http_code" = "401" ]; then
        print_result "Retorna HTTP 401 sin autenticación" "PASS" "Código: $http_code (✓ Protegido)"
    else
        print_result "Retorna HTTP 401 sin autenticación" "FAIL" "Código: $http_code (⚠ No protegido)"
    fi
}

# TEST 5: PUT /customer/{id} con autenticación - actualizar teléfono
test_put_customer_update_telefono() {
    print_header "TEST 5: PUT /customer/{id} - Actualizar teléfono (autenticado)"
    
    local customer_id=1
    local new_telefono="+34611222333"
    
    # Realizar PUT con autenticación
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" \
        -u sob:sob \
        -X PUT \
        -H "Content-Type: application/json" \
        -d "{\"telefono\": \"$new_telefono\"}" \
        "$BASE_URL/customer/$customer_id")
    
    if [ "$http_code" = "204" ]; then
        print_result "PUT retorna HTTP 204 (No Content)" "PASS" "Código: $http_code"
    else
        print_result "PUT retorna HTTP 204 (No Content)" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que el teléfono se actualizó
    sleep 1
    local response=$(curl -s "$BASE_URL/customer/$customer_id")
    local updated_telefono=$(echo "$response" | jq -r '.telefono' 2>/dev/null)
    
    if [ "$updated_telefono" = "$new_telefono" ]; then
        print_result "Teléfono actualizado correctamente" "PASS" "Nuevo teléfono: $updated_telefono"
    else
        print_result "Teléfono actualizado correctamente" "FAIL" "Esperado: $new_telefono, Obtenido: $updated_telefono"
    fi
    
    # Restaurar teléfono original
    curl -s -u sob:sob \
        -X PUT \
        -H "Content-Type: application/json" \
        -d '{"telefono": "+34612345678"}' \
        "$BASE_URL/customer/$customer_id" > /dev/null
}

# TEST 6: PUT /customer/{id} - actualizar ultimoModeloVisitado
test_put_customer_update_modelo() {
    print_header "TEST 6: PUT /customer/{id} - Actualizar último modelo visitado"
    
    local customer_id=2
    local new_modelo_id=1
    
    # Realizar PUT con autenticación
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" \
        -u demo:demo \
        -X PUT \
        -H "Content-Type: application/json" \
        -d "{\"ultimoModeloVisitadoId\": $new_modelo_id}" \
        "$BASE_URL/customer/$customer_id")
    
    if [ "$http_code" = "204" ]; then
        print_result "PUT retorna HTTP 204 (No Content)" "PASS" "Código: $http_code"
    else
        print_result "PUT retorna HTTP 204 (No Content)" "FAIL" "Código: $http_code"
        return
    fi
    
    # Verificar que el modelo se actualizó
    sleep 1
    local response=$(curl -s "$BASE_URL/customer/$customer_id")
    local updated_modelo_id=$(echo "$response" | jq -r '.ultimoModeloVisitado.id' 2>/dev/null)
    
    if [ "$updated_modelo_id" = "$new_modelo_id" ]; then
        print_result "Modelo actualizado correctamente" "PASS" "Nuevo modelo ID: $updated_modelo_id"
    else
        print_result "Modelo actualizado correctamente" "FAIL" "Esperado: $new_modelo_id, Obtenido: $updated_modelo_id"
    fi
    
    # Mostrar el modelo completo
    local modelo_nombre=$(echo "$response" | jq -r '.ultimoModeloVisitado.nombre' 2>/dev/null)
    if [ -n "$modelo_nombre" ] && [ "$modelo_nombre" != "null" ]; then
        echo -e "  ${YELLOW}→${NC} Modelo visitado: $modelo_nombre"
    fi
}

# TEST 7: PUT /customer/{id} - modelo inexistente (debe retornar 404)
test_put_customer_invalid_modelo() {
    print_header "TEST 7: PUT /customer/{id} - Modelo inexistente (404)"
    
    local customer_id=1
    local invalid_modelo_id=99999
    
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" \
        -u sob:sob \
        -X PUT \
        -H "Content-Type: application/json" \
        -d "{\"ultimoModeloVisitadoId\": $invalid_modelo_id}" \
        "$BASE_URL/customer/$customer_id")
    
    if [ "$http_code" = "404" ]; then
        print_result "Retorna HTTP 404 para modelo inexistente" "PASS" "Código: $http_code (✓ Validación correcta)"
    else
        print_result "Retorna HTTP 404 para modelo inexistente" "FAIL" "Código: $http_code (esperado: 404)"
    fi
}

# TEST 8: PUT /customer/{id} - JSON vacío (debe retornar 400)
test_put_customer_empty_json() {
    print_header "TEST 8: PUT /customer/{id} - JSON vacío (400)"
    
    local customer_id=1
    
    local http_code=$(curl -s -o /dev/null -w "%{http_code}" \
        -u sob:sob \
        -X PUT \
        -H "Content-Type: application/json" \
        -d '{}' \
        "$BASE_URL/customer/$customer_id")
    
    if [ "$http_code" = "400" ]; then
        print_result "Retorna HTTP 400 para JSON vacío" "PASS" "Código: $http_code (✓ Validación correcta)"
    else
        print_result "Retorna HTTP 400 para JSON vacío" "FAIL" "Código: $http_code (esperado: 400)"
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
    echo "║         BATERÍA DE TESTS - API REST CUSTOMERS                  ║"
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
    test_get_all_customers
    test_get_customer_by_id
    test_get_customer_not_found
    test_put_customer_unauthorized
    test_put_customer_update_telefono
    test_put_customer_update_modelo
    test_put_customer_invalid_modelo
    test_put_customer_empty_json
    
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
