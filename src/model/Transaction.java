package model;

import java.time.LocalDate;

public class Transaction {
    private int id;
    private int memberId;
    private int itemId;
    private LocalDate borrowDate;
    private LocalDate returnDate;

    
    public Transaction(int id, int memberId, int itemId, LocalDate borrowDate, LocalDate returnDate) {
        this.id = id;
        this.memberId = memberId;
        this.itemId = itemId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Transaction [id=" + id + ", memberId=" + memberId + ", itemId=" + itemId +
               ", borrowDate=" + borrowDate + ", returnDate=" + returnDate + "]";
    }
}
