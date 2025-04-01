package group3.StorageGUI;

import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StoragePage extends Application {

    private TableView<Item> warehouseTable = new TableView<>();
    private TableView<Item> assemblerTable = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Storage Page");

        // Initialize tables
        setupTable(warehouseTable);
        setupTable(assemblerTable);

        // Load data into tables
        warehouseTable.setItems(getSampleWarehouseData());
        assemblerTable.setItems(getSampleAssemblerData());

        // Labels for tables
        Label titleLabel = new Label("Storage Page");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 25px;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(javafx.geometry.Pos.CENTER);
        Label subtitleLabel = new Label("Warehouse and Assembler info");
        subtitleLabel.setStyle("-fx-font-weight: bold;");
        subtitleLabel.setMaxWidth(Double.MAX_VALUE);
        subtitleLabel.setAlignment(javafx.geometry.Pos.CENTER);
        Label warehouseLabel = new Label("Warehouse");
        warehouseLabel.setStyle("-fx-font-weight: bold;");
        Label assemblerLabel = new Label("Assembler");
        assemblerLabel.setStyle("-fx-font-weight: bold;");

        // Layout
        VBox layout = new VBox(10, titleLabel, subtitleLabel, warehouseLabel, warehouseTable, assemblerLabel, assemblerTable);
        Scene scene = new Scene(layout, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTable(TableView<Item> table) {
        TableColumn<Item, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Item, String> contentColumn = new TableColumn<>("Content");
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

        table.getColumns().addAll(idColumn, contentColumn);
    }

    private ObservableList<Item> getSampleWarehouseData() {
        return FXCollections.observableArrayList(
                new Item(1, "Item A"),
                new Item(2, "Item B")
        );
    }

    private ObservableList<Item> getSampleAssemblerData() {
        return FXCollections.observableArrayList(
                new Item(3, "Item C"),
                new Item(4, "Item D")
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}
