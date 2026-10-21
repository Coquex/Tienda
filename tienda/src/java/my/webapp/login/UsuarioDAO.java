package my.webapp.login;

import my.webapp.database.Database;
import my.webapp.seguridad.Seguridad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario validarLogin(String usuario, char[] clave) throws SQLException {
        String sql = "SELECT id, usuario, password_hash FROM usuarios WHERE usuario = ?";

        try (Connection conexion = new Database().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, usuario);

            try (ResultSet resultado = consulta.executeQuery()) {
                if (resultado.next()) {
                    String hash = resultado.getString("password_hash");
                    if (Seguridad.verificarClave(clave, hash)) {
                        return new Usuario(resultado.getInt("id"), resultado.getString("usuario"));
                    }
                }
                return null;
            }
        }
    }
}
