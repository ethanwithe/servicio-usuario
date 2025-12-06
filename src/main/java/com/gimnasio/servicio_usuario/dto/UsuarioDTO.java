package com.gimnasio.servicio_usuario.dto;

import java.time.LocalDateTime;

import com.gimnasio.servicio_usuario.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long id;
    private String username;
    private String name;
    private String role;
    private String email;
    private String telefono;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;
    private Boolean activo;

    // Constructor desde entidad
    public static UsuarioDTO fromEntity(Usuario usuario) {
        return UsuarioDTO.builder()
            .id(usuario.getId())
            .username(usuario.getUsername())
            .name(usuario.getName())
            .role(usuario.getRole())
            .email(usuario.getEmail())
            .telefono(usuario.getTelefono())
            .fechaRegistro(usuario.getFechaRegistro())
            .ultimoAcceso(usuario.getUltimoAcceso())
            .activo(usuario.getActivo())
            .build();
    }
}