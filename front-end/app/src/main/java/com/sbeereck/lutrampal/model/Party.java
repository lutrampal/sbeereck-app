package com.sbeereck.lutrampal.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A class to represent a party organized by S'Beer Eck.
 */
public class Party implements Comparable, Serializable {

    private int id = -1;
    /**
     * The name of the party.
     */
    private String name = "";
    /**
     * The date of the party.
     */
    private Date date = null;
    /**
     * How many people attended the party.
     */
    private int numberOfAttendees;
    /**
     * How much balance was earned during this party.
     */
    private float balance = 0;
    private List<Transaction> transactions = new LinkedList<>();
    private float specialBeerPrice = 0;
    private float normalBeerPrice = 0;

    private HashMap<Product, BeerCategory> servedBeers = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public float getIncome() {
        return balance;
    }

    public void setIncome(float mIncome) {
        this.balance = mIncome;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date mDate) {
        this.date = mDate;
    }

    public int getNumberOfAttendees() {
        return numberOfAttendees;
    }

    public void setNumberOfAttendees(int mNumberOfAttendees) {
        this.numberOfAttendees = mNumberOfAttendees;
    }

    public HashMap<Product, BeerCategory> getServedBeers() {
        return servedBeers;
    }

    public void setServedBeers(HashMap<Product, BeerCategory> servedBeers) {
        this.servedBeers = servedBeers;
    }

    public float getSpecialBeerPrice() {
        return specialBeerPrice;
    }

    public void setSpecialBeerPrice(float specialBeerPrice) {
        this.specialBeerPrice = specialBeerPrice;
    }

    public float getNormalBeerPrice() {
        return normalBeerPrice;
    }

    public void setNormalBeerPrice(float normalBeerPrice) {
        this.normalBeerPrice = normalBeerPrice;
    }

    public Party(int id, String name, Date date, int numberOfAttendees, float balance) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.numberOfAttendees = numberOfAttendees;
        this.balance = balance;
    }

    public Party(String name, Date date, float normalBeerPrice, float specialBeerPrice,
                 HashMap<Product, BeerCategory> servedBeers) {
        this.name = name;
        this.date = date;
        this.specialBeerPrice = specialBeerPrice;
        this.normalBeerPrice = normalBeerPrice;
        this.servedBeers = servedBeers;
    }

    @Override
    public String toString() {
        return "Party{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", numberOfAttendees=" + numberOfAttendees +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Party party = (Party) o;

        return id == party.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public int compareTo(@NonNull Object o) {
        if (getClass() != o.getClass()) return -1;
        Party party = (Party) o;
        return -getDate().compareTo(party.getDate());
    }
}
