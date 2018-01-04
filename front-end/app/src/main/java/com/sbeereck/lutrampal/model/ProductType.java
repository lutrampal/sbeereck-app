package com.sbeereck.lutrampal.model;

public enum ProductType {
    BEER,
    DEPOSIT,
    FOOD;

    @Override
    public String toString() {
        switch (this) {
            case BEER:
                return "beer";
            case DEPOSIT:
                return "deposit";
            case FOOD:
                return "food";
            default:
                return "";
        }
    }
}