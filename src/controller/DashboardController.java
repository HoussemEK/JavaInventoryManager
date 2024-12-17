package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.UserSession;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button itemsButton;

    @FXML
    private Button membersButton;

    @FXML
    private Button transactionsButton;

    @FXML
    private void initialize() {
        // Récupération des informations de l'utilisateur depuis UserSession
        String username = UserSession.getUsername();
        String role = UserSession.getRole();

        // Affichage du message de bienvenue
        welcomeLabel.setText("Welcome, " + username + " (" + role + ")");

        // Configuration des boutons en fonction du rôle
        if ("admin".equalsIgnoreCase(role)) {
            itemsButton.setVisible(true);
            membersButton.setVisible(true);
            transactionsButton.setVisible(true);
        } else if ("manager".equalsIgnoreCase(role)) {
            itemsButton.setVisible(true);
            membersButton.setVisible(false);
            transactionsButton.setVisible(true);
        } else if ("member".equalsIgnoreCase(role)) {
            itemsButton.setVisible(false);
            membersButton.setVisible(false);
            transactionsButton.setVisible(true);
        } else {
            // Si le rôle est inconnu, cacher tous les boutons
            itemsButton.setVisible(false);
            membersButton.setVisible(false);
            transactionsButton.setVisible(false);
        }
    }

    @FXML
    private void goToItems() {
        try {
            Stage stage = (Stage) itemsButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Items.fxml"));
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToMembers() {
        try {
            Stage stage = (Stage) membersButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Members.fxml"));
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToTransactions() {
        try {
            Stage stage = (Stage) transactionsButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Transactions.fxml"));
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        UserSession.clearSession();
        try {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/LoginPage.fxml"));
            stage.setScene(new Scene(root, 600, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Logged out.");
    }
}
