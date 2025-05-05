package group3.component.common.API;

import java.io.IOException;

public interface IInstructionAPIProcessingService {
     String commandAGV(String command, String location) throws IOException;
     String pickWarehouseItem(String id) throws IOException;
     String insertWarehouseItem(String id, String name) throws IOException;


}
