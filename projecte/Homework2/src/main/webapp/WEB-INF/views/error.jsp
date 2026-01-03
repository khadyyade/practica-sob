<%-- 
    error.jsp - Página de errores amigable
    
    Esta página muestra los errores de forma clara para el usuario,
    en vez de mostrar los errores técnicos feos del servidor.
    
    Variables que recibimos del controlador (ErrorController):
    - errorCode: código del error (404, 500, etc.)
    - errorTitle: título descriptivo ("Pàgina no trobada", etc.)
    - errorMessage: mensaje explicando qué ha pasado
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Importamos JSTL para poder usar <c:url> y ${variable} --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <%-- El título de la pestaña muestra el código de error --%>
    <title>Error ${errorCode}</title>
</head>
<body>

    <%-- 
        Mostramos el código de error grande para que se vea claro.
        El código nos dice qué tipo de error ha pasado (404 = no encontrado, etc.)
    --%>
    <h1>Error ${errorCode}</h1>
    
    <%-- 
        Título descriptivo del error.
        Es más amigable que solo el número (ej: "Pàgina no trobada" en vez de "404")
    --%>
    <h2>${errorTitle}</h2>
    
    <%-- 
        Mensaje con más detalles sobre qué ha pasado.
        Puede ser un mensaje genérico o específico según el error.
    --%>
    <p>${errorMessage}</p>
    
    <hr>
    
    <%-- 
        Botón/enlace para volver al inicio.
        Usamos c:url para que la ruta funcione correctamente.
    --%>
    <p>
        <a href="<c:url value='/'/>">
            <b>Tornar a l'inici</b>
        </a>
    </p>

</body>
</html>
