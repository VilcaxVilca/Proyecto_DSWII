package edu.pe.cibertec.botica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.pe.cibertec.botica.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

}