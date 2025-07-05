package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager; // PODE REMOVER
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; // PODE REMOVER
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // ESTE MÉTODO CONTINUA EXATAMENTE IGUAL
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/css/**",
                                "/js/**",
                                "/images/**"
                        ).permitAll()
                        .requestMatchers(
                                "/menu/colaborador/**",
                                "/emprestimos/colaborador",
                                "/emprestimos/listar/colaborador",
                                "/devolucao/listar/colaborador",
                                "/devolucao/colaborador"
                        ).hasRole("COLABORADOR")
                        .requestMatchers(
                                "/usuario/**",
                                "/menu/adm/**",
                                "/emprestimos/**",
                                "/devolucao/**",
                                "/epis/**"
                        ).hasRole("ADMINISTRADOR")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/redirecionar", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // ESTE MÉTODO CONTINUA IGUAL
        return new BCryptPasswordEncoder();
    }

    /*
     * ✅ APAGUE OU COMENTE O MÉTODO ABAIXO INTEIRO
     */
    // @Bean
    // public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    //     AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
    //     builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    //     return builder.build();
    // }
}