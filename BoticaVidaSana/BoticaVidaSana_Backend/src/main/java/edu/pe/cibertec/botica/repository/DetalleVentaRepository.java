package edu.pe.cibertec.botica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pe.cibertec.botica.entity.DetalleVenta;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
}
