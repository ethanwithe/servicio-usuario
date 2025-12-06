package com.gimnasio.servicio_usuario.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gimnasio.servicio_usuario.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByUsernameAndPassword(String username, String password);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByRole(String role);

    List<Usuario> findByActivoTrue();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.role = :role AND u.activo = true")
    List<Usuario> findActiveUsersByRole(@Param("role") String role);

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    long countActiveUsers();

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.role = :role")
    long countByRole(@Param("role") String role);
}
