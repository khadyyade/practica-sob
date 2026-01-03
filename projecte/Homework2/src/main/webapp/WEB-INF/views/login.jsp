<%-- 
    login.jsp - Página de login (identificación de usuario)
    
    Esta página muestra un formulario simple para que el usuario
    introduzca su nombre y contraseña.
    
    Variables que recibimos del controlador (LoginController):
    - error: mensaje de error si las credenciales son incorrectas
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- Importamos JSTL para poder usar <c:if>, <c:url>, etc. --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <title>Identificació d'usuari</title>
</head>
<body>
    <%-- Título principal de la página --%>
    <h1>Identificació d'usuari</h1>
    
    <%-- 
        Aquí comprobamos si el controlador nos ha mandado un error.
        Si existe la variable "error", la mostramos en rojo para avisar al usuario.
        Usamos <c:if> para hacer esta comprobación.
    --%>
    <c:if test="${not empty error}">
        <p style="color: red;"><b>${error}</b></p>
    </c:if>
    
    <%-- 
        Formulario de login.
        - action: usamos c:url para que la ruta funcione bien (añade el contexto de la app)
        - method="POST": enviamos los datos de forma segura (no en la URL)
    --%>
    <form action="<c:url value='/login'/>" method="POST">
        
        <%-- Campo para el nombre de usuario --%>
        <p>
            <label for="username"><b>Nom d'usuari:</b></label><br>
            <%-- El input recoge lo que escribe el usuario y lo manda como "username" --%>
            <input type="text" id="username" name="username" placeholder="Introdueix el teu usuari">
        </p>
        
        <%-- Campo para la contraseña --%>
        <p>
            <label for="password"><b>Contrasenya:</b></label><br>
            <%-- type="password" hace que no se vean los caracteres mientras se escribe --%>
            <input type="password" id="password" name="password" placeholder="Introdueix la teva contrasenya">
        </p>
        
        <%-- Botón para enviar el formulario --%>
        <p>
            <input type="submit" value="Entrar">
        </p>
        
    </form>
    
    <hr>
    
    <%-- 
        Enlace para volver al listado principal sin hacer login.
    --%>
    <p>
        <a href="<c:url value='/'/>">Tornar al llistat de models</a>
    </p>
    
</body>
</html>
