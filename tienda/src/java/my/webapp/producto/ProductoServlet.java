package my.webapp.producto;

import my.webapp.producto.ProductoDAO;
import my.webapp.producto.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

public class ProductoServlet extends HttpServlet {
    private final ProductoDAO dao = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("action");

        if ("nuevo".equals(accion)) {
            request.getRequestDispatcher("/WEB-INF/views/producto-form.jsp").forward(request, response);
            return;
        }
        if (accion != null && !accion.equals("listar")) {
            response.sendError(404);
            return;
        }

        try {
            request.setAttribute("productos", dao.listar());
            request.getRequestDispatcher("/WEB-INF/views/productos.jsp").forward(request, response);
        } catch (SQLException e) {
            getServletContext().log("Error al listar productos. SQLState=" + e.getSQLState());
            response.sendError(503);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("action");

        try {
            if (accion == null || "insertar".equals(accion)) {
                String nombre = request.getParameter("nombre");
                String textoPrecio = request.getParameter("precio");
                String textoStock = request.getParameter("stock");

                // Se revisan los datos también en Java, aunque el formulario los valide.
                if (nombre == null || nombre.trim().isEmpty() || nombre.trim().length() > 120) {
                    mostrarError(request, response, "Escribe un nombre de 1 a 120 caracteres.");
                    return;
                }
                if (textoPrecio == null || !textoPrecio.matches("[0-9]{1,8}(\\.[0-9]{1,2})?")) {
                    mostrarError(request, response, "El precio debe ser positivo o cero y tener hasta dos decimales.");
                    return;
                }

                int stock;
                try {
                    if (textoStock == null || !textoStock.matches("[0-9]{1,10}")) {
                        throw new NumberFormatException();
                    }
                    stock = Integer.parseInt(textoStock);
                } catch (NumberFormatException e) {
                    mostrarError(request, response, "El stock debe ser un entero de 0 a 2147483647.");
                    return;
                }

                BigDecimal precio = new BigDecimal(textoPrecio).setScale(2);
                Producto producto = new Producto(0, nombre.trim(), precio, stock);
                dao.insertar(producto);
                redirigir(request, response, "creado");

            } else if ("eliminar".equals(accion)) {
                int id;
                try {
                    id = Integer.parseInt(request.getParameter("id"));
                    if (id <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    response.sendError(400);
                    return;
                }

                if (dao.eliminar(id)) {
                    redirigir(request, response, "eliminado");
                } else {
                    response.sendError(404);
                }
            } else {
                response.sendError(404);
            }
        } catch (SQLException e) {
            getServletContext().log("Error al guardar o eliminar. SQLState=" + e.getSQLState());
            response.sendError(503);
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response, String mensaje)
            throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        response.setStatus(422);
        request.getRequestDispatcher("/WEB-INF/views/producto-form.jsp").forward(request, response);
    }

    private void redirigir(HttpServletRequest request, HttpServletResponse response, String estado) {
        // El navegador vuelve al listado después del POST.
        response.setStatus(303);
        response.setHeader("Location", request.getContextPath() + "/ProductoServlet?estado=" + estado);
    }
}
