package com.petmanager.gestionclientes.gestor.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtFiltroAutenticacion extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFiltroAutenticacion(JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse reponse, FilterChain filterChain) throws ServletException, IOException{
        String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);

            if(jwtUtil.tokenValido(token)){
                Claims reclamos = jwtUtil.extraerReclamos(token);
                String correo = reclamos.getSubject();
                String rol = reclamos.get("rol", String.class);

                UsernamePasswordAuthenticationToken autorizacion = new UsernamePasswordAuthenticationToken(correo, null, Collections.singleton(() -> "ROLE_" + rol.toUpperCase()));

                autorizacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(autorizacion);
            }
        }
        filterChain.doFilter(request, reponse);
    }

}
