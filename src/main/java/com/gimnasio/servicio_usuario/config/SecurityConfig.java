package com.gimnasio.servicio_usuario.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.profiles.active:dev}")
    private String activeProfile;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        // ⭐ Nueva forma recomendada desde Spring Security 6.1
        http.headers(headers -> headers
            .contentSecurityPolicy(csp -> {
                if (activeProfile.equalsIgnoreCase("prod")) {
                    // 🔒 PRODUCCIÓN: CSP seguro
                    csp.policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self'; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "img-src 'self' data:;"
                    );
                } else {
                    // 🛠️ DESARROLLO: permitir eval para Vite
                    csp.policyDirectives(
                        "script-src 'self' 'unsafe-eval' 'unsafe-inline';"
                    );
                }
            })
        );

        return http.build();
    }
}
