package com.alquilatusvehiculos.alquila_tus_vehiculos.model;

/**
 * Define los tipos de vehículos existentes.
 * El display name muestra el nombre para UI render
 */
public enum TipoVehiculo {

    MARITIMO("Marítimo"),
    AEREO("Aéreo"),
    TERRESTRE("Terrestre");

    private final String displayName;

    TipoVehiculo(String displayName){
        this.displayName = displayName;
    }

    public String getDisplayName(){
        return displayName;
    }
}
