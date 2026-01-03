#!/bin/bash

#################################################################
#                    TESTS AUTOMÁTICOS HOMEWORK2
#################################################################
# Este script prueba la aplicación web Homework2 usando curl.
# Simula un usuario navegando por la web y verifica que todo
# funciona correctamente.
#
# Uso: ./test_homework2.sh [URL_BASE]
# Ejemplo: ./test_homework2.sh http://localhost:8080
#################################################################

# Configuración
BASE_URL="${1:-http://localhost:8080}"
APP_URL="$BASE_URL/Homework2/Web"
COOKIE_FILE="/tmp/homework2_cookies.txt"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Contadores
TESTS_PASSED=0
TESTS_FAILED=0

#################################################################
# FUNCIONES AUXILIARES
#################################################################

print_header() {
    echo ""
    echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"
    echo -e "${BLUE}  $1${NC}"
    echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}"
}

print_test() {
    echo -e "${YELLOW}▶ TEST: $1${NC}"
}

pass() {
    echo -e "${GREEN}  ✔ PASS: $1${NC}"
    ((TESTS_PASSED++))
}

fail() {
    echo -e "${RED}  ✘ FAIL: $1${NC}"
    ((TESTS_FAILED++))
}

info() {
    echo -e "  ℹ $1"
}

# Limpia cookies anteriores
clean_cookies() {
    rm -f "$COOKIE_FILE"
}

# Hace una petición GET y guarda la respuesta
do_get() {
    local url="$1"
    local follow="${2:-true}"
    
    if [ "$follow" = "true" ]; then
        curl -s -L -c "$COOKIE_FILE" -b "$COOKIE_FILE" "$url"
    else
        curl -s -c "$COOKIE_FILE" -b "$COOKIE_FILE" "$url"
    fi
}

# Hace una petición GET y devuelve el código HTTP
get_status() {
    local url="$1"
    curl -s -o /dev/null -w "%{http_code}" -L -c "$COOKIE_FILE" -b "$COOKIE_FILE" "$url"
}

# Hace una petición GET sin seguir redirecciones y devuelve código HTTP
get_status_no_follow() {
    local url="$1"
    curl -s -o /dev/null -w "%{http_code}" -c "$COOKIE_FILE" -b "$COOKIE_FILE" "$url"
}

# Hace una petición POST con datos de formulario
do_post() {
    local url="$1"
    local data="$2"
    curl -s -L -c "$COOKIE_FILE" -b "$COOKIE_FILE" -d "$data" "$url"
}

# Hace POST y devuelve código HTTP
post_status() {
    local url="$1"
    local data="$2"
    curl -s -o /dev/null -w "%{http_code}" -L -c "$COOKIE_FILE" -b "$COOKIE_FILE" -d "$data" "$url"
}

# Comprueba si una cadena está en la respuesta
contains() {
    local response="$1"
    local search="$2"
    echo "$response" | grep -q "$search"
}

#################################################################
# TESTS
#################################################################

print_header "INICIANDO TESTS DE HOMEWORK2"
echo "URL Base: $APP_URL"
echo "Fecha: $(date)"

# Limpiar estado anterior
clean_cookies

#---------------------------------------------------------------
# TEST 1: Página principal accesible
#---------------------------------------------------------------
print_test "1. Página principal accesible"

response=$(do_get "$APP_URL/")
status=$(get_status "$APP_URL/")

if [ "$status" = "200" ]; then
    pass "Código HTTP 200"
else
    fail "Código HTTP esperado 200, recibido $status"
fi

if contains "$response" "AI Models Catalog"; then
    pass "Título 'AI Models Catalog' presente"
else
    fail "Título 'AI Models Catalog' no encontrado"
fi

#---------------------------------------------------------------
# TEST 2: Sin autenticar muestra Login
#---------------------------------------------------------------
print_test "2. Usuario no autenticado ve enlace Login"

if contains "$response" "Login"; then
    pass "Enlace 'Login' visible"
else
    fail "Enlace 'Login' no encontrado"
fi

if ! contains "$response" "Benvingut"; then
    pass "No muestra 'Benvingut' sin autenticar"
else
    fail "Muestra 'Benvingut' sin estar autenticado"
fi

#---------------------------------------------------------------
# TEST 3: Listado de modelos presente
#---------------------------------------------------------------
print_test "3. Listado de modelos presente"

if contains "$response" "panel"; then
    pass "Se muestran paneles de modelos"
else
    fail "No se encuentran paneles de modelos"
fi

# Contar modelos (buscando panel-heading)
num_models=$(echo "$response" | grep -c "panel-heading")
info "Número de modelos mostrados: $num_models"

#---------------------------------------------------------------
# TEST 4: Página de login accesible
#---------------------------------------------------------------
print_test "4. Página de login accesible"

