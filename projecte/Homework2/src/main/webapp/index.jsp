<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>AI Models Catalog - Home</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        
        header {
            background: white;
            padding: 20px 30px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            margin-bottom: 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        header h1 {
            color: #667eea;
            font-size: 28px;
        }
        
        .auth-links a {
            color: #667eea;
            text-decoration: none;
            margin-left: 20px;
            font-weight: 500;
            transition: color 0.3s;
        }
        
        .auth-links a:hover {
            color: #764ba2;
        }
        
        .filter-panel {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        
        .filter-panel h2 {
            color: #333;
            margin-bottom: 20px;
            font-size: 20px;
        }
        
        .filter-form {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            align-items: end;
        }
        
        .form-group {
            display: flex;
            flex-direction: column;
        }
        
        .form-group label {
            color: #555;
            margin-bottom: 8px;
            font-weight: 500;
            font-size: 14px;
        }
        
        .form-group input,
        .form-group select {
            padding: 10px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s;
        }
        
        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #667eea;
        }
        
        .capability-checkboxes {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }
        
        .capability-checkboxes label {
            display: flex;
            align-items: center;
            font-weight: normal;
            cursor: pointer;
        }
        
        .capability-checkboxes input[type="checkbox"] {
            margin-right: 8px;
            width: 18px;
            height: 18px;
            cursor: pointer;
        }
        
        .filter-actions {
            display: flex;
            gap: 10px;
        }
        
        .btn {
            padding: 10px 25px;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .btn-primary {
            background: #667eea;
            color: white;
        }
        
        .btn-primary:hover {
            background: #5568d3;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background: #e0e0e0;
            color: #333;
        }
        
        .btn-secondary:hover {
            background: #d0d0d0;
        }
        
        .alert {
            padding: 15px 20px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        
        .alert-error {
            background: #fee;
            color: #c33;
            border-left: 4px solid #c33;
        }
        
        .alert-info {
            background: #e7f3ff;
            color: #0066cc;
            border-left: 4px solid #0066cc;
        }
        
        .models-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
            gap: 25px;
        }
        
        .model-card {
            background: white;
            border-radius: 10px;
            padding: 25px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            transition: all 0.3s;
            cursor: pointer;
        }
        
        .model-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 16px rgba(0,0,0,0.2);
        }
        
        .model-header {
            display: flex;
            justify-content: space-between;
            align-items: start;
            margin-bottom: 15px;
        }
        
        .model-card h3 {
            color: #333;
            font-size: 20px;
            margin-bottom: 5px;
        }
        
        .model-badge {
            padding: 4px 10px;
            border-radius: 12px;
            font-size: 11px;
            font-weight: 600;
            text-transform: uppercase;
        }
        
        .badge-public {
            background: #d4edda;
            color: #155724;
        }
        
        .badge-private {
            background: #fff3cd;
            color: #856404;
        }
        
        .model-summary {
            color: #666;
            font-size: 14px;
            line-height: 1.6;
            margin-bottom: 15px;
        }
        
        .model-meta {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
            margin-top: 15px;
            padding-top: 15px;
            border-top: 1px solid #e0e0e0;
        }
        
        .meta-item {
            display: flex;
            align-items: center;
            font-size: 13px;
            color: #777;
        }
        
        .meta-item strong {
            color: #333;
            margin-right: 5px;
        }
        
        .capabilities-list {
            display: flex;
            flex-wrap: wrap;
            gap: 6px;
            margin-top: 10px;
        }
        
        .capability-tag {
            background: #f0f0f0;
            padding: 4px 10px;
            border-radius: 15px;
            font-size: 12px;
            color: #555;
        }
        
        .no-results {
            background: white;
            padding: 60px 30px;
            border-radius: 10px;
            text-align: center;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        
        .no-results h3 {
            color: #333;
            font-size: 24px;
            margin-bottom: 10px;
        }
        
        .no-results p {
            color: #666;
            font-size: 16px;
        }
        
        footer {
            background: white;
            padding: 20px;
            border-radius: 10px;
            margin-top: 40px;
            text-align: center;
            color: #666;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <div class="container">
        <!-- Header -->
        <header>
            <h1>🤖 AI Models Catalog</h1>
            <div class="auth-links">
                <a href="${pageContext.request.contextPath}/">Home</a>
                <a href="${pageContext.request.contextPath}/login">Login</a>
            </div>
        </header>

        <!-- Filter Panel -->
        <div class="filter-panel">
            <h2>🔍 Filtrar Modelos</h2>
            <form action="${pageContext.request.contextPath}/" method="GET" class="filter-form">
                <!-- Capabilities Filter -->
                <div class="form-group">
                    <label>Capabilities (Máximo 2):</label>
                    <div class="capability-checkboxes">
                        <label>
                            <input type="checkbox" name="capability" value="NLP" 
                                   <c:if test="${filters.capabilities.contains('NLP')}">checked</c:if>>
                            NLP (Natural Language Processing)
                        </label>
                        <label>
                            <input type="checkbox" name="capability" value="Vision" 
                                   <c:if test="${filters.capabilities.contains('Vision')}">checked</c:if>>
                            Computer Vision
                        </label>
                        <label>
                            <input type="checkbox" name="capability" value="Speech" 
                                   <c:if test="${filters.capabilities.contains('Speech')}">checked</c:if>>
                            Speech Recognition
                        </label>
                        <label>
                            <input type="checkbox" name="capability" value="Generation" 
                                   <c:if test="${filters.capabilities.contains('Generation')}">checked</c:if>>
                            Content Generation
                        </label>
                    </div>
                </div>

                <!-- Provider Filter -->
                <div class="form-group">
                    <label for="provider">Provider:</label>
                    <input type="text" 
                           id="provider" 
                           name="provider" 
                           placeholder="e.g., OpenAI, Google, Microsoft"
                           value="${filters.provider != null ? filters.provider : ''}">
                </div>

                <!-- Action Buttons -->
                <div class="form-group">
                    <div class="filter-actions">
                        <button type="submit" class="btn btn-primary">Aplicar Filtros</button>
                        <button type="button" class="btn btn-secondary" onclick="window.location.href='${pageContext.request.contextPath}/'">Limpiar</button>
                    </div>
                </div>
            </form>
        </div>

        <!-- Error Message -->
        <c:if test="${not empty error}">
            <div class="alert alert-error">
                <strong>⚠️ Error:</strong> ${error}
            </div>
        </c:if>

        <!-- Active Filters Info -->
        <c:if test="${filters.hasFilters()}">
            <div class="alert alert-info">
                <strong>📌 Filtros activos:</strong>
                <c:if test="${not empty filters.capabilities}">
                    Capabilities: <c:forEach items="${filters.capabilities}" var="cap" varStatus="status">${cap}<c:if test="${!status.last}">, </c:if></c:forEach>
                </c:if>
                <c:if test="${not empty filters.provider}">
                    | Provider: ${filters.provider}
                </c:if>
            </div>
        </c:if>

        <!-- Models Grid -->
        <c:choose>
            <c:when test="${empty models}">
                <div class="no-results">
                    <h3>🔍 No se encontraron modelos</h3>
                    <p>
                        <c:choose>
                            <c:when test="${filters.hasFilters()}">
                                Intenta ajustar los filtros de búsqueda para obtener más resultados.
                            </c:when>
                            <c:otherwise>
                                No hay modelos disponibles en este momento.
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="models-grid">
                    <c:forEach items="${models}" var="model">
                        <div class="model-card" onclick="window.location.href='${pageContext.request.contextPath}/model/${model.id}'">
                            <div class="model-header">
                                <div>
                                    <h3>${model.name}</h3>
                                    <c:if test="${not empty model.version}">
                                        <small style="color: #999;">v${model.version}</small>
                                    </c:if>
                                </div>
                                <span class="model-badge ${model.isPrivate ? 'badge-private' : 'badge-public'}">
                                    ${model.isPrivate ? '🔒 Private' : '🌐 Public'}
                                </span>
                            </div>

                            <p class="model-summary">
                                <c:choose>
                                    <c:when test="${not empty model.summary}">
                                        ${model.summary}
                                    </c:when>
                                    <c:otherwise>
                                        ${model.description != null && model.description.length() > 150 
                                          ? model.description.substring(0, 150).concat('...') 
                                          : model.description}
                                    </c:otherwise>
                                </c:choose>
                            </p>

                            <div class="model-meta">
                                <c:if test="${not empty model.provider}">
                                    <div class="meta-item">
                                        <strong>Provider:</strong> ${model.provider.name}
                                    </div>
                                </c:if>

                                <c:if test="${not empty model.trainingDate}">
                                    <div class="meta-item">
                                        <strong>Training Date:</strong> 
                                        <fmt:formatDate value="${model.trainingDate}" pattern="dd/MM/yyyy"/>
                                    </div>
                                </c:if>
                            </div>

                            <c:if test="${not empty model.capabilities}">
                                <div class="capabilities-list">
                                    <c:forEach items="${model.capabilities}" var="capability">
                                        <span class="capability-tag">
                                            ${capability.name}
                                        </span>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>

        <!-- Footer -->
        <footer>
            <p>© 2026 AI Models Catalog - Práctica SOB (Sistemes Oberts)</p>
            <p style="margin-top: 5px; font-size: 14px;">
                Total de modelos: <strong>${not empty models ? models.size() : 0}</strong>
            </p>
        </footer>
    </div>

    <script>
        // Limitar a máximo 2 capabilities seleccionadas
        document.addEventListener('DOMContentLoaded', function() {
            const checkboxes = document.querySelectorAll('input[name="capability"]');
            
            checkboxes.forEach(checkbox => {
                checkbox.addEventListener('change', function() {
                    const checkedCount = document.querySelectorAll('input[name="capability"]:checked').length;
                    
                    if (checkedCount > 2) {
                        this.checked = false;
                        alert('⚠️ Solo puedes seleccionar un máximo de 2 capabilities.');
                    }
                });
            });
        });
    </script>
</body>
</html>
