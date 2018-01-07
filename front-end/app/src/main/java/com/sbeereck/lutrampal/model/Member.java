package com.sbeereck.lutrampal.model;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

/**
 * A class to represent a member of the association who attends parties.
 */
public class Member implements Serializable, Comparable {

    private int id = -1;
    private String firstName = "";
    private String lastName = "";
    private float balance = 0;
    private Date lastMembershipPayment = new Date();
    private boolean formerStaff = false;
    private String phone;
    private String email;
    private School school;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isFormerStaff() {
        return formerStaff;
    }

    public void setFormerStaff(boolean formerStaff) {
        this.formerStaff = formerStaff;
    }

    public Member(int id, String firstName, String lastName, float balance,
                  Date lastMembershipPayment, boolean formerStaff, String phone,
                  String email, School school) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.lastMembershipPayment = lastMembershipPayment;
        this.formerStaff = formerStaff;
        this.phone = phone;
        this.email = email;
        this.school = school;
    }

    public Member(int id, String firstName, String lastName, float balance,
                  Date lastMembershipPayment, boolean formerStaff) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = balance;
        this.lastMembershipPayment = lastMembershipPayment;
        this.formerStaff = formerStaff;
    }

    public Member(String firstName, String lastName, String phone, String email, School school) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.school = school;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public Date getLastMembershipPayment() {
        return lastMembershipPayment;
    }

    public void setLastMembershipPayment(Date lastMembershipPayment) {
        this.lastMembershipPayment = lastMembershipPayment;
    }

    public boolean isMembershipValid() {
        if (isFormerStaff()) {
            return true;
        }
        Date today = new Date();
        Date twentyFifthOfAugust = new Date(today.getYear(), 8, 25);
        if (today.before(twentyFifthOfAugust)) {
            twentyFifthOfAugust.setYear(twentyFifthOfAugust.getYear() - 1);
        }
        return getLastMembershipPayment().after(twentyFifthOfAugust);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        return getId() == member.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }


    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof Member) {
            int cmp = getLastName().compareTo(((Member) o).getLastName());
            if (cmp == 0) {
                return getFirstName().compareTo(((Member) o).getFirstName());
            }
            return cmp;
        }
        return 0;
    }
}
