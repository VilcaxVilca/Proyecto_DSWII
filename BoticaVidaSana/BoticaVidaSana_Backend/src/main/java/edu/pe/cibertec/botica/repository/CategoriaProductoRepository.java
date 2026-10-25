package edu.pe.cibertec.botica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pe.cibertec.botica.entity.CategoriaProducto;

public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long> {

    Optional<CategoriaProducto> findByNombre(String nombre);
}
