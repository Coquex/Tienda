package my.webapp.login;

import my.webapp.seguridad.Seguridad;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class FiltroSesion implements Filter {

    @Override
    public void doFilter(ServletRequest entrada, ServletResponse salida, FilterChain cadena)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) entrada;
        HttpServletResponse response = (HttpServletResponse) salida;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "DENY");

        String ruta = request.getServletPath();
        HttpSession sesion = request.getSession(false);

        if (ruta.equals("/ProductoServlet") || ruta.equals("/LogoutServlet")) {
            if (sesion == null || sesion.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/LoginServlet");
                return;
            }
        }

        // Cada formulario POST debe llevar el código de la sesión.
        if (request.getMethod().equals("POST") && !Seguridad.validarToken(request)) {
            response.sendError(403);
            return;
        }

        cadena.doFilter(request, response);
    }
}
