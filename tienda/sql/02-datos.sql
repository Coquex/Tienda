USE tienda;
-- IDs explícitos: repetir el script no duplica los cinco productos.
INSERT IGNORE INTO productos (id, nombre, precio, stock) VALUES
 (1, 'Cuaderno universitario', 2500.00, 30),
 (2, 'Bolígrafo azul', 450.00, 100),
 (3, 'Memoria USB 32 GB', 6500.00, 15),
 (4, 'Mouse óptico', 8900.00, 12),
 (5, 'Teclado USB', 12500.00, 8);
-- Usuario de prueba. Sin contraseña compartida: activarlo con my.webapp.seguridad.CrearUsuario.
-- El marcador no es un hash válido y Seguridad.verificarClave lo rechaza.
INSERT IGNORE INTO usuarios (usuario, password_hash) VALUES ('estudiante', 'PENDIENTE_ACTIVACION_LOCAL');
