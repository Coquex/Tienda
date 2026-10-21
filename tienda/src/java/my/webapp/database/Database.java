package my.webapp.database;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    public Connection getConnection() throws SQLException {
        // Las claves se leen de un archivo privado del equipo.
        String ruta = System.getProperty("tienda.config");
        if (ruta == null || ruta.isBlank()) {
            ruta = System.getProperty("user.home") + "/.tienda/db.properties";
        }

        Properties datos = new Properties();
        try (Reader archivo = Files.newBufferedReader(Path.of(ruta), StandardCharsets.UTF_8)) {
            datos.load(archivo);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer db.properties.", "08001");
        }

        String url = datos.getProperty("db.url");
        String usuario = datos.getProperty("db.user");
        String clave = datos.getProperty("db.password");
        if (url == null || usuario == null || clave == null
                || url.isBlank() || usuario.isBlank() || clave.isBlank()) {
            throw new SQLException("Faltan datos de conexión.", "08001");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Falta el controlador de MySQL.", "08001");
        }
        return DriverManager.getConnection(url, usuario, clave);
    }
}
