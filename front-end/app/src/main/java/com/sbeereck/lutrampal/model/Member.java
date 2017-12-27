package com.sbeereck.lutrampal.model;

import java.io.Serializable;

/**
 * A class to represent a member of the association who attends parties.
 */
public class Member implements Serializable {

    private String mName;
    private float mBalance;
    private School mSchool;
    private String mEmail;
    private String mPhone;
    /**
     * Indicates whether or not the member has payed its membership fee this year.
     */
    private Boolean mMembership;

    public Member(String mName, float mBalance, School mSchool, String mEmail, String mPhone,
                  Boolean mMembership) {
        this.mName = mName;
        this.mBalance = mBalance;
        this.mSchool = mSchool;
        this.mEmail = mEmail;
        this.mPhone = mPhone;
        this.mMembership = mMembership;
    }

    public Boolean getMembership() {
        return mMembership;
    }

    public void setMembership(Boolean mMembershipFee) {
        this.mMembership = mMembershipFee;
    }

    public String getName() {

        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public float getBalance() {
        return mBalance;
    }

    public void setBalance(float mBalance) {
        this.mBalance = mBalance;
    }

    public School getSchool() {
        return mSchool;
    }

    public void setSchool(School mSchool) {
        this.mSchool = mSchool;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        if (getName() != null ? !getName().equals(member.getName()) : member.getName() != null)
            return false;
        return getEmail() != null ? getEmail().equals(member.getEmail()) : member.getEmail() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Member{" +
                "mName='" + mName + '\'' +
                ", mBalance=" + mBalance +
                ", mSchool=" + mSchool +
                ", mEmail='" + mEmail + '\'' +
                ", mPhone='" + mPhone + '\'' +
                '}';
    }
}
