package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Member;

public class SignUpController {
    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleBox;

    @FXML
    private void initialize() {
        // Populate the ComboBox with role options
        roleBox.setItems(FXCollections.observableArrayList("Admin", "Manager", "Member"));
    }

    @FXML
    private void handleSignUp() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String role = roleBox.getValue();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role == null) {
            System.out.println("Please fill out all fields.");
            return;
        }

        MemberManager memberManager = new MemberManager();
        memberManager.addMember(new Member(0, name, email,  password, role));
        System.out.println("Sign-Up successful for: " + name);
    }

    @FXML
    private void goToLogin() {
        try {
            Stage stage = (Stage) nameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
            stage.setScene(new Scene(root, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
