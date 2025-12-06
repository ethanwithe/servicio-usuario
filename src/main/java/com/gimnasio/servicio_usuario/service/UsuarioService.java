package com.gimnasio.servicio_usuario.service;

import java.util.List;

import com.gimnasio.servicio_usuario.dto.LoginRequest;
import com.gimnasio.servicio_usuario.dto.LoginResponse;
import com.gimnasio.servicio_usuario.dto.UsuarioDTO;
import com.gimnasio.servicio_usuario.model.Usuario;

public interface UsuarioService {

    LoginResponse login(LoginRequest request);

    UsuarioDTO crearUsuario(Usuario usuario);

    UsuarioDTO obtenerUsuarioPorId(Long id);

    UsuarioDTO obtenerUsuarioPorUsername(String username);

    List<UsuarioDTO> obtenerTodosLosUsuarios();

    List<UsuarioDTO> obtenerUsuariosPorRole(String role);

    List<UsuarioDTO> obtenerUsuariosActivos();

    UsuarioDTO actualizarUsuario(Long id, Usuario usuario);

    void eliminarUsuario(Long id);

    void desactivarUsuario(Long id);

    void activarUsuario(Long id);

    long contarUsuariosActivos();

    long contarUsuariosPorRole(String role);
}
