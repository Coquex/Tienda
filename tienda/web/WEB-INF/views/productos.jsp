<%@ page import="java.util.List" %>
<%@ page import="my.webapp.producto.Producto" %>
<% List<Producto> productos = (List<Producto>) request.getAttribute("productos"); %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jspf" %>
<div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
  <div><p class="eyebrow">INVENTARIO</p><h1 class="h3 mb-1">Productos</h1><p class="text-secondary mb-0">Consulta existencias y registra nuevos artículos.</p></div>
  <a class="btn btn-primary" href="${pageContext.request.contextPath}/ProductoServlet?action=nuevo">Nuevo producto</a>
</div>
<% if ("creado".equals(request.getParameter("estado"))) { %><div class="alert alert-success" role="status">Producto registrado.</div><% } %>
<% if ("eliminado".equals(request.getParameter("estado"))) { %><div class="alert alert-success" role="status">Producto eliminado.</div><% } %>
<div class="card shadow-sm"><div class="table-responsive">
  <table class="table table-hover align-middle mb-0"><caption class="px-3">Precios en colones costarricenses (₡).</caption>
    <thead class="table-light"><tr><th scope="col">ID</th><th scope="col">Nombre</th><th scope="col" class="text-end">Precio</th><th scope="col" class="text-end">Stock</th><th scope="col">Acción</th></tr></thead>
    <tbody><% for (Producto p : productos) { %><tr>
      <td><%= Seguridad.texto(p.getId()) %></td><td class="fw-semibold"><%= Seguridad.texto(p.getNombre()) %></td>
      <td class="text-end text-nowrap">₡ <%= Seguridad.texto(p.getPrecio()) %></td><td class="text-end"><%= Seguridad.texto(p.getStock()) %></td>
      <td><details><summary class="text-danger">Eliminar</summary>
        <form action="${pageContext.request.contextPath}/ProductoServlet?action=eliminar" method="post" class="mt-2">
          <input type="hidden" name="csrf" value="<%= Seguridad.texto(session.getAttribute("csrf")) %>">
          <input type="hidden" name="id" value="<%= Seguridad.texto(p.getId()) %>">
          <button class="btn btn-outline-danger btn-sm">Confirmar eliminación</button>
        </form>
      </details></td>
    </tr><% } %>
    <% if (productos.isEmpty()) { %><tr><td colspan="5" class="text-center p-4">No hay productos. Registra el primero.</td></tr><% } %>
    </tbody>
  </table>
</div></div>
<%@ include file="footer.jspf" %>
