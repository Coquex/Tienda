<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ include file="header.jspf" %>
<div class="card card-body p-4"><h1 class="h3">No se pudo completar la solicitud</h1>
  <p>Revisa la dirección, la sesión y los datos enviados. Si el problema persiste, revisa la configuración de MySQL y el registro de Tomcat.</p>
  <p>Estado HTTP: <%= Seguridad.texto(request.getAttribute("jakarta.servlet.error.status_code")) %></p>
  <p class="mb-0"><a href="${pageContext.request.contextPath}/LoginServlet">Volver al inicio</a></p>
</div>
<%@ include file="footer.jspf" %>
