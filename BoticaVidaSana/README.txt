BOTICA VIDA SANA

Estructura del proyecto:

1. BoticaVidaSana_Backend
   Proyecto Spring Boot con JPA, Spring MVC, Spring Security, BCrypt, CORS y servicios REST.

2. BoticaVidaSana_Frontend
   Proyecto Angular que consume los servicios REST del backend. Incluye dashboard,
   mantenimiento de productos, alertas de stock bajo, control de vencimientos,
   registro de ventas y reporte comercial.

3. BaseDatos
   Script SQL para crear la base DBBoticaVidaSana y datos iniciales.

Credenciales de prueba:

Administrador:
Usuario: admin
Password: admin123

Farmaceutico:
Usuario: farmaceutico
Password: farma123

Cajero:
Usuario: cajero
Password: cajero123

Ejecucion:

Backend:
cd BoticaVidaSana_Backend
mvnw.cmd spring-boot:run

Frontend:
cd BoticaVidaSana_Frontend
npm install
npm start

URLs:

Backend: http://localhost:8080
Frontend: http://localhost:4200

Vistas principales:

- Login: acceso protegido por usuario, contrasena cifrada y rol.
- Panel: resumen del inventario, productos activos, stock bajo, vencimientos y ventas del dia.
- Inventario: productos con busqueda, categorias, proveedores, precio, stock, estado y vencimiento.
- Ventas: registro de ventas, listado, anulacion y descuento automatico de stock.
- Reporte de ventas: total vendido, cantidad de ventas, ventas anuladas, promedio, productos mas vendidos y ventas por usuario.

Permisos:

- ADMINISTRADOR: acceso completo a inventario, ventas y reportes.
- FARMACEUTICO: gestiona inventario, registra ventas, anula ventas y revisa reportes.
- CAJERO: consulta inventario y registra ventas. No elimina productos ni accede a reportes.

Endpoints principales:

POST   /api/auth/login
GET    /api/productos
POST   /api/productos
PUT    /api/productos/{id}
DELETE /api/productos/{id}
GET    /api/categorias
GET    /api/proveedores
GET    /api/ventas
POST   /api/ventas
PUT    /api/ventas/{id}/anular
GET    /api/ventas/reporte
