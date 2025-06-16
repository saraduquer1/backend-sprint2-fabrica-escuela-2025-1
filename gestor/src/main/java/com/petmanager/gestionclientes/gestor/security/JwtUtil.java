package com.petmanager.gestionclientes.gestor.security;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtUtil {
    private final String LLAVESECRETA = "Mi llave secreta segura";
    private final long EXPIRACION = 86400000; //24 horas

    public String generarToken(String correo, String rol){
        return Jwts.builder()
                .setSubject(correo)
                .claim("Roll", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACION))
                .signWith(SignatureAlgorithm.ES256, LLAVESECRETA)
                .compact();
    }

    public Claims extraerReclamos(String token){
        return Jwts.parser().setSigningKey(LLAVESECRETA).parseClaimsJws(token).getBody();
    }

    public boolean tokenValido(String token){
        try{
            extraerReclamos(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
