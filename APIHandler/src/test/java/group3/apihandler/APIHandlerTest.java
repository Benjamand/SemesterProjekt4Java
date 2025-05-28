package group3.apihandler;

import group3.component.common.API.Warehouse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class APIHandlerTest {

    @InjectMocks
    private App appInstance;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWarehouseInfo() {
        String jsonResponse = "{\"Items\":[{\"Id\":1,\"Content\":\"Item 1\"},{\"Id\":2,\"Content\":\"Item 2\"}],\"State\":0}";

        Warehouse warehouse = appInstance.getWarehouseFromString(jsonResponse);

        assertNotNull(warehouse);
        assertNotNull(warehouse.getItems());
        assertEquals(2, warehouse.getItems().size());
        assertEquals("Item 1", warehouse.getItems().get(0).getContent());
        assertEquals("Item 2", warehouse.getItems().get(1).getContent());
        assertEquals(0, warehouse.getState());
    }

    @Test
    void testCommandAGV() throws IOException {
        String command = "move";
        String location = "assembly";

        String jsonResponse = appInstance.commandAGV(command, location);
        String expectedResponse = "{\"status\":true,\"message\":\"AGV now moving to Assembly\"}";
        assertNotNull(jsonResponse);
        assertEquals(expectedResponse, jsonResponse);
    }

    @Test
    void testPickWarehouseItem() throws IOException {
        String itemId = "3";

        String jsonResponse = appInstance.pickWarehouseItem(itemId);
        String expectedResponse = "{\"response\":\"Received pick operation.\",\"item\":\"Item 3\",\"item_id\":3}";
        assertNotNull(jsonResponse);
        assertEquals(expectedResponse, jsonResponse);
    }

    @Test
    void testInsertWarehouseItem() throws IOException {
        String itemId = "1337";
        String itemName = "9mm Bolt";

        String jsonResponse = appInstance.insertWarehouseItem(itemId, itemName);
        String expectedResponse = "{\"response\":\"Received insert operation.\",\"item\":\"9mm Bolt\",\"item_id\":1337}";
        assertNotNull(jsonResponse);
        assertEquals(expectedResponse, jsonResponse);
    }
}
