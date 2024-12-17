package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddMemberController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize() {
        // Initialiser les rôles disponibles dans le ComboBox
        roleComboBox.getItems().addAll("Admin", "Manager", "Member");
    }

    @FXML
    private void handleAdd() {
        // Validation des champs
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty() || roleComboBox.getValue() == null) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        String query = "INSERT INTO members (name, email, password, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nameField.getText());
            ps.setString(2, emailField.getText());
            ps.setString(3, passwordField.getText()); // Note : Assurez-vous de chiffrer le mot de passe si nécessaire
            ps.setString(4, roleComboBox.getValue());
            ps.executeUpdate();

            showAlert("Success", "Member added successfully.");
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add member.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
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
