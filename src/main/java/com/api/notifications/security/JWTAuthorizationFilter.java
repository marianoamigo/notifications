package com.api.notifications.security;

import com.api.notifications.models.Usuario;
import com.api.notifications.errors.ErrorService;
import com.api.notifications.services.UserService;
import com.api.notifications.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private JWTUtil jwtUtil;
    private UserService userService;

    public JWTAuthorizationFilter(JWTUtil jwtUtil, UserService userService){
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws  ServletException, IOException {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")) {
            token = jwtUtil.acortaToken(token);
            System.out.println("Token recibido: " + token);
            String userId = jwtUtil.validaToken(token);
            System.out.println("User ID decodificado: " + userId);
            if (userId != null) {
                try {
                    Usuario usuario = userService.verUsuarioPorId(Integer.valueOf(userId), token);
                    if (usuario != null) {
                        UserDetails userDetails = new User(usuario.getMail(), "", new ArrayList<>());
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } catch (ErrorService es) {
                    es.printStackTrace();
                }

            }
        }
        filterChain.doFilter(request,response);

    }
}