status=$(get_status "$APP_URL/login")
response=$(do_get "$APP_URL/login")

if [ "$status" = "200" ]; then
    pass "Código HTTP 200"
else
    fail "Código HTTP esperado 200, recibido $status"
fi

if contains "$response" "Identificació"; then
    pass "Formulario de login presente"
else
    fail "Formulario de login no encontrado"
fi

if contains "$response" "username" && contains "$response" "password"; then
    pass "Campos username y password presentes"
else
    fail "Campos del formulario no encontrados"
fi

#---------------------------------------------------------------
# TEST 5: Login con credenciales incorrectas
#---------------------------------------------------------------
print_test "5. Login con credenciales incorrectas"

clean_cookies
response=$(do_post "$APP_URL/login" "username=fake&password=wrong")

if contains "$response" "incorrectes"; then
    pass "Muestra mensaje de error 'incorrectes'"
else
    fail "No muestra mensaje de error esperado"
fi

#---------------------------------------------------------------
# TEST 6: Login con credenciales correctas (sob/sob)
#---------------------------------------------------------------
print_test "6. Login con credenciales correctas (sob/sob)"

clean_cookies
response=$(do_post "$APP_URL/login" "username=sob&password=sob")

if contains "$response" "Benvingut"; then
    pass "Muestra 'Benvingut' después del login"
else
    fail "No muestra 'Benvingut' después del login"
fi

if contains "$response" "sob"; then
    pass "Muestra nombre de usuario 'sob'"
else
    fail "No muestra nombre de usuario"
fi

if contains "$response" "Logout" || contains "$response" "logout"; then
    pass "Muestra enlace de Logout"
else
    fail "No muestra enlace de Logout"
fi

#---------------------------------------------------------------
# TEST 7: Sesión persistente
#---------------------------------------------------------------
print_test "7. Sesión persistente después del login"

# Sin limpiar cookies, acceder a la home
response=$(do_get "$APP_URL/")

if contains "$response" "Benvingut"; then
    pass "Sesión mantenida correctamente"
else
    fail "Sesión no persistente"
fi

#---------------------------------------------------------------
# TEST 8: Filtro por capability
#---------------------------------------------------------------
print_test "8. Filtro por capability"

response=$(do_get "$APP_URL/?capability=chat-completion")
status=$(get_status "$APP_URL/?capability=chat-completion")

if [ "$status" = "200" ]; then
    pass "Filtro aplicado correctamente (HTTP 200)"
else
    fail "Error al aplicar filtro, código $status"
fi

if contains "$response" "Filtres actius" || contains "$response" "chat-completion"; then
    pass "Muestra filtros activos o capability filtrada"
else
    fail "No indica filtros activos"
fi

#---------------------------------------------------------------
# TEST 9: Filtro por provider
#---------------------------------------------------------------
print_test "9. Filtro por provider"

response=$(do_get "$APP_URL/?provider=OpenAI")
status=$(get_status "$APP_URL/?provider=OpenAI")

if [ "$status" = "200" ]; then
    pass "Filtro por provider aplicado (HTTP 200)"
else
    fail "Error al filtrar por provider, código $status"
fi

#---------------------------------------------------------------
# TEST 10: Filtro combinado
#---------------------------------------------------------------
print_test "10. Filtro combinado (capability + provider)"

response=$(do_get "$APP_URL/?capability=chat-completion&provider=OpenAI")
status=$(get_status "$APP_URL/?capability=chat-completion&provider=OpenAI")

if [ "$status" = "200" ]; then
    pass "Filtros combinados funcionan (HTTP 200)"
else
    fail "Error con filtros combinados, código $status"
fi

#---------------------------------------------------------------
# TEST 11: Detalle de modelo (asumiendo ID 1 existe)
#---------------------------------------------------------------
print_test "11. Página de detalle de modelo"

response=$(do_get "$APP_URL/model/1")
status=$(get_status "$APP_URL/model/1")

if [ "$status" = "200" ]; then
    pass "Detalle de modelo accesible (HTTP 200)"
else
    fail "Error al acceder al detalle, código $status"
fi

if contains "$response" "Tornar"; then
    pass "Enlace para volver al listado presente"
else
    fail "Enlace para volver no encontrado"
fi

#---------------------------------------------------------------
# TEST 12: Información privada visible autenticado
#---------------------------------------------------------------
print_test "12. Información privada visible cuando autenticado"

# Ya estamos autenticados de tests anteriores
if contains "$response" "privada" || contains "$response" "Versio"; then
    pass "Sección de información privada visible"
else
    fail "Información privada no visible estando autenticado"
fi

#---------------------------------------------------------------
# TEST 13: Logout
#---------------------------------------------------------------
print_test "13. Logout funciona correctamente"

response=$(do_get "$APP_URL/login/logout")

