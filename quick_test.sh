#!/bin/bash

BASE_URL="http://localhost:8080/practica-sob/rest/api/v1"

echo "========================================="
echo "QUICK TEST - API REST CUSTOMERS"
echo "========================================="
echo ""

# Test 1: GET all customers
echo "1. GET /customer (Listar todos)"
echo "-----------------------------------"
curl -s "$BASE_URL/customer" | jq '.' 2>/dev/null || curl -s "$BASE_URL/customer"
echo ""
echo ""

# Test 2: GET customer by ID
echo "2. GET /customer/1 (Customer específico)"
echo "-----------------------------------"
curl -s "$BASE_URL/customer/1" | jq '.' 2>/dev/null || curl -s "$BASE_URL/customer/1"
echo ""
echo ""

# Test 3: PUT sin autenticación (debería fallar con 401)
echo "3. PUT /customer/1 sin auth (debe fallar con 401)"
echo "-----------------------------------"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" \
    -X PUT \
    -H "Content-Type: application/json" \
    -d '{"telefono": "+34600000000"}' \
    "$BASE_URL/customer/1")
echo "HTTP Status: $HTTP_CODE"
if [ "$HTTP_CODE" = "401" ]; then
    echo "✓ Protección funcionando correctamente"
else
    echo "✗ ERROR: Debería retornar 401"
fi
echo ""
echo ""

# Test 4: PUT con autenticación (debería funcionar)
echo "4. PUT /customer/1 con auth (actualizar teléfono)"
echo "-----------------------------------"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" \
    -u sob:sob \
    -X PUT \
    -H "Content-Type: application/json" \
    -d '{"telefono": "+34611222333"}' \
    "$BASE_URL/customer/1")
echo "HTTP Status: $HTTP_CODE"
if [ "$HTTP_CODE" = "204" ]; then
    echo "✓ Actualización exitosa"
    # Verificar el cambio
    echo ""
    echo "Verificando cambio..."
    sleep 1
    curl -s "$BASE_URL/customer/1" | jq '.telefono' 2>/dev/null
else
    echo "✗ ERROR: No se pudo actualizar"
fi
echo ""
echo ""

# Test 5: Restaurar teléfono original
echo "5. Restaurar teléfono original"
echo "-----------------------------------"
curl -s -u sob:sob \
    -X PUT \
    -H "Content-Type: application/json" \
    -d '{"telefono": "+34612345678"}' \
    "$BASE_URL/customer/1" > /dev/null
echo "✓ Teléfono restaurado a +34612345678"
echo ""

echo "========================================="
echo "TESTS COMPLETADOS"
echo "========================================="
