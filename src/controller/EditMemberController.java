package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Member;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class EditMemberController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private Member member;

    @FXML
    private void initialize() {
        // Initialiser les valeurs du ComboBox
        roleComboBox.getItems().addAll("Admin", "Manager", "Member");
    }

    public void setMember(Member member) {
        this.member = member;
        nameField.setText(member.getName());
        emailField.setText(member.getEmail());
        roleComboBox.setValue(member.getRole());
    }

    @FXML
    private void handleSave() {
        if (nameField.getText().isEmpty() || emailField.getText().isEmpty() || roleComboBox.getValue() == null) {
            showAlert("Error", "All fields must be filled.");
            return;
        }

        String query = "UPDATE members SET name = ?, email = ?, role = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, nameField.getText());
            ps.setString(2, emailField.getText());
            ps.setString(3, roleComboBox.getValue());
            ps.setInt(4, member.getId());
            ps.executeUpdate();

            showAlert("Success", "Member updated successfully.");
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update member.");
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
