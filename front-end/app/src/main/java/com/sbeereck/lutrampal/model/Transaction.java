package com.sbeereck.lutrampal.model;

import java.io.Serializable;

public class Transaction implements Serializable {

    private Member member;
    private Party party;
    private float totalAmount;
    private String label;
    private long timestamp;

    public Transaction(Member member, float totalAmount, String label, long timestamp) {
        this.member = member;
        this.totalAmount = totalAmount;
        this.label = label;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
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

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
}
