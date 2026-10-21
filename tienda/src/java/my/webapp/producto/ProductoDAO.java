package my.webapp.producto;

import my.webapp.database.Database;
import my.webapp.producto.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public List<Producto> listar() throws SQLException {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT id, nombre, precio, stock FROM productos ORDER BY id";

        // try cierra la conexión y los resultados al terminar.
        try (Connection conexion = new Database().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {

            while (resultado.next()) {
                Producto producto = new Producto(
                        resultado.getInt("id"),
                        resultado.getString("nombre"),
                        resultado.getBigDecimal("precio"),
                        resultado.getInt("stock"));
                productos.add(producto);
            }
        }
        return productos;
    }

    public void insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos(nombre, precio, stock) VALUES (?, ?, ?)";

        try (Connection conexion = new Database().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, producto.getNombre());
            consulta.setBigDecimal(2, producto.getPrecio());
            consulta.setInt(3, producto.getStock());
            consulta.executeUpdate();
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection conexion = new Database().getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, id);
            int filas = consulta.executeUpdate();
            return filas == 1;
        }
    }
}
