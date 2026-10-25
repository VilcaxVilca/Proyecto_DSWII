
package pe.edu.cibertec.producto.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.producto.entity.CategoriaProducto;
import pe.edu.cibertec.producto.repository.CategoriaRepository;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoriaRestController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaRestController(
            CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public List<CategoriaProducto> listar() {
        return categoriaRepository.findAll();
    }
}
