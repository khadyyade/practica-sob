<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="ca">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>AI Models Catalog - Home</title>
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
                <a href="${pageContext.request.contextPath}/Web/">Home</a> |
                <c:choose>
                    <c:when test="${authenticated}">
                        <b>Benvingut ${username}!</b> |
                        <a href="${pageContext.request.contextPath}/Web/login/logout">Logout</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/Web/login">Login</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        <hr>

        <!-- Filtres -->
        <div class="row">
            <div class="col-xs-12">
                <h3>Filtrar Models</h3>
                <form action="${pageContext.request.contextPath}/Web/" method="GET">
                    <div class="row">
                        <div class="col-xs-12 col-sm-6">
                            <p><b>Capabilities (Maxim 2):</b></p>
                            <label><input type="checkbox" name="capability" value="chat-completion" <c:if test="${filters.capabilities.contains('chat-completion')}">checked</c:if>> Chat Completion</label><br>
                            <label><input type="checkbox" name="capability" value="code-generation" <c:if test="${filters.capabilities.contains('code-generation')}">checked</c:if>> Code Generation</label><br>
                            <label><input type="checkbox" name="capability" value="text-to-image" <c:if test="${filters.capabilities.contains('text-to-image')}">checked</c:if>> Text to Image</label><br>
                            <label><input type="checkbox" name="capability" value="audio-generation" <c:if test="${filters.capabilities.contains('audio-generation')}">checked</c:if>> Audio Generation</label>
                        </div>
                        <div class="col-xs-12 col-sm-6">
                            <p><b>Provider:</b></p>
                            <input type="text" name="provider" class="form-control" placeholder="OpenAI, Mistral, Anthropic..." value="${filters.provider != null ? filters.provider : ''}">
                            <br>
                            <button type="submit" class="btn btn-primary">Aplicar Filtres</button>
                            <button type="button" class="btn btn-default" onclick="window.location.href='${pageContext.request.contextPath}/Web/'">Netejar</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
        <hr>

        <!-- Errors i filtres actius -->
        <c:if test="${not empty error}">
            <div class="row">
                <div class="col-xs-12">
                    <div class="alert alert-danger"><b>Error:</b> ${error}</div>
                </div>
            </div>
        </c:if>

        <c:if test="${filters.hasFilters()}">
            <div class="row">
                <div class="col-xs-12">
                    <div class="alert alert-info">
                        <b>Filtres actius:</b>
                        <c:if test="${not empty filters.capabilities}">Capabilities: <c:forEach items="${filters.capabilities}" var="cap" varStatus="status">${cap}<c:if test="${!status.last}">, </c:if></c:forEach></c:if>
                        <c:if test="${not empty filters.provider}"> | Provider: ${filters.provider}</c:if>
                    </div>
                </div>
            </div>
        </c:if>

        <!-- Models -->
        <c:choose>
            <c:when test="${empty models}">
                <div class="row">
                    <div class="col-xs-12">
                        <p>No s'han trobat models.</p>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row">
                    <c:forEach items="${models}" var="model">
                        <!-- col-xs-12: mobil 1 columna, col-sm-6: escriptori 2 columnes -->
                        <div class="col-xs-12 col-sm-6">
                            <div class="panel panel-default" onclick="window.location.href='${pageContext.request.contextPath}/Web/model/${model.id}'" style="cursor:pointer;">
                                <div class="panel-heading">
                                    <h4>${model.name} <c:if test="${not empty model.version}"><small>v${model.version}</small></c:if></h4>
                                    <span class="label ${model.isPrivate ? 'label-warning' : 'label-success'}">${model.isPrivate ? 'PRIVAT' : 'PUBLIC'}</span>
                                </div>
                                <div class="panel-body">
                                    <p><c:choose><c:when test="${not empty model.summary}">${model.summary}</c:when><c:otherwise>${model.description != null && model.description.length() > 150 ? model.description.substring(0, 150).concat('...') : model.description}</c:otherwise></c:choose></p>
                                    <c:if test="${not empty model.capabilities}">
                                        <p><c:forEach items="${model.capabilities}" var="capability"><span class="label label-default">${capability.name}</span> </c:forEach></p>
                                    </c:if>
                                    <c:if test="${not empty model.provider}"><p><b>Provider:</b> ${model.provider.name}</p></c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Footer -->
        <hr>
        <div class="row">
            <div class="col-xs-12">
                <p>AI Models Catalog - Homework 2 SOB</p>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var checkboxes = document.querySelectorAll('input[name="capability"]');
            for (var i = 0; i < checkboxes.length; i++) {
                checkboxes[i].addEventListener('change', function() {
                    if (document.querySelectorAll('input[name="capability"]:checked').length > 2) {
                        this.checked = false;
                        alert('Nomes pots seleccionar un maxim de 2 capabilities.');
                    }
                });
            }
        });
    </script>
</body>
</html>
