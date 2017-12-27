package com.sbeereck.lutrampal.applisbeereck;

import com.sbeereck.lutrampal.model.BeerCategory;
import com.sbeereck.lutrampal.model.Member;
import com.sbeereck.lutrampal.model.Party;
import com.sbeereck.lutrampal.model.Product;
import com.sbeereck.lutrampal.model.ProductType;
import com.sbeereck.lutrampal.model.School;
import com.sbeereck.lutrampal.model.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lutrampal on 26/12/2017.
 */

public class Placeholders {

    public static List<Party> getPlaceHolderParties() {
        List<Party> parties = new ArrayList<>();
        parties.add(new Party("Soirée 1", new Date(2017, 12, 25), 200, 2000, 1.35f, 1.70f));
        parties.get(0).setTransactions(getPlaceHolderTransactions());
        List<Product> beers = getPlaceHolderBeers();
        parties.get(0).getServedBeers().put(beers.get(0), BeerCategory.SPECIAL);
        parties.get(0).getServedBeers().put(beers.get(2), BeerCategory.NORMAL);
        parties.add(new Party("Soirée 2", new Date(2017, 12, 24), 50, 2000, 1.35f, 1.70f));
        parties.add(new Party("Soirée 3", new Date(2017, 12, 11), 50, -8000, 1.35f, 1.70f));
        return parties;
    }

    public static List<Member> getPlaceHolderMembers() {
        List<Member> members = new ArrayList<>();
        members.add(new Member("Kerboul Thomas", -50, School.ENSE3, "kerboul@gmail.com", "0635368817", true));
        members.add(new Member("Bertaux Benjamin", 10.5f, School.ENSIMAG, "ber.ben@gmail.com", "0635368817", false));
        members.add(new Member("Trampal Lucas", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Georges", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Benoît", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Nicolas", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal William", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Florent", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Robert", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Michel", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Michelle", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal José", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Hugo", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Huguette", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Denis", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Benjamin", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        members.add(new Member("Trampal Trampal", 100, School.ENSIMAG, "lt@gmail.com", "0635368817", true));
        return members;
    }

    public static List<Product> getPlaceHolderProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product("Triple K", 2f, ProductType.BEER));
        products.add(new Product("Chouffe", 2.5f, ProductType.BEER));
        products.add(new Product("Caution pinte", 1f, ProductType.DEPOSIT));
        products.add(new Product("Caution pichet", 3f, ProductType.DEPOSIT));
        products.add(new Product("Saucisson", 3f, ProductType.FOOD));
        return products;
    }

    public static List<Transaction> getPlaceHolderTransactions() {
        List<Member> members = getPlaceHolderMembers();
        List<Transaction> transactions = new LinkedList<>();
        transactions.add(new Transaction(members.get(0), -8f, "2 pintes de chouffe", System.currentTimeMillis()));
        transactions.add(new Transaction(members.get(1), +1f, "1 caution pinte rendue", System.currentTimeMillis() - 60000));
        transactions.add(new Transaction(members.get(1), -1f, "1 caution pinte", System.currentTimeMillis() - 60000 * 2));
        transactions.add(new Transaction(members.get(2), -4f, "1 demi de Triple K", System.currentTimeMillis() - 60000 * 3));
        return transactions;
    }

    public static List<Product> getPlaceHolderBeers() {
        List<Product> beers = new ArrayList<>();
        beers.add(new Product("Triple K", 2.5f, ProductType.BEER));
        beers.add(new Product("Chouffe", 3f, ProductType.BEER));
        beers.add(new Product("Sassenage blonde", 1.5f, ProductType.BEER));
        beers.add(new Product("Sassenage rousse", 1.5f, ProductType.BEER));
        beers.add(new Product("Punk IPA", 1.5f, ProductType.BEER));
        return beers;
    }

    public static List<Product> getPlaceHolderDeposits() {
        List<Product> deposits = new ArrayList<>();
        deposits.add(new Product("Caution demi", 1f, ProductType.DEPOSIT));
        deposits.add(new Product("Caution pinte", 2f, ProductType.DEPOSIT));
        deposits.add(new Product("Caution pichet", 3f, ProductType.DEPOSIT));
        return deposits;
    }

    public static List<Product> getPlaceHolderFood() {
        List<Product> food = new ArrayList<>();
        food.add(new Product("Saucisson", 3f, ProductType.FOOD));
        return food;
    }
}
