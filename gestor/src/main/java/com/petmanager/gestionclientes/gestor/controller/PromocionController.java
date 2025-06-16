/*package com.petmanager.gestionclientes.gestor.controller;

import com.petmanager.gestionclientes.gestor.model.Promocion;
import com.petmanager.gestionclientes.gestor.repository.PromocionRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/promociones")
public class PromocionController {
    private final PromocionRepository promocionRepo;

    public PromocionController(PromocionRepository promocionRepo){
        this.promocionRepo = promocionRepo;
    }

    @PostMapping("/programar")
    public Promocion programarPromocion(@RequestBody Promocion promo){
        promo.setEnviada(false);
        return promocionRepo.save(promo);
    }

    @GetMapping
    public List<Promocion> listarTodas(){
        return promocionRepo.findAll();
    }
}*/