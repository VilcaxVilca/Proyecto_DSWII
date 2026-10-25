
package pe.edu.cibertec.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.producto.entity.CategoriaProducto;

public interface CategoriaRepository
        extends JpaRepository<CategoriaProducto, Long> {
}
