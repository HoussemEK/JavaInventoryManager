package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Transaction;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class EditTransactionController {

    @FXML
    private TextField memberIdField;

    @FXML
    private TextField itemIdField;

    @FXML
    private DatePicker borrowDatePicker;

    @FXML
    private DatePicker returnDatePicker;

    private Transaction transaction; // La transaction à modifier

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;

        // Remplir les champs avec les données de la transaction sélectionnée
        memberIdField.setText(String.valueOf(transaction.getMember_id()));
        itemIdField.setText(String.valueOf(transaction.getItem_id()));
        borrowDatePicker.setValue(transaction.getBorrow_date());
        returnDatePicker.setValue(transaction.getReturn_date());
    }

    @FXML
    private void handleSaveChanges() {
        try {
            int memberId = Integer.parseInt(memberIdField.getText());
            int itemId = Integer.parseInt(itemIdField.getText());
            LocalDate borrowDate = borrowDatePicker.getValue();
            LocalDate returnDate = returnDatePicker.getValue();

            String query = "UPDATE transactions SET member_id = ?, item_id = ?, borrow_date = ?, return_date = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, memberId);
                ps.setInt(2, itemId);
                ps.setObject(3, borrowDate);
                ps.setObject(4, returnDate);
                ps.setInt(5, transaction.getId());
                ps.executeUpdate();

                showAlert("Success", "Transaction updated successfully.");
                closeWindow();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Member ID and Item ID must be numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update the transaction.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) memberIdField.getScene().getWindow();
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
