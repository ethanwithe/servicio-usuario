package com.gimnasio.servicio_usuario.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gimnasio.servicio_usuario.dto.LoginRequest;
import com.gimnasio.servicio_usuario.dto.LoginResponse;
import com.gimnasio.servicio_usuario.dto.UsuarioDTO;
import com.gimnasio.servicio_usuario.exception.InvalidCredentialsException;
import com.gimnasio.servicio_usuario.exception.UsuarioNotFoundException;
import com.gimnasio.servicio_usuario.model.Usuario;
import com.gimnasio.servicio_usuario.repository.UsuarioRepository;
import com.gimnasio.servicio_usuario.service.UsuarioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Intento de login para usuario: {}", request.getUsername());

        Usuario usuario = usuarioRepository
            .findByUsernameAndPassword(request.getUsername(), request.getPassword())
            .orElseThrow(() -> new InvalidCredentialsException("Usuario o contraseña incorrectos"));

        if (!usuario.getActivo()) {
            throw new InvalidCredentialsException("Usuario inactivo");
        }

        // Actualizar último acceso
        usuario.setUltimoAcceso(LocalDateTime.now());
        usuarioRepository.save(usuario);

        log.info("Login exitoso para usuario: {}", usuario.getUsername());

        return LoginResponse.builder()
            .success(true)
            .message("Login exitoso")
            .usuario(UsuarioDTO.fromEntity(usuario))
            .build();
    }

    @Override
    public UsuarioDTO crearUsuario(Usuario usuario) {
        log.info("Creando nuevo usuario: {}", usuario.getUsername());

        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El username ya existe");
        }

        if (usuario.getEmail() != null && usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya existe");
        }

        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente con ID: {}", nuevoUsuario.getId());

        return UsuarioDTO.fromEntity(nuevoUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));
        return UsuarioDTO.fromEntity(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerUsuarioPorUsername(String username) {
        log.info("Buscando usuario por username: {}", username);
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado: " + username));
        return UsuarioDTO.fromEntity(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll().stream()
            .map(UsuarioDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosPorRole(String role) {
        log.info("Obteniendo usuarios por role: {}", role);
        return usuarioRepository.findByRole(role).stream()
            .map(UsuarioDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerUsuariosActivos() {
        log.info("Obteniendo usuarios activos");
        return usuarioRepository.findByActivoTrue().stream()
            .map(UsuarioDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO actualizarUsuario(Long id, Usuario usuarioActualizado) {
        log.info("Actualizando usuario con ID: {}", id);

        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));

        // Actualizar campos
        if (usuarioActualizado.getName() != null) {
            usuario.setName(usuarioActualizado.getName());
        }
        if (usuarioActualizado.getEmail() != null) {
            usuario.setEmail(usuarioActualizado.getEmail());
        }
        if (usuarioActualizado.getTelefono() != null) {
            usuario.setTelefono(usuarioActualizado.getTelefono());
        }
        if (usuarioActualizado.getPassword() != null) {
            usuario.setPassword(usuarioActualizado.getPassword());
        }

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        log.info("Usuario actualizado exitosamente");

        return UsuarioDTO.fromEntity(usuarioGuardado);
    }

    @Override
    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new UsuarioNotFoundException(id);
        }
        usuarioRepository.deleteById(id);
        log.info("Usuario eliminado exitosamente");
    }

    @Override
    public void desactivarUsuario(Long id) {
        log.info("Desactivando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        log.info("Usuario desactivado exitosamente");
    }

    @Override
    public void activarUsuario(Long id) {
        log.info("Activando usuario con ID: {}", id);
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new UsuarioNotFoundException(id));
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
        log.info("Usuario activado exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public long contarUsuariosActivos() {
        return usuarioRepository.countActiveUsers();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarUsuariosPorRole(String role) {
        return usuarioRepository.countByRole(role);
    }
}