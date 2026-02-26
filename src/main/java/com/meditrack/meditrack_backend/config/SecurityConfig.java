package com.meditrack.meditrack_backend.config;

import com.meditrack.meditrack_backend.security.JwtFilter;
import com.meditrack.meditrack_backend.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtFilter jwtFilter;
        private final UserDetailsServiceImpl userDetailsService;
        private final CorsConfigurationSource corsConfigurationSource;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                                .requestMatchers("/api/auth/**", "/auth/**", "/api/departments/**",
                                                                "/departments/**")
                                                .permitAll()

                                                // Users listing
                                                .requestMatchers(HttpMethod.GET, "/api/users/**")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "NURSE",
                                                                "LAB_TECHNICIAN")

                                                // Doctor -> receptionist patient registration request flow
                                                .requestMatchers(HttpMethod.POST, "/api/patient-requests")
                                                .hasRole("DOCTOR")
                                                .requestMatchers(HttpMethod.GET, "/api/patient-requests/**")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR")
                                                .requestMatchers(HttpMethod.PUT,
                                                                "/api/patient-requests/*/approve",
                                                                "/api/patient-requests/*/reject",
                                                                "/api/patient-requests/*/registered")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST")

                                                // Patients
                                                .requestMatchers(HttpMethod.POST, "/api/patients")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST")
                                                .requestMatchers(HttpMethod.PUT, "/api/patients/*/discharge")
                                                .hasAnyRole("ADMIN", "DOCTOR")
                                                .requestMatchers(HttpMethod.PUT, "/api/patients/*")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST")

                                                .requestMatchers(HttpMethod.GET, "/api/patients/**")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "NURSE")

                                                // Appointments
                                                .requestMatchers(HttpMethod.POST,
                                                                "/api/appointments",
                                                                "/api/appointments/book",
                                                                "/api/appointments/bookings")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST")
                                                .requestMatchers(HttpMethod.PUT, "/api/appointments/*/status")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR")
                                                .requestMatchers(HttpMethod.GET, "/api/appointments/**")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "NURSE")

                                                // Prescriptions (doctor creates; nurse/admin can view)
                                                .requestMatchers(HttpMethod.POST, "/api/prescriptions")
                                                .hasRole("DOCTOR")
                                                .requestMatchers(HttpMethod.PUT, "/api/prescriptions/*/stop",
                                                                "/api/prescriptions/*/complete")
                                                .hasRole("DOCTOR")
                                                .requestMatchers(HttpMethod.GET, "/api/prescriptions/**")
                                                .hasAnyRole("ADMIN", "DOCTOR", "NURSE")

                                                // Nurse tasks (doctor assigns; nurse executes)
                                                .requestMatchers(HttpMethod.POST, "/api/nurse-tasks")
                                                .hasRole("DOCTOR")
                                                .requestMatchers(HttpMethod.PUT, "/api/nurse-tasks/*/complete",
                                                                "/api/nurse-tasks/*/missed")
                                                .hasRole("NURSE")
                                                .requestMatchers(HttpMethod.GET, "/api/nurse-tasks/**")
                                                .hasAnyRole("ADMIN", "DOCTOR", "NURSE")

                                                // Lab tests (module-ready)
                                                .requestMatchers(HttpMethod.POST, "/api/lab-tests/**")
                                                .hasAnyRole("DOCTOR", "LAB_TECHNICIAN")
                                                .requestMatchers(HttpMethod.PUT, "/api/lab-tests/**")
                                                .hasAnyRole("LAB_TECHNICIAN", "DOCTOR")
                                                .requestMatchers(HttpMethod.GET, "/api/lab-tests/**")
                                                .hasAnyRole("ADMIN", "DOCTOR", "NURSE", "LAB_TECHNICIAN",
                                                                "RECEPTIONIST")

                                                // Beds
                                                .requestMatchers(HttpMethod.PUT, "/api/beds/*/status")
                                                .hasAnyRole("ADMIN", "NURSE")
                                                .requestMatchers(HttpMethod.GET, "/api/beds/**")
                                                .hasAnyRole("ADMIN", "RECEPTIONIST", "DOCTOR", "NURSE")

                                                // Attendance
                                                .requestMatchers(HttpMethod.POST, "/api/attendance/checkin/**",
                                                                "/api/attendance/mark")
                                                .hasAnyRole("DOCTOR", "NURSE", "LAB_TECHNICIAN")
                                                .requestMatchers(HttpMethod.PUT, "/api/attendance/checkout/**")
                                                .hasAnyRole("DOCTOR", "NURSE", "LAB_TECHNICIAN")
                                                .requestMatchers(HttpMethod.GET, "/api/attendance/**")
                                                .hasAnyRole("ADMIN", "DOCTOR", "NURSE", "LAB_TECHNICIAN")
                                                .requestMatchers(HttpMethod.POST, "/api/beds")
                                                .hasRole("ADMIN")

                                                // Alerts
                                                .requestMatchers("/api/alerts/**").hasRole("ADMIN")

                                                .anyRequest().authenticated())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authenticationProvider(authenticationProvider(passwordEncoder()))
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
                provider.setPasswordEncoder(passwordEncoder);
                return provider;
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
                return config.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}
