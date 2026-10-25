
package pe.edu.cibertec.producto.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.producto.entity.Proveedor;
import pe.edu.cibertec.producto.repository.ProveedorRepository;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "http://localhost:4200")
public class ProveedorRestController {

    private final ProveedorRepository proveedorRepository;

    public ProveedorRestController(
            ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @GetMapping
    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }
}
