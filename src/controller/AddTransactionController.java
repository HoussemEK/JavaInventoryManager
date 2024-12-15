package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class AddTransactionController {

    @FXML
    private ComboBox<String> memberComboBox;

    @FXML
    private ComboBox<String> itemComboBox;

    @FXML
    private DatePicker returnDatePicker;

    @FXML
    private void initialize() {
        loadMembers();
        loadItems();
    }

    private void loadMembers() {
        ObservableList<String> members = FXCollections.observableArrayList();
        String query = "SELECT id, name FROM members";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(query);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                members.add(rs.getInt("id") + " - " + rs.getString("name"));
            }
            memberComboBox.setItems(members);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadItems() {
        ObservableList<String> items = FXCollections.observableArrayList();
        String query = "SELECT id, name FROM items";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(query);
             var rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(rs.getInt("id") + " - " + rs.getString("name"));
            }
            itemComboBox.setItems(items);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddTransaction() {
        String selectedMember = memberComboBox.getValue();
        String selectedItem = itemComboBox.getValue();
        LocalDate returnDate = returnDatePicker.getValue();

        if (selectedMember == null || selectedItem == null) {
            showAlert("Error", "Please select both a member and an item.");
            return;
        }

        int memberId = Integer.parseInt(selectedMember.split(" - ")[0]);
        int itemId = Integer.parseInt(selectedItem.split(" - ")[0]);
        LocalDate borrowDate = LocalDate.now(); // Date d'emprunt (aujourd'hui)

        String query = "INSERT INTO transactions (member_id, item_id, borrow_date, return_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, memberId);
            ps.setInt(2, itemId);
            ps.setDate(3, java.sql.Date.valueOf(borrowDate));
            ps.setDate(4, returnDate != null ? java.sql.Date.valueOf(returnDate) : null);
            ps.executeUpdate();

            showAlert("Success", "Transaction added successfully!");
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add transaction.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) memberComboBox.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
