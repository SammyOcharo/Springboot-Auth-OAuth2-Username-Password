package com.samdev.customizedOauth20.Config;
import com.samdev.customizedOauth20.Entity.User;
import com.samdev.customizedOauth20.Repository.UserRepository;
import com.samdev.customizedOauth20.ServiceImpl.JWTService;
import com.samdev.customizedOauth20.ServiceImpl.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@SuppressWarnings("unused")
public class SecurityConfig {

    private final UserDetailService userDetailService;
    private final JWTService jwtService;
    private final UserRepository userRepository;

    private final SecurityFilter securityFilter;

    public SecurityConfig(UserDetailService userDetailService, JWTService jwtService, UserRepository userRepository, SecurityFilter securityFilter) {
        this.userDetailService = userDetailService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.securityFilter = securityFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler oauth2LoginAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // Get the authenticated user from the OAuth2 authentication object
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String email = oauthToken.getPrincipal().getAttribute("email");  // Get email from OAuth2 user info
            String username = oauthToken.getPrincipal().getName(); // Or use username from OAuth2 user info

            // Check if the user exists in the database
            User user = userRepository.findByEmail(email);  // Use email to find the user

            // If the user does not exist, create a new one
            if (user == null) {
                user = jwtService.createUser(username, email);
            }

            // Create the JWT token for the authenticated user
            String token = jwtService.GenerateToken(user);  // Generate JWT token

            // Redirect to the frontend with the JWT token as a query parameter
            response.sendRedirect("http://localhost:3000/dashboard?token=" + token);
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Enable CORS
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request.requestMatchers("/apps/api/v1/auth/**", "/v3/**","/swagger-ui/**", "/login",
                                        "/oauth2/**", "/login/oauth2/**")
                                .permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions
                .authenticationProvider(authenticationProvider()) // JWT Authentication provider
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("http://localhost:3000/dashboard") // Google OAuth2 login (for Google)
                        .successHandler(oauth2LoginAuthenticationSuccessHandler()) // Attach custom success handler
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Ensure JWT filter is applied first
                .build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000/login")); // ✅ Allow frontend origin
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public OidcUserService oidcUserService(){
        return new OidcUserService();
    }
}