package controller;

import model.Transaction;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionManager {
    public void addTransaction(Transaction transaction) {
        String t = "INSERT INTO transactions (member_id, item_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(t)) {
            ps.setInt(1, transaction.getMember_id());
            ps.setInt(2, transaction.getItem_id());
            ps.setDate(3, Date.valueOf(transaction.getBorrow_date()));
            ps.setDate(4, transaction.getReturn_date() != null ? Date.valueOf(transaction.getReturn_date()) : null);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String t = "SELECT * FROM transactions";
        try (Connection conn = DatabaseConnection.getInstance(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(t)) {
            while (rs.next()) {
                transactions.add(new Transaction(
                    rs.getInt("id"),
                    rs.getInt("member_id"),
                    rs.getInt("item_id"),
                    rs.getDate("borrow_date").toLocalDate(),
                    rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public Transaction getTransactionById(int id) {
        String t = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(t)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Transaction(
                        rs.getInt("id"),
                        rs.getInt("member_id"),
                        rs.getInt("item_id"),
                        rs.getDate("borrow_date").toLocalDate(),
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateTransaction(int id, int memberId, int itemId, Date borrowDate, Date returnDate) {
        String t = "UPDATE transactions SET member_id = ?, item_id = ?, borrow_date = ?, return_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(t)) {
            ps.setInt(1, memberId);
            ps.setInt(2, itemId);
            ps.setDate(3, borrowDate);
            ps.setDate(4, returnDate);
            ps.setInt(5, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTransaction(int id) {
        String t = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(t)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
