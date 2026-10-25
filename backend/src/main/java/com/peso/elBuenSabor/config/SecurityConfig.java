package com.peso.elBuenSabor.config;


import com.peso.elBuenSabor.JWT.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth ->
                                auth
                                        // Auth
                                        .requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll()
                                        // Página de error de Spring: si no es pública, cualquier excepción termina como 401
                                        .requestMatchers(new AntPathRequestMatcher("/error")).permitAll()
//                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/**")).permitAll()

                                        //Consola H2:
                                        .requestMatchers(PathRequest.toH2Console()).permitAll()

                                        // CLIENTES
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/clientes/auditoria/**")).hasRole("ADMIN")
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/clientes/**")).hasAnyRole("ADMIN", "EMPLOYEE")

                                        // MENÚ PÚBLICO: el catálogo se lee sin login, salvo los rankings de ventas
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/articulosmanufacturados/ranking*")).hasRole("ADMIN")
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/articulosmanufacturados/**", "GET")).permitAll()
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/rubroarticulos/**", "GET")).permitAll()

                                        // ARTÍCULOS
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/articulosmanufacturados/**")).hasRole("ADMIN")
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/rubroarticulos/**")).hasRole("ADMIN")
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/detalleartmanuf/**")).hasRole("ADMIN")
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/articulosinsumo/**", "GET")).hasAnyRole("ADMIN", "EMPLOYEE")
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/articulosinsumo/**")).hasRole("ADMIN")

                                        // PEDIDOS (los clientes usan /me/pedidos)
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/pedidos/**")).hasAnyRole("ADMIN", "EMPLOYEE")
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/detallepedidos/**")).hasAnyRole("ADMIN", "EMPLOYEE")

                                        // FACTURACIÓN
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/facturas/**")).hasRole("ADMIN")
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/detallefacturas/**")).hasRole("ADMIN")
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/mpdatos/**")).hasRole("ADMIN")

                                        // USERS
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/users/**"))
                                        .hasRole("ADMIN")

                                        // Todo lo demás (incluye /elbuensabor/v1/me/**)
                                        .anyRequest().authenticated()

                )
                .cors(Customizer.withDefaults())
                // Sin token: 401 (no 403), así el frontend distingue "no logueado" de "sin permiso"
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) //H2
                .sessionManagement(session->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    }

