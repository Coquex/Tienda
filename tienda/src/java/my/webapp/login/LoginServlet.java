package my.webapp.login;

import my.webapp.login.UsuarioDAO;
import my.webapp.seguridad.Seguridad;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        if (sesion.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/ProductoServlet");
            return;
        }

        Seguridad.crearToken(sesion);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usuario = request.getParameter("usuario");
        String textoClave = request.getParameter("password");

        if (usuario == null || usuario.isBlank() || usuario.length() > 50
                || textoClave == null || textoClave.isEmpty() || textoClave.length() > 128) {
            mostrarError(request, response, 401, "Usuario o contraseña incorrectos.");
            return;
        }

        char[] clave = textoClave.toCharArray();
        try {
            UsuarioDAO dao = new UsuarioDAO();
            Usuario encontrado = dao.validarLogin(usuario.trim(), clave);
            if (encontrado != null) {
                // Al ingresar se crea una sesión nueva para el usuario.
                request.getSession().invalidate();
                HttpSession sesion = request.getSession(true);
                sesion.setAttribute("usuario", encontrado);
                Seguridad.crearToken(sesion);

                response.setStatus(303);
                response.setHeader("Location", request.getContextPath() + "/ProductoServlet");
            } else {
                mostrarError(request, response, 401, "Usuario o contraseña incorrectos.");
            }
        } catch (SQLException e) {
            getServletContext().log("Error de conexión en login. SQLState=" + e.getSQLState());
            mostrarError(request, response, 503, "No se pudo consultar la base de datos. Revisa la configuración local.");
        } finally {
            Arrays.fill(clave, '\0');
        }
    }

    private void mostrarError(HttpServletRequest request, HttpServletResponse response,
                              int estado, String mensaje) throws ServletException, IOException {
        request.setAttribute("error", mensaje);
        response.setStatus(estado);
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
}
