package group3.component.common.API;

import java.io.IOException;

public interface IWarehouseAPIProcessingService {
    public Warehouse getWarehouseInfo() throws IOException;

    public String commandAGV(String command, String location) throws IOException;

    public String pickWarehouseItem(String id) throws IOException;

    public String insertWarehouseItem(String id, String name) throws IOException;

    public Warehouse getWarehouseFromString(String response);
}
