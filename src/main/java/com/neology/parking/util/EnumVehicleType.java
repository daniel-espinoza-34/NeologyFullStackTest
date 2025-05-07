package com.neology.parking.util;

public enum EnumVehicleType {
    OFICIAL("oficial"),
    RESIDENTE("residente"),
    NO_RESIDENTE("no-residente");

    public final String id;

    EnumVehicleType(String id) {
        this.id = id;
    }
}