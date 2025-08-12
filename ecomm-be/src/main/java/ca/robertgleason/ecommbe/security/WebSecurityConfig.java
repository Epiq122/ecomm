package ca.robertgleason.ecommbe.security;


import ca.robertgleason.ecommbe.security.jwt.AuthEntryPointJwt;
import ca.robertgleason.ecommbe.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/api/auth/**",
                        "/v3/api-docs/**",
                        "/swagger-ui/**",
                        "/api/public/**",
                        "/api/admin/**",
                        "/api/test/**",
                        "/images/**",
                        "/h2-console/**").permitAll()

                .anyRequest().authenticated()
        );

        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Add this line to set your UserDetailsService
        http.userDetailsService(userDetailsService);

        http.exceptionHandling(
                exceptionHandling -> exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
        );

        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/signin"));
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));


        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // completely bypass security
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }
}
