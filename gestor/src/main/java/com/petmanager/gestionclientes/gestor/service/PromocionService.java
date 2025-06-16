package com.petmanager.gestionclientes.gestor.service;

import com.petmanager.gestionclientes.gestor.model.Promocion;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

@Service
public class PromocionService {

    @PostConstruct
    public void simularEnvioPromociones() {
        new Thread(() -> {
            List<Promocion> promociones = Arrays.asList(
                    new Promocion(1L, "¡20% de descuento en productos para gatos!", LocalDateTime.now(), false),
                    new Promocion(2L, "Consulta gratis para tu mascota este mes", LocalDateTime.now().plusMinutes(5), false)
            );

            for (Promocion promo : promociones) {
                System.out.println("[SIMULACION] Enviando mensaje: \"" + promo.getMensaje() + "\" a las " + promo.getFechaEnvio());
                promo.setEnviada(true);
            }

            try{
                Thread.sleep(5000); //Espera 5 segundos
                System.out.println("[SIMULACION] Finalizada. Cerrando aplicacion");
                System.exit(0);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }).start();
    }
}