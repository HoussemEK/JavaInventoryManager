package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Member;
import utils.DatabaseConnection;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MembersController {

    @FXML
    private TableView<Member> membersTable;

    @FXML
    private TableColumn<Member, Integer> idColumn;

    @FXML
    private TableColumn<Member, String> nameColumn;

    @FXML
    private TableColumn<Member, String> emailColumn;

    @FXML
    private TableColumn<Member, String> roleColumn;

    private ObservableList<Member> memberList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configurer les colonnes avec les attributs de Member
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Chargement initial des membres
        loadMembers();
    }

    /**
     * Charger les membres depuis la base de données
     */
    private void loadMembers() {
        memberList.clear();
        String query = "SELECT id, name, email, role FROM members";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                memberList.add(new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        "", // Le mot de passe n'est pas chargé pour des raisons de sécurité
                        rs.getString("role")
                ));
            }
            membersTable.setItems(memberList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load members.");
        }
    }

    /**
     * Ajouter un nouveau membre
     */
    @FXML
    private void handleAddMember() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddMember.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Member");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadMembers(); // Rafraîchir la liste après ajout
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Add Member window.");
        }
    }


    /**
     * Modifier un membre sélectionné
     */
    @FXML
    private void handleEditMember() {
        Member selectedMember = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert("Error", "Please select a member to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditMember.fxml"));
            Parent root = loader.load();

            // Passer le membre sélectionné au contrôleur EditMemberController
            EditMemberController controller = loader.getController();
            controller.setMember(selectedMember);

            Stage stage = new Stage();
            stage.setTitle("Edit Member");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadMembers(); // Rafraîchir la liste après modification
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Edit Member window.");
        }
    }

    /**
     * Supprimer un membre sélectionné
     */
    @FXML
    private void handleDeleteMember() {
        // Récupérer l'élément sélectionné dans la table
        Member selectedMember = membersTable.getSelectionModel().getSelectedItem();
        if (selectedMember == null) {
            showAlert("Error", "Please select a member to delete.");
            return;
        }

        // Demander une confirmation avant de supprimer
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Are you sure you want to delete this member?");
        confirmAlert.setContentText("Name: " + selectedMember.getName() + "\nEmail: " + selectedMember.getEmail());
        
        // Si l'utilisateur clique sur "OK", on supprime le membre
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String query = "DELETE FROM members WHERE id = ?";
                try (Connection conn = DatabaseConnection.getInstance(); 
                     PreparedStatement ps = conn.prepareStatement(query)) {

                    ps.setInt(1, selectedMember.getId());
                    ps.executeUpdate();

                    showAlert("Success", "Member deleted successfully.");
                    loadMembers(); // Recharger la liste des membres
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to delete the member.");
                }
            }
        });
    }


    /**
     * Rafraîchir la liste
     */
    @FXML
    private void handleRefresh() {
        loadMembers();
    }

    /**
     * Retourner au tableau de bord
     */
    @FXML
    private void goToDashboard() {
        try {
            Stage stage = (Stage) membersTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Afficher une alerte
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
