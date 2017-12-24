package com.sbeereck.lutrampal.model;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * A class to represent a party organized by S'Beer Eck.
 */
public class Party implements Comparable {

    /**
     * The name of the party.
     */
    private String name;
    /**
     * The date of the party.
     */
    private Date date;
    /**
     * How many people attended the party.
     */
    private int numberOfAttendees;

    /**
     * How much income was earned during this party.
     */
    private float income;

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNumberOfAttendees() {
        return numberOfAttendees;
    }

    public void setNumberOfAttendees(int numberOfAttendees) {
        this.numberOfAttendees = numberOfAttendees;
    }

    public Party(String name, Date date, int numberOfAttendees, float income) {
        this.name = name;
        this.date = date;
        this.numberOfAttendees = numberOfAttendees;
        this.income = income;
    }

    @Override
    public String toString() {
        return "Party{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", numberOfAttendees=" + numberOfAttendees +
                ", income=" + income +
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
