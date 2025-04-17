package mx.com.gm.config;

import mx.com.gm.service.DeportistaDetailsServ;
import mx.com.gm.service.InstructorDetailsServ;
import mx.com.gm.service.OrganizacionDetailsServ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTAuthFilter jwtDeportista;

    @Autowired
    private JWTAuthFilterOrg jwtOrganizacion;
    
    @Autowired
    private JWTAuthFilterInst jwtInstructor;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, 
                                                   CustomAuthenticationProvider customAuthenticationProvider) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/auth/**", "/public/**").permitAll()
                        .requestMatchers("/auth/login/deportista/**").permitAll()
                        .requestMatchers("/auth/login/organizacion/**").permitAll()
                        .requestMatchers("/auth/login/instructor/**").permitAll()
                        .requestMatchers("/rutinas/**").hasAnyAuthority("instructor")
                        .requestMatchers("/posiciones/**").hasAnyAuthority("instructor")
                        .requestMatchers("/deportistas/**").hasAnyAuthority("instructor")
                        .requestMatchers("/rutinaJugador/**").hasAnyAuthority("instructor")
                        .requestMatchers("/equipos/**").hasAnyAuthority("instructor")
                        .requestMatchers("/eventosEquipo").hasAnyAuthority("instructor")
                        .requestMatchers("/api/**").hasAnyAuthority("instructor")
                        .requestMatchers("/ejercicios/**").hasAnyAuthority("instructor")
                        .requestMatchers("/video/**").permitAll()
                        .requestMatchers("/eventosFuturosDep/**").hasAnyAuthority("deportista")
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(customAuthenticationProvider)  // ✅ Se inyecta como parámetro
                .addFilterBefore(jwtOrganizacion, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtDeportista, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtInstructor, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class).build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider(DeportistaDetailsServ deportistaDetailsServ,
                                                                     OrganizacionDetailsServ organizacionDetailsServ,
                                                                     InstructorDetailsServ instructorDetailsServ,
                                                                     PasswordEncoder passwordEncoder) {
        return new CustomAuthenticationProvider(deportistaDetailsServ, organizacionDetailsServ,instructorDetailsServ,passwordEncoder);
    }
}
