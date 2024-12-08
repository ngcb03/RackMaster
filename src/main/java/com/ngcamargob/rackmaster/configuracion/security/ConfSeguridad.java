package com.ngcamargob.rackmaster.configuracion.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfSeguridad {

    private final ServDetalleUsuarioImpl userDetailsService;

    public ConfSeguridad(ServDetalleUsuarioImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new FiltroLimitadorRed(), UsernamePasswordAuthenticationFilter.class)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // Rutas solo para SUPER_ADMIN
                                .requestMatchers("/rackmaster/cuentas/**").hasAuthority("ROLE_SUPER_ADMIN")

                                // Rutas accesibles por SUPER_ADMIN, ADMIN y USER
                                .requestMatchers(
                                        "/rackmaster/clusters/a1/**",
                                        "/rackmaster/servidores/a1/**",
                                        "/rackmaster/maquinas-virtuales/a1/**",
                                        "/rackmaster/inicio",
                                        "/rackmaster/acceder"
                                ).hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_ADMIN", "ROLE_USER")

                                // Rutas accesibles por SUPER_ADMIN y ADMIN
                                .requestMatchers(
                                        "/rackmaster/clusters/a2/**",
                                        "/rackmaster/servidores/a2/**",
                                        "/rackmaster/maquinas-virtuales/a2/**"
                                ).hasAnyAuthority("ROLE_SUPER_ADMIN", "ROLE_ADMIN")

                                // Recursos estáticos accesibles sin autenticación
                                .requestMatchers("/vendor/**", "/css/**", "/js/**").permitAll()

                                // Cualquier otra solicitud debe estar autenticada
                                .anyRequest().authenticated()
                )

                .formLogin(formLogin ->
                        formLogin
                                .loginPage("/rackmaster/acceder")
                                .failureUrl("/rackmaster/invalid-session")
                                .defaultSuccessUrl("/rackmaster/verificar-usuario", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                .logoutSuccessUrl("/rackmaster/cerrar-sesion")
                                .invalidateHttpSession(true) // Invalidar la sesión HTTP
                                .clearAuthentication(true) // Limpiar la autenticación
                                .deleteCookies("JSESSIONID") // Eliminar cookies (por ejemplo, la de sesión)
                                .permitAll()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionFixation().none()
                                .invalidSessionUrl("/rackmaster/acceder?sessionExpired")
                                .maximumSessions(1)
                                .expiredUrl("/rackmaster/acceder?sessionExpired")
                                .sessionRegistry(sessionRegistry())
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedPage("/error")
                );

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManager.class);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

//    @Bean
//    public FilterRegistrationBean<FiltroLimitadorRed> rateLimitFilter() {
//        FilterRegistrationBean<FiltroLimitadorRed> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new FiltroLimitadorRed());
//        registrationBean.addUrlPatterns("/*");
//        registrationBean.setOrder(1);
//        return registrationBean;
//    }

}
