package com.bgsix.bookmanagement.config;

import com.bgsix.bookmanagement.service.UserService;

import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class UserExistenceCheckFilter extends OncePerRequestFilter {

    private final UserService userService;

    public UserExistenceCheckFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();

            // Check if the user still exists in the database
            if (!userService.existsByEmail(email)) {
                // Invalidate session and log the user out
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                response.sendRedirect("/login?deleted");

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
