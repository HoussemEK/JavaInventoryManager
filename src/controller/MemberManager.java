package controller;

import model.Member;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberManager {
    public void addMember(Member member) {
        String m = "INSERT INTO members (name, email, role) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(m)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getRole());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String m = "SELECT * FROM members";
        try (Connection conn = DatabaseConnection.getInstance(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(m)) {
            while (rs.next()) {
                members.add(new Member(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("role")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public Member getMemberById(int id) {
        String m = "SELECT * FROM members WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(m)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Member(rs.getInt("id"), rs.getString("name"), rs.getString("email"), rs.getString("role"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateMember(int id, String name, String email, String role) {
        String m = "UPDATE members SET name = ?, email = ?, role = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(m)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, role);
            ps.setInt(4, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteMember(int id) {
        String m = "DELETE FROM members WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(m)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}