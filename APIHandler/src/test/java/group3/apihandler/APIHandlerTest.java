package group3.apihandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

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
}
