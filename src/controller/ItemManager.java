package controller;

import model.Item;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    public void addItem(Item item) {
        String i = "INSERT INTO items (name, quantity, cost) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(i)) {
            ps.setString(1, item.getName());
            ps.setInt(2, item.getQuantity());
            ps.setDouble(3, item.getCost());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        String i = "SELECT * FROM items";
        try (Connection conn = DatabaseConnection.getInstance(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(i)) {
            while (rs.next()) {
                items.add(new Item(rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), rs.getDouble("cost")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public Item getItemById(int id) {
        String i = "SELECT * FROM items WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(i)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Item(rs.getInt("id"), rs.getString("name"), rs.getInt("quantity"), rs.getDouble("cost"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateItem(int id, String name, int quantity, double cost) {
        String i = "UPDATE items SET name = ?, quantity = ?, cost = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(i)) {
            ps.setString(1, name);
            ps.setInt(2, quantity);
            ps.setDouble(3, cost);
            ps.setInt(4, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteItem(int id) {
        String i = "DELETE FROM items WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(i)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
