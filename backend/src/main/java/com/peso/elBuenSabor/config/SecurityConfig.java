package com.peso.elBuenSabor.config;


import com.peso.elBuenSabor.JWT.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
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
//                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/**")).permitAll()

                                        //Consola H2:
                                        .requestMatchers(PathRequest.toH2Console()).permitAll()

                                        // CLIENTES
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/clientes/auditoria/**")).hasRole("ADMIN")
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/clientes/**")).hasAnyRole("ADMIN", "EMPLOYEE")

                                        // ARTÍCULOS
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/articulosmanufacturados/**")).hasRole("ADMIN")

                                        // USERS
                                        .requestMatchers(new AntPathRequestMatcher("/elbuensabor/v1/users/**"))
                                        .hasRole("ADMIN")

                                        // Todo lo demás
                                        .anyRequest().authenticated()

                )
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

