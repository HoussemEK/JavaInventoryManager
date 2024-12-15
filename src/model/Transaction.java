package model;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private int member_id; // Correspond à member_id dans la base de données
    private int item_id;   // Correspond à item_id dans la base de données
    private LocalDate borrow_date; // Correspond à borrow_date dans la base de données
    private LocalDate return_date; // Correspond à return_date dans la base de données

    public Transaction(int id, int member_id, int item_id, LocalDate borrow_date, LocalDate return_date) {
        this.id = id;
        this.member_id = member_id;
        this.item_id = item_id;
        this.borrow_date = borrow_date;
        this.return_date = return_date;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public LocalDate getBorrow_date() {
        return borrow_date;
    }

    public void setBorrow_date(LocalDate borrow_date) {
        this.borrow_date = borrow_date;
    }

    public LocalDate getReturn_date() {
        return return_date;
    }

    public void setReturn_date(LocalDate return_date) {
        this.return_date = return_date;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", member_id=" + member_id +
                ", item_id=" + item_id +
                ", borrow_date=" + borrow_date +
                ", return_date=" + return_date +
                '}';
    }
}
