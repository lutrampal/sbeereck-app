package com.sbeereck.lutrampal.model;

public enum ProductType {
    BEER,
    DEPOSIT,
    FOOD;

    @Override
    public String toString() {
        switch (this) {
            case BEER:
                return "Bière";
            case DEPOSIT:
                return "Caution";
            case FOOD:
                return "Nourriture";
            default:
                return "";
        }
    }
}
