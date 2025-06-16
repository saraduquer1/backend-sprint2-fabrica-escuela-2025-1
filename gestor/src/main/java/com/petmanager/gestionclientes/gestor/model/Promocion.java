package com.petmanager.gestionclientes.gestor.model;

import java.time.LocalDateTime;

public class Promocion {

    private Long id;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private boolean enviada;

    //Constructor
    public Promocion(Long id, String mensaje, LocalDateTime fechaEnvio, boolean enviada){
        this.id = id;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.enviada = enviada;
    }

    //Getters y setters
    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public boolean isEnviada() {
        return enviada;
    }

    public void setEnviada(boolean enviada) {
        this.enviada = enviada;
    }
}