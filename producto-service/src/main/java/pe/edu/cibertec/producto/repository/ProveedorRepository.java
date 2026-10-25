
package pe.edu.cibertec.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.producto.entity.Proveedor;

public interface ProveedorRepository
        extends JpaRepository<Proveedor, Long> {
}
