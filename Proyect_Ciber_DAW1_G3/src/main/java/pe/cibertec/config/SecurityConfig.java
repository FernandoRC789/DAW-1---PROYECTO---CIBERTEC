package pe.cibertec.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // 1. PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // CSRF deshabilitado para APIs
                .cors(Customizer.withDefaults()) // habilitamos CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login").permitAll()

                        // RECLAMOS
                        .requestMatchers("/api/reclamos/*").hasRole("ADMIN")   // DELETE
                        .requestMatchers("/api/reclamos/registrar").hasRole("OPERADOR")
                        .requestMatchers("/api/reclamos/id/**").hasRole("OPERADOR")
                        .requestMatchers("/api/reclamos/*/estado").hasAnyRole("SUPERVISOR", "ADMIN")
                        .requestMatchers("/api/reclamos/*/historial").hasAnyRole("SUPERVISOR", "ADMIN")
                        .requestMatchers("/api/reclamos/listar").hasAnyRole("OPERADOR", "SUPERVISOR", "ADMIN")
                        .requestMatchers("/api/reclamos/{id}").hasAnyRole("OPERADOR", "SUPERVISOR", "ADMIN")

                        // CIUDADANOS
                        .requestMatchers("/api/ciudadanos/**").hasRole("OPERADOR")

                        // ADMIN
                        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")
                        .requestMatchers("/api/prioridades/**").hasRole("ADMIN")
                        .requestMatchers("/api/tipos/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
                .build();
    }

    // 3. Configuración global de CORS
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:4200")); // URL del frontend
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}