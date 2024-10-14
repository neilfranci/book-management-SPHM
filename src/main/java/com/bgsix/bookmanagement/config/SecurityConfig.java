package com.bgsix.bookmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;

import com.bgsix.bookmanagement.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.COOKIES));

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,  UserService userService, UserExistenceCheckFilter userExistenceCheckFilter) throws Exception {

        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        http
            .cors(cors -> cors.disable())
            .csrf(csrf -> csrf.disable())
            .authenticationProvider(authenticationProvider)
			.addFilterBefore(userExistenceCheckFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/signup", "/login", "/logout").permitAll()
				.requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/book/add", "/book/edit", "/book/request-approve", "/book/update/**", "/book/delete/**").hasAnyRole("ADMIN", "LIBRARIAN")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/book/search", true)
                .failureUrl("/login?error")
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .addLogoutHandler(clearSiteData)
                .permitAll());

        return http.build();
    }

    
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}