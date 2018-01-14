package com.sbeereck.lutrampal.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable, Comparable {

    private int id;
    private Member member;
    private Party party;
    private float amount;
    private String label;
    private Date timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Transaction(int id, Member member, Party party, float amount, String label, Date timestamp) {
        this.id = id;
        this.member = member;
        this.party = party;
        this.amount = amount;
        this.label = label;
        this.timestamp = timestamp;
    }

    public Transaction(Member member, Party party, float amount, String label) {
        this.member = member;
        this.party = party;
        this.amount = amount;
        this.label = label;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }


    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Transaction) {
            return -getTimestamp().compareTo(((Transaction)o).getTimestamp());
        }
        return 0;
    }
}