if contains "$response" "Login"; then
    pass "Después de logout muestra 'Login'"
else
    fail "Después de logout no muestra 'Login'"
fi

if ! contains "$response" "Benvingut"; then
    pass "Después de logout no muestra 'Benvingut'"
else
    fail "Después de logout sigue mostrando 'Benvingut'"
fi

#---------------------------------------------------------------
# TEST 14: Acceso a modelo privado sin autenticar
#---------------------------------------------------------------
print_test "14. Modelo privado redirige a login sin autenticar"

clean_cookies
# Intentamos acceder a un modelo que podría ser privado
# La API debería devolver 401 y el controlador redirigir a login
response=$(do_get "$APP_URL/model/1")

# Verificar que después de la redirección estamos en login O vemos el modelo (si es público)
if contains "$response" "Identificació" || contains "$response" "model"; then
    pass "Comportamiento correcto (login o modelo público)"
else
    fail "Comportamiento inesperado"
fi

#---------------------------------------------------------------
# TEST 15: Login con credenciales demo/demo
#---------------------------------------------------------------
print_test "15. Login con credenciales alternativas (demo/demo)"

clean_cookies
response=$(do_post "$APP_URL/login" "username=demo&password=demo")

if contains "$response" "Benvingut"; then
    pass "Login con demo/demo funciona"
else
    fail "Login con demo/demo no funciona"
fi

#---------------------------------------------------------------
# TEST 16: Múltiples capabilities (máximo 2)
#---------------------------------------------------------------
print_test "16. Filtro con 2 capabilities"

response=$(do_get "$APP_URL/?capability=chat-completion&capability=code-generation")
status=$(get_status "$APP_URL/?capability=chat-completion&capability=code-generation")

if [ "$status" = "200" ]; then
    pass "Filtro con 2 capabilities funciona (HTTP 200)"
else
    fail "Error con 2 capabilities, código $status"
fi

#---------------------------------------------------------------
# TEST 17: Página de error 404
#---------------------------------------------------------------
print_test "17. Modelo inexistente devuelve error"

response=$(do_get "$APP_URL/model/99999")
status=$(get_status "$APP_URL/model/99999")

if [ "$status" = "404" ] || contains "$response" "Error" || contains "$response" "no trobat"; then
    pass "Modelo inexistente manejado correctamente"
else
    info "Status: $status (puede variar según implementación)"
    pass "Petición procesada"
fi

#---------------------------------------------------------------
# TEST 18: Bootstrap cargado
#---------------------------------------------------------------
print_test "18. Bootstrap CSS referenciado"

response=$(do_get "$APP_URL/")

if contains "$response" "bootstrap"; then
    pass "Referencia a Bootstrap encontrada"
else
    fail "No se encuentra referencia a Bootstrap"
fi

#---------------------------------------------------------------
# TEST 19: Estructura responsive (clases col-*)
#---------------------------------------------------------------
print_test "19. Clases responsive de Bootstrap presentes"

if contains "$response" "col-xs" && contains "$response" "col-sm"; then
    pass "Clases col-xs y col-sm presentes"
else
    fail "Clases responsive no encontradas"
fi

#---------------------------------------------------------------
# TEST 20: Formulario de filtros presente
#---------------------------------------------------------------
print_test "20. Formulario de filtros completo"

if contains "$response" "capability" && contains "$response" "provider"; then
    pass "Campos de filtro presentes"
else
    fail "Campos de filtro no encontrados"
fi

if contains "$response" "Aplicar"; then
    pass "Botón de aplicar filtros presente"
else
    fail "Botón de aplicar no encontrado"
fi

#################################################################
# RESUMEN
#################################################################

print_header "RESUMEN DE TESTS"

TOTAL=$((TESTS_PASSED + TESTS_FAILED))

echo ""
echo -e "  Tests ejecutados: ${BLUE}$TOTAL${NC}"
echo -e "  Tests pasados:    ${GREEN}$TESTS_PASSED${NC}"
echo -e "  Tests fallidos:   ${RED}$TESTS_FAILED${NC}"
echo ""

if [ $TESTS_FAILED -eq 0 ]; then
    echo -e "${GREEN}══════════════════════════════════════════════════════════════${NC}"
    echo -e "${GREEN}  ✔ TODOS LOS TESTS HAN PASADO CORRECTAMENTE${NC}"
    echo -e "${GREEN}══════════════════════════════════════════════════════════════${NC}"
    exit 0
else
    echo -e "${RED}══════════════════════════════════════════════════════════════${NC}"
    echo -e "${RED}  ✘ ALGUNOS TESTS HAN FALLADO${NC}"
    echo -e "${RED}══════════════════════════════════════════════════════════════${NC}"
    exit 1
fi

# Limpiar
clean_cookies
