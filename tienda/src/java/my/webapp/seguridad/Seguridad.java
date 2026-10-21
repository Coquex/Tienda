package my.webapp.seguridad;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Seguridad {
    private static final int ITERACIONES = 600000;

    // El hash permite verificar la clave sin guardarla como texto.
    public static String crearHash(char[] clave) {
        byte[] sal = new byte[16];
        new SecureRandom().nextBytes(sal);
        byte[] resultado = derivar(clave, sal, ITERACIONES);

        return "pbkdf2_sha256$" + ITERACIONES + "$"
                + Base64.getEncoder().encodeToString(sal) + "$"
                + Base64.getEncoder().encodeToString(resultado);
    }

    public static boolean verificarClave(char[] clave, String hash) {
        try {
            String[] partes = hash.split("\\$");
            if (partes.length != 4 || !partes[0].equals("pbkdf2_sha256")) {
                return false;
            }
            int iteraciones = Integer.parseInt(partes[1]);
            if (iteraciones < 100000 || iteraciones > 1000000) {
                return false;
            }

            byte[] sal = Base64.getDecoder().decode(partes[2]);
            byte[] guardado = Base64.getDecoder().decode(partes[3]);
            if (sal.length != 16 || guardado.length != 32) {
                return false;
            }
            byte[] resultado = derivar(clave, sal, iteraciones);
            return MessageDigest.isEqual(guardado, resultado);
        } catch (RuntimeException e) {
            return false;
        }
    }

    private static byte[] derivar(char[] clave, byte[] sal, int iteraciones) {
        PBEKeySpec datos = new PBEKeySpec(clave, sal, iteraciones, 256);
        try {
            SecretKeyFactory algoritmo = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return algoritmo.generateSecret(datos).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo procesar la contraseña.", e);
        } finally {
            datos.clearPassword();
        }
    }

    // El token es un código aleatorio que acompaña los formularios de esa sesión.
    public static void crearToken(HttpSession sesion) {
        if (sesion.getAttribute("csrf") == null) {
            byte[] codigo = new byte[32];
            new SecureRandom().nextBytes(codigo);
            String token = Base64.getUrlEncoder().withoutPadding().encodeToString(codigo);
            sesion.setAttribute("csrf", token);
        }
    }

    public static boolean validarToken(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion == null) {
            return false;
        }
        String recibido = request.getParameter("csrf");
        String guardado = (String) sesion.getAttribute("csrf");
        if (recibido == null || guardado == null || recibido.length() != 43) {
            return false;
        }
        return MessageDigest.isEqual(
                guardado.getBytes(StandardCharsets.UTF_8),
                recibido.getBytes(StandardCharsets.UTF_8));
    }
    // Los textos se muestran como datos, aunque contengan símbolos de HTML.
    public static String texto(Object valor) {
        if (valor == null) {
            return "";
        }
        return valor.toString().replace("&", "&amp;")
                .replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }
}
