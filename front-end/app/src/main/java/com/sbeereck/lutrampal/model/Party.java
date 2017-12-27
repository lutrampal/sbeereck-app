package com.sbeereck.lutrampal.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class to represent a party organized by S'Beer Eck.
 */
public class Party implements Comparable, Serializable {

    public Map<Product, BeerCategory> servedBeers;
    /**
     * The mName of the party.
     */
    private String mName;
    /**
     * The mDate of the party.
     */
    private Date mDate;
    /**
     * How many people attended the party.
     */
    private int mNumberOfAttendees;
    /**
     * How much mIncome was earned during this party.
     */
    private float mIncome;
    private List<Transaction> transactions;
    private float specialBeerPrice;
    private float normalBeerPrice;

    public Party(String mName, Date mDate, int mNumberOfAttendees, float mIncome,
                 float normalBeerPrice, float specialBeerPrice) {
        this.mName = mName;
        this.mDate = mDate;
        this.mNumberOfAttendees = mNumberOfAttendees;
        this.mIncome = mIncome;
        this.specialBeerPrice = specialBeerPrice;
        this.normalBeerPrice = normalBeerPrice;
        this.servedBeers = new HashMap<>();
        this.transactions = new ArrayList<>();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public float getIncome() {
        return mIncome;
    }

    public void setIncome(float mIncome) {
        this.mIncome = mIncome;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public int getNumberOfAttendees() {
        return mNumberOfAttendees;
    }

    public void setNumberOfAttendees(int mNumberOfAttendees) {
        this.mNumberOfAttendees = mNumberOfAttendees;
    }

    public Map<Product, BeerCategory> getServedBeers() {
        return servedBeers;
    }

    public void setServedBeers(Map<Product, BeerCategory> servedBeers) {
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

    @Override
    public String toString() {
        return "Party{" +
                "mName='" + mName + '\'' +
                ", mDate=" + mDate +
                ", mNumberOfAttendees=" + mNumberOfAttendees +
                ", mIncome=" + mIncome +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Party party = (Party) o;

        if (getName() != null ? !getName().equals(party.getName()) : party.getName() != null)
            return false;
        return getDate() != null ? getDate().equals(party.getDate()) : party.getDate() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        return result;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        if (getClass() != o.getClass()) return -1;
        Party party = (Party) o;
        return getDate().compareTo(party.getDate());
    }
}
