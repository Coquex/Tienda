<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="row justify-content-center"><div class="col-12 col-md-7 col-lg-5">
  <div class="card shadow-sm"><div class="card-body p-4">
    <p class="eyebrow">GESTIÓN DE INVENTARIO</p><h1 class="h3 mb-2">Iniciar sesión</h1>
    <p class="text-secondary">Ingresa para consultar y administrar los productos.</p>
    <% if (request.getAttribute("error") != null) { %><div class="alert alert-danger" role="alert"><%= Seguridad.texto(request.getAttribute("error")) %></div><% } %>
    <% if ("1".equals(request.getParameter("salida"))) { %><div class="alert alert-success" role="status">Sesión cerrada.</div><% } %>
    <form action="${pageContext.request.contextPath}/LoginServlet" method="post">
      <input type="hidden" name="csrf" value="<%= Seguridad.texto(session.getAttribute("csrf")) %>">
      <div class="mb-3"><label for="usuario" class="form-label">Usuario</label>
        <input class="form-control" id="usuario" name="usuario" maxlength="50" autocomplete="username" required></div>
      <div class="mb-4"><label for="password" class="form-label">Contraseña</label>
        <input type="password" class="form-control" id="password" name="password" maxlength="128" autocomplete="current-password" required></div>
      <button class="btn btn-primary w-100">Ingresar</button>
    </form>
  </div></div>
</div></div>
<%@ include file="footer.jspf" %>
