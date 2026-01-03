<%-- 
    detall.jsp - Página de detalle de un modelo de IA
    
    Esta página muestra toda la información de un modelo:
    - Datos públicos: nombre, proveedor, resumen, descripción, capacidades
    - Datos privados (solo si estás logueado): versión y fecha de actualización
    
    Variables que recibimos del controlador (ModelDetailController):
    - model: el objeto ModelDTO con todos los datos del modelo
    - authenticated: boolean que indica si el usuario está logueado
    - username: nombre del usuario logueado (solo si authenticated=true)
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Importamos JSTL para usar <c:if>, <c:forEach>, <c:url> --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- Importamos fmt para formatear fechas de forma bonita --%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <title>${model.name} - Detall del model</title>
</head>
<body>

    <%-- 
        CABECERA: Estado de la sesión del usuario
        Aquí mostramos si está logueado o no, y los enlaces correspondientes.
    --%>
    <div>
        <c:choose>
            <%-- Si el usuario está autenticado, mostramos bienvenida y logout --%>
            <c:when test="${authenticated}">
                <b>Benvingut ${username}!</b> | 
                <a href="<c:url value='/login/logout'/>">Tancar sessió</a>
            </c:when>
            <%-- Si no está autenticado, mostramos enlace al login --%>
            <c:otherwise>
                <a href="<c:url value='/login'/>">Iniciar sessió</a>
            </c:otherwise>
        </c:choose>
        
        <%-- Enlace para volver al listado --%>
        | <a href="<c:url value='/'/>">Tornar al llistat</a>
    </div>
    
    <hr>
    
    <%-- 
        SECCIÓN PRINCIPAL: Información del modelo
    --%>
    
    <%-- Título: nombre del modelo --%>
    <h1>${model.name}</h1>
    
    <%-- 
        Proveedor del modelo (empresa que lo ha creado).
        Comprobamos que exista antes de mostrarlo para evitar errores.
    --%>
    <c:if test="${not empty model.provider}">
        <p>
            <b>Proveïdor:</b> ${model.provider.name}
            <%-- Si tiene país, también lo mostramos --%>
            <c:if test="${not empty model.provider.country}">
                (${model.provider.country})
            </c:if>
        </p>
    </c:if>
    
    <%-- Resumen corto del modelo --%>
    <c:if test="${not empty model.summary}">
        <p><b>Resum:</b> ${model.summary}</p>
    </c:if>
    
    <%-- Descripción larga del modelo --%>
    <c:if test="${not empty model.description}">
        <h2>Descripció</h2>
        <p>${model.description}</p>
    </c:if>
    
    <%-- 
        Capacidades del modelo (las 3 habilidades destacadas).
        Usamos <c:forEach> para recorrer la lista una a una.
    --%>
    <c:if test="${not empty model.capabilities}">
        <h2>Capacitats destacades</h2>
        <ul>
            <%-- Con forEach recorremos cada capacidad de la lista --%>
            <c:forEach var="capability" items="${model.capabilities}">
                <li>${capability.name}</li>
            </c:forEach>
        </ul>
    </c:if>
    
    <%-- 
        Licencia del modelo.
    --%>
    <c:if test="${not empty model.license}">
        <p><b>Llicència:</b> ${model.license.name}</p>
    </c:if>
    
    <hr>
    
    <%-- 
        SECCIÓN PRIVADA: Solo visible para usuarios autenticados
        
        Según el enunciado (Figura 2), los usuarios logueados pueden ver
        información extra como la versión y la fecha de última actualización.
        Usamos <c:if> para comprobar si el usuario está autenticado.
    --%>
    <c:if test="${authenticated}">
        <h2>Informació privada</h2>
        <p><i>(Només visible per usuaris identificats)</i></p>
        
        <%-- Versión del modelo --%>
        <c:if test="${not empty model.version}">
            <p><b>Versió:</b> ${model.version}</p>
        </c:if>
        
        <%-- 
            Fecha de última actualización.
            Usamos fmt:formatDate para que la fecha salga bonita (día/mes/año).
        --%>
        <c:if test="${not empty model.lastUpdateDate}">
            <p>
                <b>Última actualització:</b> 
                <fmt:formatDate value="${model.lastUpdateDate}" pattern="dd/MM/yyyy"/>
            </p>
        </c:if>
        
        <%-- Fecha de entrenamiento (si existe) --%>
        <c:if test="${not empty model.trainingDate}">
            <p>
                <b>Data d'entrenament:</b> 
                <fmt:formatDate value="${model.trainingDate}" pattern="dd/MM/yyyy"/>
            </p>
        </c:if>
    </c:if>
    
    <%-- 
        Si el usuario NO está logueado, le avisamos de que hay más info
        y le invitamos a identificarse.
    --%>
    <c:if test="${not authenticated}">
        <hr>
        <p>
            <i>Inicia sessió per veure informació addicional del model.</i>
            <a href="<c:url value='/login'/>">Identificar-se</a>
        </p>
    </c:if>

</body>
</html>
