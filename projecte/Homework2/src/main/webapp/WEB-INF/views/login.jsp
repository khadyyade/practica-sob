<%-- 
    login.jsp - Página de login (identificación de usuario)
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Identificació d'usuari</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { padding-top: 40px; background-color: #f5f5f5; }
        .login-box {
            max-width: 400px;
            margin: 0 auto;
            padding: 30px;
            background: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="login-box">
            <h2 class="text-center">Identificació d'usuari</h2>
            <hr>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            
            <form action="<c:url value='/Web/login'/>" method="POST">
                <div class="form-group">
                    <label for="username">Nom d'usuari:</label>
                    <input type="text" class="form-control" id="username" name="username" placeholder="Introdueix el teu usuari">
                </div>
                <div class="form-group">
                    <label for="password">Contrasenya:</label>
                    <input type="password" class="form-control" id="password" name="password" placeholder="Introdueix la teva contrasenya">
                </div>
                <button type="submit" class="btn btn-primary btn-block">Entrar</button>
            </form>
            
            <hr>
            <p class="text-center">
                <a href="<c:url value='/Web/'/>">Tornar al llistat de models</a>
            </p>
        </div>
    </div>
</body>
</html>
