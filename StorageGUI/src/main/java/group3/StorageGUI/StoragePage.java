package group3.StorageGUI;

import group3.component.common.services.IGUIProcessingService;
import group3.component.common.API.IWarehouseAPIProcessingService;
import group3.component.common.API.Item;
import group3.component.common.API.Warehouse;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ServiceLoader;

public class StoragePage extends Application implements IWarehouseAPIProcessingService, IGUIProcessingService {

    private TableView<Item> warehouseTable = new TableView<>();
    private TableView<Item> assemblerTable = new TableView<>();
    private IWarehouseAPIProcessingService ApiService;

    @Override
    public void start(Stage primaryStage) {

        ServiceLoader<IWarehouseAPIProcessingService> services = ServiceLoader.load(IWarehouseAPIProcessingService.class);

        for (IWarehouseAPIProcessingService service : services) {
            ApiService = service;
        }

        primaryStage.setTitle("Storage Page");

        setupTable(warehouseTable);
        setupTable(assemblerTable);

        try {
            Warehouse warehouse = ApiService.getWarehouseInfo();
            warehouseTable.setItems(FXCollections.observableArrayList(warehouse.getItems())); // Set warehouse items
        } catch (IOException e) {
            e.printStackTrace();
        }

        Label titleLabel = new Label("Storage Page");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 28px;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        Label subtitleLabel = new Label("Warehouse and Assembler Information");
        subtitleLabel.setStyle("-fx-font-size: 16px; -fx-font-style: italic;");
        subtitleLabel.setMaxWidth(Double.MAX_VALUE);
        subtitleLabel.setAlignment(Pos.CENTER);

        Label warehouseLabel = new Label("Warehouse");
        warehouseLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label assemblerLabel = new Label("Assembler");
        assemblerLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

      
        warehouseTable.setTooltip(new Tooltip("Displays items in the warehouse"));
        assemblerTable.setTooltip(new Tooltip("Displays items in the assembler"));

       
        Button refreshButton = new Button("Refresh Data");
        refreshButton.setOnAction(e -> refreshData());

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> primaryStage.close());

        HBox buttonBox = new HBox(10, refreshButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);

        
        VBox layout = new VBox(15, titleLabel, subtitleLabel, warehouseLabel, warehouseTable, assemblerLabel, assemblerTable, buttonBox);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTable(TableView<Item> table) {
        TableColumn<Item, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(200);

        TableColumn<Item, String> contentColumn = new TableColumn<>("Content");
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        contentColumn.setPrefWidth(400);

        table.getColumns().addAll(idColumn, contentColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void refreshData() {
        try {
            Warehouse warehouse = ApiService.getWarehouseInfo();
            warehouseTable.setItems(FXCollections.observableArrayList(warehouse.getItems()));
            // Uncomment and implement assembler data fetching if needed
            // Warehouse assembler = ApiService.getAssemblerInfo();
            // assemblerTable.setItems(FXCollections.observableArrayList(assembler.getItems()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Warehouse getWarehouseInfo() throws IOException {
        return ApiService.getWarehouseInfo();
    }

    public Warehouse getAssemblerInfo() throws IOException {
        return ApiService.getWarehouseInfo();
    }

    @Override
    public String commandAGV(String command, String location) throws IOException {
        return ApiService.commandAGV(command, location);
    }

    @Override
    public String pickWarehouseItem(String id) throws IOException {
        return ApiService.pickWarehouseItem(id);
    }

    @Override
    public String insertWarehouseItem(String id, String name) throws IOException {
        return ApiService.insertWarehouseItem(id, name);
    }

    @Override
    public Warehouse getWarehouseFromString(String response) {
        return ApiService.getWarehouseFromString(response);
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
}