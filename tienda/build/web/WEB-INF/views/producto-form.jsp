<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="row justify-content-center"><div class="col-12 col-lg-7">
  <p class="eyebrow">INVENTARIO</p><h1 class="h3">Registrar producto</h1>
  <p class="text-secondary">Completa los datos. El precio usa punto decimal.</p>
  <% if (request.getAttribute("error") != null) { %>
    <div class="alert alert-danger" role="alert"><%= Seguridad.texto(request.getAttribute("error")) %></div>
  <% } %>
  <form class="card card-body shadow-sm p-4" action="${pageContext.request.contextPath}/ProductoServlet?action=insertar" method="post">
    <input type="hidden" name="csrf" value="<%= Seguridad.texto(session.getAttribute("csrf")) %>">
    <div class="mb-3"><label for="nombre" class="form-label">Nombre</label>
      <input class="form-control" id="nombre" name="nombre" maxlength="120" value="<%= Seguridad.texto(request.getParameter("nombre")) %>" required></div>
    <div class="row"><div class="col-sm-6 mb-3"><label for="precio" class="form-label">Precio (₡)</label>
      <input type="number" class="form-control" id="precio" name="precio" min="0" max="99999999.99" step="0.01" value="<%= Seguridad.texto(request.getParameter("precio")) %>" required></div>
    <div class="col-sm-6 mb-3"><label for="stock" class="form-label">Stock</label>
      <input type="number" class="form-control" id="stock" name="stock" min="0" max="2147483647" step="1" value="<%= Seguridad.texto(request.getParameter("stock")) %>" required></div></div>
    <div class="d-flex gap-2"><button class="btn btn-primary">Guardar producto</button>
      <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/ProductoServlet">Cancelar</a></div>
  </form>
</div></div>
<%@ include file="footer.jspf" %>
