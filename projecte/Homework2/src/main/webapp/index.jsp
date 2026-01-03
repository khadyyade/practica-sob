<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Redirigir automáticamente al controlador MVC --%>
<%
    String queryString = request.getQueryString();
    String redirectUrl = request.getContextPath() + "/Web/";
    if (queryString != null && !queryString.isEmpty()) {
        redirectUrl += "?" + queryString;
    }
    response.sendRedirect(redirectUrl);
%>
