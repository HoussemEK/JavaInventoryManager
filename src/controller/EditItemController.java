package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Item;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditItemController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField costField;

    private Item item; // L'article à modifier

    public void setItem(Item item) {
        this.item = item;

        // Pré-remplir les champs avec les valeurs de l'article sélectionné
        nameField.setText(item.getName());
        quantityField.setText(String.valueOf(item.getQuantity()));
        costField.setText(String.valueOf(item.getCost()));
    }

    @FXML
    private void handleSaveChanges() {
        String name = nameField.getText();
        String quantityText = quantityField.getText();
        String costText = costField.getText();

        if (name.isEmpty() || quantityText.isEmpty() || costText.isEmpty()) {
            showAlert("Error", "Please fill out all fields.");
            return;
        }

        try {
            int quantity = Integer.parseInt(quantityText);
            double cost = Double.parseDouble(costText);

            String sql = "UPDATE items SET name = ?, quantity = ?, cost = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, quantity);
                ps.setDouble(3, cost);
                ps.setInt(4, item.getId());
                ps.executeUpdate();

                showAlert("Success", "Item updated successfully!");
                ((Stage) nameField.getScene().getWindow()).close();
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Quantity and cost must be numeric.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update the item.");
        }
    }

    @FXML
    private void handleCancel() {
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
