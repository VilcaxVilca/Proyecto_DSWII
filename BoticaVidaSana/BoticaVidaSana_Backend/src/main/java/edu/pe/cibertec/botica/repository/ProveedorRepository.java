package edu.pe.cibertec.botica.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pe.cibertec.botica.entity.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {

    Optional<Proveedor> findByRuc(String ruc);
}
