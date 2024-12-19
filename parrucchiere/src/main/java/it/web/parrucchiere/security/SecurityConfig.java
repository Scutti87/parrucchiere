//package it.web.parrucchiere.security;
//
//import java.util.Optional;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//import it.web.parrucchiere.exception.BusinessException;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .authorizeHttpRequests(authz -> authz
//                .requestMatchers("/login", "/", "/static/**", "/cliente/**", "/appuntamento/**", "/appuntamentoStorico/**", "/ricevuta/**", "/ricevutaStorico/**", "/aggiornaCalendario", "/generaRicevute").permitAll()
//                .requestMatchers("/admin/**", "/indexAdmin").hasRole("ADMIN")
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .loginPage("/login")
//                .defaultSuccessUrl("/admin/indexAdmin")
//                .failureUrl("/login?error=true")
//                .permitAll()
//            )
//            .logout(logout -> logout
//                .logoutSuccessUrl("/login")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//                .permitAll()
//            )
//            .sessionManagement(session -> session
//                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//            )
//            .csrf(csrf -> csrf.disable()); // Disabilita CSRF se necessario
//
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsService userDetailsService(IUserRepository userRepository) {
//        return username -> {
//        	Optional<User> userOp = userRepository.findByUsername(username);
//        	User user = userOp.get();
//        	if (userOp.isEmpty()) {
//				throw new BusinessException("User non presente");
//			}
//        	
//            return org.springframework.security.core.userdetails.User
//                .withUsername(user.getUsername())
//                .password(user.getPassword())
//                .roles(user.getRole().getName())
//                .build();
//        };
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance(); // Usa solo se le password sono già in chiaro
//    }
//}