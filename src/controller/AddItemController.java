package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Item;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddItemController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField costField;

    @FXML
    private void handleAddItem() {
        String name = nameField.getText();
        String quantityText = quantityField.getText();
        String costText = costField.getText();

        // Validation des champs
        if (name.isEmpty() || quantityText.isEmpty() || costText.isEmpty()) {
            showAlert("Error", "Please fill out all fields.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double cost = Double.parseDouble(costText);

            String sql = "INSERT INTO items (name, quantity, cost) VALUES (?, ?, ?)";
            try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, quantity);
                ps.setDouble(3, cost);
                ps.executeUpdate();
                showAlert("Success", "Item added successfully!");

                // Fermer la fenêtre
                ((Stage) nameField.getScene().getWindow()).close();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Quantity and cost must be numeric.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add the item.");
        }
    }

    @FXML
    private void handleCancel() {
        // Fermer la fenêtre
        ((Stage) nameField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
