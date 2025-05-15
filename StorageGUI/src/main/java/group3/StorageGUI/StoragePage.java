package group3.StorageGUI;

import group3.component.common.services.IGUIProcessingService;
import group3.component.common.API.IWarehouseAPIProcessingService;
import group3.component.common.API.Item;
import group3.component.common.API.Warehouse;
import group3.component.common.services.IWarehouseGUIProcessingService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.ServiceLoader;

public class StoragePage extends Application implements IGUIProcessingService, IWarehouseGUIProcessingService {

    private TableView<Item> warehouseTable = new TableView<>();
    private TableView<Item> assemblerTable = new TableView<>();

    private IWarehouseAPIProcessingService ApiService;

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Storage Page");

        setupTable(warehouseTable);
        setupTable(assemblerTable);

        try {
            Warehouse warehouse = ApiService.getWarehouseInfo();
            warehouseTable.setItems(FXCollections.observableArrayList(warehouse.getItems()));  // Set warehouse items
/*
            Warehouse assembler = ApiService.getAssemblerInfo();  // Fetch assembler data
            assemblerTable.setItems(FXCollections.observableArrayList(assembler.getItems()));  // Set assembler items
 */
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create labels and layout
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

        // Layout setup
        VBox layout = new VBox(10, titleLabel, subtitleLabel, warehouseLabel, warehouseTable, assemblerLabel, assemblerTable);
        Scene scene = new Scene(layout, 600, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTable(TableView<Item> table) {
        TableColumn<Item, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Item, String> contentColumn = new TableColumn<>("Content");
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));

        table.getColumns().addAll(idColumn, contentColumn);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(Stage stage) {
        start(stage);
    }

    @Override
    public Button getButton() {
        return new Button("Storage Page");
    }

    @Override
    public void initializeServices(IWarehouseAPIProcessingService apiProcessingService) {
        ApiService = apiProcessingService;
    }
}
