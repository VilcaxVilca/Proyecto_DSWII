package edu.pe.cibertec.botica.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pe.cibertec.botica.entity.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {

    Optional<Venta> findByCodigo(String codigo);

    List<Venta> findByFechaBetweenOrderByFechaDesc(LocalDateTime inicio, LocalDateTime fin);

    List<Venta> findByEstadoAndFechaBetweenOrderByFechaDesc(String estado, LocalDateTime inicio, LocalDateTime fin);
}
