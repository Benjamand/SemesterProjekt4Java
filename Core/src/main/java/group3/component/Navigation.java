package group3.component;

import group3.component.common.API.IInstructionAPIProcessingService;
import group3.component.common.API.IWarehouseAPIProcessingService;
import group3.component.common.InstructionSequence.IInstructionSequenceProcessingService;
import group3.component.common.services.IGUIProcessingService;
import group3.component.common.services.IInstructionGUIProcessingService;
import group3.component.common.services.IWarehouseGUIProcessingService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.lang.module.Configuration;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Navigation extends Application {

    private static ModuleLayer layer;
    private ArrayList<Button> buttons = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Navigation Page");

        Button knapButton = new Button("Knap");
        buttons.add(knapButton);
        Button exitButton = new Button("Exit");
        buttons.add(exitButton);

        exitButton.setOnAction(e -> primaryStage.close());

        IInstructionSequenceProcessingService loadedInstructionService = getInstructionSequenceServices()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No IInstructionSequenceProcessingService implementation found."));

        IInstructionAPIProcessingService loadedInstructionAPIService = getInstructionAPIServices()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No IInstructionAPIProcessingService implementation found."));

        IWarehouseAPIProcessingService loadedWarehouseAPIService = getWarehouseAPIServices()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No IInstructionAPIProcessingService implementation found."));


        for (IGUIProcessingService iGuiPlugin : getGUIServices()) {
            if (iGuiPlugin instanceof IInstructionGUIProcessingService) {
                IInstructionGUIProcessingService instructionPlugin = (IInstructionGUIProcessingService) iGuiPlugin;
                instructionPlugin.initializeServices(loadedInstructionService, loadedInstructionAPIService);
            } else if (iGuiPlugin instanceof IWarehouseGUIProcessingService) {
                IWarehouseGUIProcessingService instructionPlugin = (IWarehouseGUIProcessingService) iGuiPlugin;
                instructionPlugin.initializeServices(loadedWarehouseAPIService);
            }
            insertButton(iGuiPlugin);
        }

        VBox layout = new VBox(20);
        layout.getChildren().addAll(buttons);

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Collection<? extends IGUIProcessingService> getGUIServices() {
        return ServiceLoader.load(layer, IGUIProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());    }

    private Collection<? extends IInstructionSequenceProcessingService> getInstructionSequenceServices() {
        return ServiceLoader.load(layer, IInstructionSequenceProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());    }

    private Collection<? extends IWarehouseAPIProcessingService> getWarehouseAPIServices() {
        return ServiceLoader.load(layer, IWarehouseAPIProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());    }

    private Collection<? extends IInstructionAPIProcessingService> getInstructionAPIServices() {
        return ServiceLoader.load(layer, IInstructionAPIProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());    }

    private Collection<? extends IInstructionGUIProcessingService> getInstructionGUIServices() {
        return ServiceLoader.load(layer, IInstructionGUIProcessingService.class).stream().map(ServiceLoader.Provider::get).collect(toList());    }


    void insertButton(IGUIProcessingService processingService) {
        Button newButton = processingService.getButton();
        newButton.setOnAction(e -> {
            Stage stage = new Stage();
            try {
                processingService.initialize(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        buttons.add(newButton);
    }

    public static void main(String[] args) {
        Path pluginsDir = Paths.get("plugins");

        ModuleFinder pluginsFinder = ModuleFinder.of(pluginsDir);

        List<String> plugins = pluginsFinder
                .findAll()
                .stream()
                .map(ModuleReference::descriptor)
                .map(ModuleDescriptor::name)
                .collect(Collectors.toList());


        Configuration pluginsConfiguration = ModuleLayer
                .boot()
                .configuration()
                .resolve(pluginsFinder, ModuleFinder.of(), plugins);

        layer = ModuleLayer
                .boot()
                .defineModulesWithOneLoader(pluginsConfiguration, ClassLoader.getSystemClassLoader());

        launch(Navigation.class);
    }
}
