package com.sbeereck.lutrampal.model;

import android.support.annotation.NonNull;

import java.util.Date;

/**
 * A class to represent a party organized by S'Beer Eck.
 */
public class Party implements Comparable {

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

    public Party(String mName, Date mDate, int mNumberOfAttendees, float mIncome) {
        this.mName = mName;
        this.mDate = mDate;
        this.mNumberOfAttendees = mNumberOfAttendees;
        this.mIncome = mIncome;
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
