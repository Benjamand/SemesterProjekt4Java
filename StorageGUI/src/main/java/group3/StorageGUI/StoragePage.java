package group3.StorageGUI;

import group3.component.common.services.IGUIProcessingService;
import group3.component.common.API.IWarehouseAPIProcessingService;
import group3.component.common.API.Item;
import group3.component.common.API.Warehouse;
import group3.component.common.services.IWarehouseGUIProcessingService;
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


        Label titleLabel = new Label("Storage Page");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 25px;");
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);

        Label subtitleLabel = new Label("Warehouse and Assembler Info");
        subtitleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        subtitleLabel.setMaxWidth(Double.MAX_VALUE);
        subtitleLabel.setAlignment(Pos.CENTER);

        Label warehouseLabel = new Label("Warehouse");
        warehouseLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");

        Label assemblerLabel = new Label("Assembler");
        assemblerLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");


        warehouseTable.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px;");
        assemblerTable.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-font-size: 14px;");

        warehouseTable.setTooltip(new Tooltip("Displays items in the warehouse"));
        assemblerTable.setTooltip(new Tooltip("Displays items in the assembler"));


        VBox layout = new VBox(20, titleLabel, subtitleLabel, warehouseLabel, warehouseTable, assemblerLabel, assemblerTable);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #222222;");

        Scene scene = new Scene(layout, 700, 500);


        Button refreshButton = new Button("Refresh Data");
        refreshButton.setOnAction(e -> refreshData());

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> primaryStage.close());

        HBox buttonBox = new HBox(10, refreshButton, closeButton);
        buttonBox.setAlignment(Pos.CENTER);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setupTable(TableView<Item> table) {
        TableColumn<Item, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setStyle("-fx-text-fill: white; -fx-alignment: CENTER;");
        idColumn.setPrefWidth(200);

        TableColumn<Item, String> contentColumn = new TableColumn<>("Content");
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        contentColumn.setStyle("-fx-text-fill: white; -fx-alignment: CENTER;");
        contentColumn.setPrefWidth(400);

        table.getColumns().addAll(idColumn, contentColumn);
        table.setStyle("-fx-background-color: #444444; -fx-border-color: #555555; -fx-border-width: 2px; -fx-border-radius: 5;");
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void initialize(Stage stage) {
        start(stage);
    }

    @Override
    public Button getButton() {
        Button button = new Button("Storage Page");
        button.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10 20; -fx-border-radius: 5; -fx-background-radius: 5;");
        return button;
    }

    @Override
    public void initializeServices(IWarehouseAPIProcessingService apiProcessingService) {
        ApiService = apiProcessingService;
    }
}



