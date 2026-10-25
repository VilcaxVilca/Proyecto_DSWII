package edu.pe.cibertec.botica.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="usuario")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable=false, unique=true)
    private String username;

    @Column(nullable=false)
    private String password;

    @Column(length=30, nullable=false)
    private String rol;

    @Column(nullable=false)
    private Boolean estado=true;
}