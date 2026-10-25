CREATE DATABASE IF NOT EXISTS DBBoticaVidaSana;
USE DBBoticaVidaSana;

CREATE TABLE IF NOT EXISTS usuario (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  rol VARCHAR(30) NOT NULL,
  estado BIT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categoria_producto (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(80) NOT NULL UNIQUE,
  descripcion VARCHAR(200),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS proveedor (
  id BIGINT NOT NULL AUTO_INCREMENT,
  ruc VARCHAR(11) NOT NULL UNIQUE,
  razon_social VARCHAR(120) NOT NULL,
  correo VARCHAR(120),
  telefono VARCHAR(20),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS producto (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo VARCHAR(20) NOT NULL UNIQUE,
  nombre VARCHAR(120) NOT NULL,
  precio DECIMAL(10,2) NOT NULL,
  stock INT NOT NULL,
  fecha_vencimiento DATE NOT NULL,
  activo BIT NOT NULL,
  categoria_id BIGINT NOT NULL,
  proveedor_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria_producto(id),
  CONSTRAINT fk_producto_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedor(id)
);

CREATE TABLE IF NOT EXISTS venta (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo VARCHAR(20) NOT NULL UNIQUE,
  fecha DATETIME NOT NULL,
  cliente VARCHAR(120) NOT NULL,
  usuario VARCHAR(50) NOT NULL,
  metodo_pago VARCHAR(30) NOT NULL,
  estado VARCHAR(20) NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS detalle_venta (
  id BIGINT NOT NULL AUTO_INCREMENT,
  venta_id BIGINT NOT NULL,
  producto_id BIGINT NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES venta(id),
  CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto(id)
);

INSERT IGNORE INTO usuario (username, password, rol, estado)
VALUES
('admin', '$2a$10$MhsjPTyf40WJvfIABdRbXejQ0fmBKUd4VH/vFmeaVY3DfNfWSLaq2', 'ADMINISTRADOR', 1),
('farmaceutico', '$2a$10$4OdY7km7c.UzHyXpUuSgHetIvc5N5hEhrlKYmWzIOO4EsUIIxbvOe', 'FARMACEUTICO', 1),
('cajero', '$2a$10$uVOl2J34wYxoFdFf1oNmROMaV.Y4mvfoJ.wpNPP98cfebMoAy1Tw6', 'CAJERO', 1);

INSERT IGNORE INTO categoria_producto (id, nombre, descripcion) VALUES
(1, 'Analgesicos', 'Dolor, fiebre e inflamacion.'),
(2, 'Antibioticos', 'Medicamentos sujetos a receta medica.'),
(3, 'Vitaminas', 'Suplementos y cuidado preventivo.'),
(4, 'Respiratorio', 'Tos, gripe, congestion y alergias.'),
(5, 'Digestivo', 'Acidez, diarrea, gases y digestion.'),
(6, 'Dermatologia', 'Cuidado de piel, heridas y tratamientos topicos.'),
(7, 'Higiene', 'Aseo personal y limpieza sanitaria.'),
(8, 'Botiquin', 'Primeros auxilios y material medico.'),
(9, 'Infantil', 'Productos para bebes y ninos.');

INSERT IGNORE INTO proveedor (id, ruc, razon_social, correo, telefono) VALUES
(1, '20601234567', 'Distribuidora Salud Peru SAC', 'ventas@saludperu.com', '987654321'),
(2, '20555666771', 'FarmaAndina Distribuciones', 'contacto@farmaandina.pe', '965112233'),
(3, '20444555662', 'Laboratorios Vida Farma', 'pedidos@vidafarma.pe', '944778899'),
(4, '20333444553', 'Medisur Importaciones', 'ventas@medisur.pe', '933224455');

INSERT IGNORE INTO producto (codigo, nombre, precio, stock, fecha_vencimiento, activo, categoria_id, proveedor_id) VALUES
('MED001', 'Paracetamol 500 mg', 4.50, 80, DATE_ADD(CURDATE(), INTERVAL 2 YEAR), 1, 1, 1),
('MED002', 'Ibuprofeno 400 mg', 6.90, 45, DATE_ADD(CURDATE(), INTERVAL 18 MONTH), 1, 1, 1),
('MED003', 'Naproxeno 550 mg', 8.80, 9, DATE_ADD(CURDATE(), INTERVAL 10 MONTH), 1, 1, 2),
('MED004', 'Diclofenaco gel 1%', 12.90, 18, DATE_ADD(CURDATE(), INTERVAL 14 MONTH), 1, 1, 3),
('ATB001', 'Amoxicilina 500 mg', 15.90, 24, DATE_ADD(CURDATE(), INTERVAL 11 MONTH), 1, 2, 3),
('ATB002', 'Azitromicina 500 mg', 22.50, 12, DATE_ADD(CURDATE(), INTERVAL 9 MONTH), 1, 2, 2),
('ATB003', 'Cefalexina 500 mg', 18.40, 7, DATE_ADD(CURDATE(), INTERVAL 7 MONTH), 1, 2, 4),
('VIT001', 'Vitamina C 1000 mg', 12.50, 35, DATE_ADD(CURDATE(), INTERVAL 2 YEAR), 1, 3, 1),
('VIT002', 'Complejo B x 30 tabletas', 19.90, 28, DATE_ADD(CURDATE(), INTERVAL 20 MONTH), 1, 3, 2),
('VIT003', 'Multivitaminico adulto', 32.90, 14, DATE_ADD(CURDATE(), INTERVAL 16 MONTH), 1, 3, 3),
('VIT004', 'Vitamina D3 2000 UI', 27.50, 5, DATE_ADD(CURDATE(), INTERVAL 12 MONTH), 1, 3, 4),
('RES001', 'Loratadina 10 mg', 9.50, 40, DATE_ADD(CURDATE(), INTERVAL 15 MONTH), 1, 4, 1),
('RES002', 'Jarabe para la tos adulto', 18.90, 10, DATE_ADD(CURDATE(), INTERVAL 50 DAY), 1, 4, 2),
('RES003', 'Descongestionante nasal spray', 16.90, 16, DATE_ADD(CURDATE(), INTERVAL 8 MONTH), 1, 4, 4),
('RES004', 'Pastillas para garganta miel limon', 7.90, 32, DATE_ADD(CURDATE(), INTERVAL 13 MONTH), 1, 4, 3),
('DIG001', 'Omeprazol 20 mg', 11.90, 38, DATE_ADD(CURDATE(), INTERVAL 19 MONTH), 1, 5, 3),
('DIG002', 'Sales de rehidratacion oral', 3.50, 60, DATE_ADD(CURDATE(), INTERVAL 24 MONTH), 1, 5, 1),
('DIG003', 'Simeticona gotas', 13.90, 8, DATE_ADD(CURDATE(), INTERVAL 35 DAY), 1, 5, 2),
('DIG004', 'Antiacido masticable', 6.50, 4, DATE_ADD(CURDATE(), INTERVAL 6 MONTH), 1, 5, 4),
('DER001', 'Clotrimazol crema 1%', 14.80, 20, DATE_ADD(CURDATE(), INTERVAL 17 MONTH), 1, 6, 4),
('DER002', 'Bloqueador solar FPS 50', 39.90, 25, DATE_ADD(CURDATE(), INTERVAL 22 MONTH), 1, 6, 2),
('DER003', 'Alcohol medicinal 70%', 8.50, 55, DATE_ADD(CURDATE(), INTERVAL 30 MONTH), 1, 6, 1),
('DER004', 'Agua oxigenada 120 ml', 5.80, 6, DATE_ADD(CURDATE(), INTERVAL 28 DAY), 1, 6, 3),
('HIG001', 'Gel antibacterial 250 ml', 9.90, 50, DATE_ADD(CURDATE(), INTERVAL 2 YEAR), 1, 7, 1),
('HIG002', 'Jabon antiseptico', 7.50, 30, DATE_ADD(CURDATE(), INTERVAL 18 MONTH), 1, 7, 2),
('HIG003', 'Mascarillas quirurgicas x 50', 18.00, 22, DATE_ADD(CURDATE(), INTERVAL 3 YEAR), 1, 7, 4),
('HIG004', 'Papel higienico humedo', 10.90, 3, DATE_ADD(CURDATE(), INTERVAL 14 MONTH), 1, 7, 3),
('BOT001', 'Gasas esteriles x 10', 6.20, 44, DATE_ADD(CURDATE(), INTERVAL 25 MONTH), 1, 8, 1),
('BOT002', 'Vendas elasticas 4 pulgadas', 8.70, 11, DATE_ADD(CURDATE(), INTERVAL 3 YEAR), 1, 8, 2),
('BOT003', 'Curitas surtidas x 100', 12.40, 2, DATE_ADD(CURDATE(), INTERVAL 21 MONTH), 1, 8, 4),
('BOT004', 'Termometro digital', 24.90, 17, DATE_ADD(CURDATE(), INTERVAL 4 YEAR), 1, 8, 3),
('INF001', 'Panal infantil talla M', 36.90, 15, DATE_ADD(CURDATE(), INTERVAL 2 YEAR), 1, 9, 2),
('INF002', 'Jarabe pediatrico multivitaminico', 21.90, 6, DATE_ADD(CURDATE(), INTERVAL 45 DAY), 1, 9, 3),
('INF003', 'Crema para escaldaduras', 17.60, 19, DATE_ADD(CURDATE(), INTERVAL 16 MONTH), 1, 9, 4),
('INF004', 'Toallitas humedas bebe', 11.80, 26, DATE_ADD(CURDATE(), INTERVAL 18 MONTH), 1, 9, 1);

INSERT IGNORE INTO venta (id, codigo, fecha, cliente, usuario, metodo_pago, estado, total) VALUES
(1, 'V00001', DATE_SUB(NOW(), INTERVAL 2 DAY), 'Consumidor final', 'cajero', 'Efectivo', 'REGISTRADA', 33.90),
(2, 'V00002', DATE_SUB(NOW(), INTERVAL 1 DAY), 'Maria Torres', 'farmaceutico', 'Yape', 'REGISTRADA', 29.40),
(3, 'V00003', DATE_SUB(NOW(), INTERVAL 5 HOUR), 'Consumidor final', 'cajero', 'Tarjeta', 'REGISTRADA', 40.70);

INSERT IGNORE INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 2, 4.50, 9.00),
(1, 8, 1, 12.50, 12.50),
(1, 28, 2, 6.20, 12.40),
(2, 13, 1, 18.90, 18.90),
(2, 17, 3, 3.50, 10.50),
(3, 22, 1, 8.50, 8.50),
(3, 24, 2, 9.90, 19.80),
(3, 30, 1, 12.40, 12.40);
