package com.events.booking.web.Configration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
    public class JwtSecurityConfig {

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); // Adds ROLE_ prefix
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles"); // Looks for "roles" claim in JWT

        JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
        jwtConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtConverter;
    }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    // 2. Disable CSRF for API endpoints
                    .csrf(AbstractHttpConfigurer::disable)

                    // 3. Configure authorization
                    .authorizeHttpRequests(auth -> {
                        // Explicitly allow OPTIONS requests

                        // Public endpoints
                        auth.requestMatchers(
                                "/api/users/login",
                                "/api/users/register"

                        ).permitAll();

                        auth.requestMatchers("/api/admin/**").hasRole("ADMIN");
                        auth.requestMatchers("/api/booking/**").hasRole("USER");


                        // All other requests need authentication
                        auth.anyRequest().authenticated() ;

                    })

                    // 4. Session management
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                    // 5. Enable JWT
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt ->jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())))

                    // 6. HTTP Basic for specific endpoints
                    .httpBasic(Customizer.withDefaults());

            return http.build();
        }
        // JWT Configuration (unchanged from your original)
        @Bean
        public KeyPair keyPair() throws NoSuchAlgorithmException {
            var keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            return keyPairGenerator.generateKeyPair();
        }

        @Bean
        public RSAKey rsaKey(KeyPair keyPair) {
            return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                    .privateKey(keyPair.getPrivate())
                    .keyID(UUID.randomUUID().toString())
                    .build();
        }

        @Bean
        public JWKSource<SecurityContext> jwkSource(RSAKey rsaKey) {
            var jwkSet = new JWKSet(rsaKey);
            return (jwkSelector, context) -> jwkSelector.select(jwkSet);
        }

        @Bean
        public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
            return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
        }

        @Bean
        public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
            return new NimbusJwtEncoder(jwkSource);
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }


    }

