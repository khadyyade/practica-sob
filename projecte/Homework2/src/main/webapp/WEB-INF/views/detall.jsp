<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${model.name} - Detall del model</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container-fluid">
        <!-- Header -->
        <div class="row">
            <div class="col-xs-12">
                <h1>AI Models Catalog</h1>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12">
                <c:choose>
                    <c:when test="${authenticated}">
                        <b>Benvingut ${username}!</b> |
                        <a href="${pageContext.request.contextPath}/Web/login/logout">Tancar sessio</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/Web/login">Iniciar sessio</a>
                    </c:otherwise>
                </c:choose>
                | <a href="${pageContext.request.contextPath}/Web/">Tornar al llistat</a>
            </div>
        </div>
        <hr>

        <!-- Contingut principal -->
        <div class="row">
            <!-- col-xs-12: mobil 1 columna, col-sm-8: escriptori columna principal -->
            <div class="col-xs-12 col-sm-8">
                <h2>${model.name}</h2>
                
                <c:if test="${not empty model.provider}">
                    <p><b>Proveidor:</b> ${model.provider.name}</p>
                </c:if>
                
                <c:if test="${not empty model.summary}">
                    <p><b>Resum:</b> ${model.summary}</p>
                </c:if>
                
                <c:if test="${not empty model.description}">
                    <h3>Descripcio</h3>
                    <p>${model.description}</p>
                </c:if>
                
                <c:if test="${not empty model.capabilities}">
                    <h3>Capacitats destacades</h3>
                    <ul>
                        <c:forEach var="capability" items="${model.capabilities}">
                            <li>${capability.name}</li>
                        </c:forEach>
                    </ul>
                </c:if>
                
                <c:if test="${not empty model.license}">
                    <p><b>Llicencia:</b> ${model.license.name}</p>
                </c:if>
            </div>

            <!-- col-xs-12: mobil 1 columna (sota), col-sm-4: escriptori columna lateral -->
            <div class="col-xs-12 col-sm-4">
                <c:choose>
                    <c:when test="${authenticated}">
                        <div class="panel panel-info">
                            <div class="panel-heading">
                                <h4>Informacio privada</h4>
                            </div>
                            <div class="panel-body">
                                <p><i>(Nomes visible per usuaris identificats)</i></p>
                                
                                <c:if test="${not empty model.version}">
                                    <p><b>Versio:</b> ${model.version}</p>
                                </c:if>
                                
                                <c:if test="${not empty model.lastUpdateDate}">
                                    <p><b>Ultima actualitzacio:</b> <fmt:formatDate value="${model.lastUpdateDate}" pattern="dd/MM/yyyy"/></p>
                                </c:if>
                                
                                <c:if test="${not empty model.trainingDate}">
                                    <p><b>Data entrenament:</b> <fmt:formatDate value="${model.trainingDate}" pattern="dd/MM/yyyy"/></p>
                                </c:if>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="panel panel-default">
                            <div class="panel-body">
                                <p><i>Inicia sessio per veure informacio addicional del model.</i></p>
                                <a href="${pageContext.request.contextPath}/Web/login" class="btn btn-primary">Identificar-se</a>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Footer -->
        <hr>
        <div class="row">
            <div class="col-xs-12">
                <p>AI Models Catalog - Homework 2 SOB</p>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>
</body>
</html>
