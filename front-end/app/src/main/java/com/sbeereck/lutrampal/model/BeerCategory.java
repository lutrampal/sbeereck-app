package com.sbeereck.lutrampal.model;

public enum BeerCategory {
    NORMAL,
    SPECIAL;

    @Override
    public String toString() {
        switch (this) {
            case NORMAL:
                return "Normale";
            case SPECIAL:
                return "Spéciale";
            default:
                return "";
        }
    }
}
