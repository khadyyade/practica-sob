<%-- 
    error.jsp - Página de errores amigable
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Error ${errorCode}</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { padding-top: 40px; background-color: #f5f5f5; }
        .error-box {
            max-width: 500px;
            margin: 0 auto;
            padding: 30px;
            background: #fff;
            border: 1px solid #d9534f;
            border-radius: 5px;
            text-align: center;
        }
        .error-code { font-size: 72px; color: #d9534f; margin-bottom: 0; }
    </style>
</head>
<body>
    <div class="container">
        <div class="error-box">
            <p class="error-code">${errorCode}</p>
            <h2>${errorTitle}</h2>
            <p class="text-muted">${errorMessage}</p>
            <hr>
            <a href="<c:url value='/Web/'/>" class="btn btn-primary">Tornar a l'inici</a>
        </div>
    </div>
</body>
</html>
