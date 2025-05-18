package group3.component.common.API;

import java.io.IOException;
import java.util.List;

public interface IWarehouseAPIProcessingService {
    public Warehouse getWarehouseInfo() throws IOException;

    public String commandAGV(String command, String location) throws IOException;

    public String pickWarehouseItem(String id) throws IOException;

    public String insertWarehouseItem(String id, String name) throws IOException;

    public Warehouse getWarehouseFromString(String response);

    public List<AssemblyRecipe> getAssemblyRecipes() throws IOException;

    public String commandAssembly(String processID, String droneName) throws IOException;

    public List<String> getAssemblyInventory() throws IOException;
}