package com.sbeereck.lutrampal.model;

public enum BeerCategory {
    NORMAL,
    SPECIAL;

    @Override
    public String toString() {
        switch (this) {
            case NORMAL:
                return "normal";
            case SPECIAL:
                return "special";
            default:
                return "";
        }
    }
}
