package com.gimnasio.servicio_usuario.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gimnasio.servicio_usuario.dto.LoginRequest;
import com.gimnasio.servicio_usuario.dto.LoginResponse;
import com.gimnasio.servicio_usuario.dto.UsuarioDTO;
import com.gimnasio.servicio_usuario.model.Usuario;
import com.gimnasio.servicio_usuario.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    // =============================
    // LOGIN
    // =============================
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/users/login - Username: {}", request.getUsername());
        LoginResponse response = usuarioService.login(request);
        return ResponseEntity.ok(response);
    }

    // =============================
    // CREAR USUARIO
    // =============================
    @PostMapping
    public ResponseEntity<UsuarioDTO> crearUsuario(@Valid @RequestBody Usuario usuario) {
        log.info("POST /api/users - Creando usuario: {}", usuario.getUsername());
        UsuarioDTO nuevoUsuario = usuarioService.crearUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    // =============================
    // OBTENER TODOS LOS USUARIOS
    // =============================
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios() {
        log.info("GET /api/users");
        return ResponseEntity.ok(usuarioService.obtenerTodosLosUsuarios());
    }

    // =============================
    // OBTENER USUARIO POR ID
    // =============================
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuario(@PathVariable Long id) {
        log.info("GET /api/users/{}", id);
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorId(id));
    }

    // =============================
    // OBTENER USUARIO POR USERNAME
    // =============================
    @GetMapping("/username/{username}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorUsername(@PathVariable String username) {
        log.info("GET /api/users/username/{}", username);
        return ResponseEntity.ok(usuarioService.obtenerUsuarioPorUsername(username));
    }

    // =============================
    // OBTENER POR ROLE
    // =============================
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosPorRole(@PathVariable String role) {
        log.info("GET /api/users/role/{}", role);
        return ResponseEntity.ok(usuarioService.obtenerUsuariosPorRole(role));
    }

    // =============================
    // OBTENER USUARIOS ACTIVOS
    // =============================
    @GetMapping("/activos")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosActivos() {
        log.info("GET /api/users/activos");
        return ResponseEntity.ok(usuarioService.obtenerUsuariosActivos());
    }

    // =============================
    // ACTUALIZAR USUARIO
    // =============================
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {
        log.info("PUT /api/users/{}", id);
        return ResponseEntity.ok(usuarioService.actualizarUsuario(id, usuario));
    }

    // =============================
    // ELIMINAR USUARIO
    // =============================
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminarUsuario(@PathVariable Long id) {
        log.info("DELETE /api/users/{}", id);
        usuarioService.eliminarUsuario(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario eliminado exitosamente");

        return ResponseEntity.ok(response);
    }

    // =============================
    // DESACTIVAR USUARIO
    // =============================
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Map<String, String>> desactivarUsuario(@PathVariable Long id) {
        log.info("PATCH /api/users/{}/desactivar", id);
        usuarioService.desactivarUsuario(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario desactivado exitosamente");

        return ResponseEntity.ok(response);
    }

    // =============================
    // ACTIVAR USUARIO
    // =============================
    @PatchMapping("/{id}/activar")
    public ResponseEntity<Map<String, String>> activarUsuario(@PathVariable Long id) {
        log.info("PATCH /api/users/{}/activar", id);
        usuarioService.activarUsuario(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuario activado exitosamente");

        return ResponseEntity.ok(response);
    }

    // =============================
    // ESTADÍSTICAS
    // =============================
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        log.info("GET /api/users/estadisticas");

        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalActivos", usuarioService.contarUsuariosActivos());
        estadisticas.put("administradores", usuarioService.contarUsuariosPorRole("administrador"));
        estadisticas.put("gerentes", usuarioService.contarUsuariosPorRole("gerente"));
        estadisticas.put("clientes", usuarioService.contarUsuariosPorRole("cliente"));
        estadisticas.put("fundadores", usuarioService.contarUsuariosPorRole("fundador"));

        return ResponseEntity.ok(estadisticas);
    }

    // =============================
    // HEALTH CHECK
    // =============================
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "servicio-usuario");
        return ResponseEntity.ok(response);
    }
}
