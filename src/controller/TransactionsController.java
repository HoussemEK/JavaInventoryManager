package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Transaction;
import utils.DatabaseConnection;
import javafx.scene.control.cell.PropertyValueFactory;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class TransactionsController {

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, Integer> idColumn;

    @FXML
    private TableColumn<Transaction, Integer> memberIdColumn;

    @FXML
    private TableColumn<Transaction, Integer> itemIdColumn;

    @FXML
    private TableColumn<Transaction, LocalDate> borrowDateColumn;

    @FXML
    private TableColumn<Transaction, LocalDate> returnDateColumn;

    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configurer les colonnes pour utiliser les attributs de Transaction
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("member_id")); // Correspond à la colonne member_id
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("item_id"));     // Correspond à la colonne item_id
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrow_date")); // Correspond à borrow_date
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("return_date")); // Correspond à return_date

        loadTransactions(); // Charger les transactions depuis la base de données
    }



    private void loadTransactions() {
        transactionList.clear();
        String query = "SELECT * FROM transactions";

        try (Connection conn = DatabaseConnection.getInstance();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                transactionList.add(new Transaction(
                        rs.getInt("id"),
                        rs.getInt("member_id"), // Utilisation de member_id
                        rs.getInt("item_id"),   // Utilisation de item_id
                        rs.getDate("borrow_date").toLocalDate(), // Conversion de borrow_date
                        rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null // Conversion de return_date (nullable)
                ));
            }
            transactionsTable.setItems(transactionList);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load transactions.");
        }
    }


    @FXML
    private void handleAddTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddTransaction.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add Transaction");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadTransactions(); // Rafraîchir les transactions après l'ajout
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDeleteTransaction() {
        Transaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showAlert("Error", "Please select a transaction to delete.");
            return;
        }

        String query = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, selectedTransaction.getId());
            ps.executeUpdate();

            showAlert("Success", "Transaction deleted successfully.");
            loadTransactions();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete the transaction.");
        }
    }

    @FXML
    private void handleRefresh() {
        loadTransactions();
    }

    @FXML
    private void goToDashboard() {
        try {
            Stage stage = (Stage) transactionsTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
