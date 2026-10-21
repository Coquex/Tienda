package my.webapp.seguridad;

import java.io.Console;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class CrearUsuario {

    public static void main(String[] args) throws Exception {
        Console consola = System.console();
        if (consola == null) {
            throw new IllegalStateException("Ejecuta esta clase desde PowerShell o CMD.");
        }

        char[] clave = consola.readPassword("Contraseña para estudiante (12-128 caracteres): ");
        char[] repetir = consola.readPassword("Repite la contraseña: ");
        try {
            if (clave == null || clave.length < 12 || clave.length > 128
                    || !Arrays.equals(clave, repetir)) {
                throw new IllegalArgumentException("Revisa la longitud y que las claves coincidan.");
            }

            String hash = Seguridad.crearHash(clave);
            String sql = "USE tienda;\nUPDATE usuarios SET password_hash = '" + hash
                    + "' WHERE usuario = 'estudiante';\n";
            Path archivo = Path.of("config/local/activar-usuario.sql");
            Files.createDirectories(archivo.getParent());
            Files.writeString(archivo, sql, StandardCharsets.UTF_8);
            System.out.println("Ejecuta config/local/activar-usuario.sql en Workbench.");
        } finally {
            if (clave != null) {
                Arrays.fill(clave, '\0');
            }
            if (repetir != null) {
                Arrays.fill(repetir, '\0');
            }
        }
    }
}
