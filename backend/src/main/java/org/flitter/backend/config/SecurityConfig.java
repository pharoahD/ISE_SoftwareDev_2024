package org.flitter.backend.config;

import org.flitter.backend.entity.User;
import org.flitter.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserRepository userRepository;
    public SecurityConfig(@Autowired UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(cors -> cors.configurationSource(
                corsConfigurationSource()
        ));
//        http.formLogin(Customizer.withDefaults());
        // 配置访问
        http.authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/auth/**").permitAll()    // 开启登录
                        .requestMatchers("/api/project/**").authenticated() // 仅用户角色可访问项目接口
                        .requestMatchers("/api/user/**").authenticated()
                        .anyRequest().authenticated());

        http.sessionManagement(session -> {
                    session.maximumSessions(1);
                    session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
                });
        http.securityContext(securityContext -> securityContext.requireExplicitSave(false));
        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            var user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            return new UserDetails() {
                @Override
                public Collection<? extends SimpleGrantedAuthority> getAuthorities() {
                    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                }

                @Override
                public String getPassword() {
                    return user.getPassword();
                }

                @Override
                public String getUsername() {
                    return user.getUsername();
                }

                @Override
                public boolean isAccountNonExpired() {
                    return true;
                }

                @Override
                public boolean isAccountNonLocked() {
                    return true;
                }

                @Override
                public boolean isCredentialsNonExpired() {
                    return true;
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }
            };
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            System.err.println(username);
            return userRepository.findByUsername(username);
        }
        return null;
    }
}
