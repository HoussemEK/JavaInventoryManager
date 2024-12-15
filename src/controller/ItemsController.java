package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Item;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ItemsController {

    @FXML
    private TableView<Item> itemsTable;

    @FXML
    private TableColumn<Item, Integer> idColumn;

    @FXML
    private TableColumn<Item, String> nameColumn;

    @FXML
    private TableColumn<Item, Integer> quantityColumn;

    @FXML
    private TableColumn<Item, Double> costColumn;

    @FXML
    private Button addButton, editButton, deleteButton, refreshButton;

    private ObservableList<Item> itemList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialiser les colonnes du TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

        loadItems(); // Charger les articles au démarrage
    }

    @FXML
    private void handleAddItem() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddItem.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Add New Item");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadItems(); // Rafraîchir la liste des articles après ajout
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleEditItem() {
        Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditItem.fxml"));
            Parent root = loader.load();

            // Passer l'article sélectionné au contrôleur EditItemController
            EditItemController controller = loader.getController();
            controller.setItem(selectedItem);

            Stage stage = new Stage();
            stage.setTitle("Edit Item");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadItems(); // Rafraîchir la liste après modification
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDeleteItem() {
        Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Error", "Please select an item to delete.");
            return;
        }

        String sql = "DELETE FROM items WHERE id = ?";
        try (Connection conn = DatabaseConnection.getInstance(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, selectedItem.getId());
            ps.executeUpdate();

            showAlert("Success", "Item deleted successfully!");
            loadItems(); // Rafraîchir la liste après suppression
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete the item.");
        }
    }

    @FXML
    private void handleRefresh() {
        loadItems();
    }

    @FXML
    private void goToDashboard() {
        try {
            Stage stage = (Stage) itemsTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Dashboard.fxml"));
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadItems() {
        itemList.clear(); // Nettoyer la liste avant de charger
        String query = "SELECT * FROM items";

        try (Connection conn = DatabaseConnection.getInstance();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                itemList.add(new Item(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("cost")
                ));
            }
            itemsTable.setItems(itemList);
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
