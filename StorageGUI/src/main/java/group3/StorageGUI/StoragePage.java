package group3.StorageGUI;

import group3.component.common.services.IGUIProcessingService;
import group3.component.common.API.IWarehouseAPIProcessingService;
import group3.component.common.API.Item;
import group3.component.common.API.Warehouse;
import group3.component.common.API.AssemblyRecipe;
import group3.component.common.services.IWarehouseGUIProcessingService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

public class StoragePage extends Application implements IGUIProcessingService, IWarehouseGUIProcessingService {

    private final TableView<Item> warehouseTable = new TableView<>();
    private final TableView<AssemblyRecipe> assemblerTable = new TableView<>();

    private static IWarehouseAPIProcessingService apiService;
    private static boolean warehouseTableInitialized = false;
    private static boolean assemblerTableInitialized = false;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Storage Page");

        if (!warehouseTableInitialized) {
            setupItemTable();
            warehouseTableInitialized = true;
        }

        if (!assemblerTableInitialized) {
            setupAssemblerTable();
            assemblerTableInitialized = true;
        }

        refreshData();

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> refreshData())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        VBox layout = new VBox(20,
                createStyledLabel("Storage Page", 25, true),
                createStyledLabel("Warehouse and Assembler Info", 16, false),
                createStyledLabel("Warehouse", 18, true),
                warehouseTable,
                createStyledLabel("Assembler", 18, true),
                assemblerTable
        );

        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #222222;");

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void refreshData() {
        if (apiService == null) {
            System.err.println("API service not initialized! Cannot fetch data.");
            return;
        }

        try {
            Warehouse warehouse = apiService.getWarehouseInfo();
            warehouseTable.getItems().setAll(warehouse.getItems());
        } catch (IOException e) {
            System.err.println("Failed to load warehouse info:");
            e.printStackTrace();
        }

        try {
            List<AssemblyRecipe> recipes = apiService.getAssemblyRecipes();
            assemblerTable.getItems().setAll(recipes);
        } catch (IOException e) {
            System.err.println("Failed to load assembly recipes:");
            e.printStackTrace();
        }
    }

    private void setupItemTable() {
        TableColumn<Item, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setMinWidth(150);
        idColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Item, String> contentColumn = new TableColumn<>("Content");
        contentColumn.setCellValueFactory(new PropertyValueFactory<>("content"));
        contentColumn.setMinWidth(300);
        contentColumn.setStyle("-fx-alignment: CENTER;");

        warehouseTable.getColumns().setAll(idColumn, contentColumn);
        warehouseTable.setStyle("-fx-background-color: #444444; -fx-border-color: #555555; -fx-border-width: 2px; -fx-border-radius: 5;");
    }

    private void setupAssemblerTable() {
        TableColumn<AssemblyRecipe, String> prodCol = new TableColumn<>("Production Name");
        prodCol.setCellValueFactory(new PropertyValueFactory<>("productionName"));

        TableColumn<AssemblyRecipe, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<AssemblyRecipe, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<AssemblyRecipe, Integer> countCol = new TableColumn<>("Ingredient Count");
        countCol.setCellValueFactory(new PropertyValueFactory<>("ingredientCount"));

        assemblerTable.getColumns().setAll(prodCol, nameCol, descCol, countCol);
        assemblerTable.setStyle("-fx-background-color: #444444; -fx-border-color: #555555; -fx-border-width: 2px; -fx-border-radius: 5;");
    }

    private Label createStyledLabel(String text, int fontSize, boolean bold) {
        Label label = new Label(text);
        String fontWeight = bold ? "bold" : "normal";
        label.setStyle(String.format("-fx-text-fill: white; -fx-font-weight: %s; -fx-font-size: %dpx;", fontWeight, fontSize));
        label.setMaxWidth(Double.MAX_VALUE);
        label.setAlignment(Pos.CENTER);
        return label;
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
        apiService = apiProcessingService;
    }
}
