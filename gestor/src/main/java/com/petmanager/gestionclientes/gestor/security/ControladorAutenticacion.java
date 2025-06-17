/*
package com.petmanager.gestionclientes.gestor.security;

import com.petmanager.gestionclientes.gestor.model.Usuario;
import com.petmanager.gestionclientes.gestor.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class ControladorAutenticacion {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario fechaLoguin){
        Optional<Usuario> usuario = usuarioRepository.findAll().stream()
                .filter(u -> u.getCorreo_electronico().equals(fechaLoguin.getCorreo_electronico()) && u.getContrasena().equals(fechaLoguin.getContrasena()))
                .findFirst();

        if(usuario.isPresent()){
            String token = jwtUtil.generarToken(usuario.get().getCorreo_electronico(), usuario.get().getRol());
            return ResponseEntity.ok().body("Bearer " + token);
        }else{
            return ResponseEntity.status(401).body("credenciales incorrectas");
        }
    }
}
*/
