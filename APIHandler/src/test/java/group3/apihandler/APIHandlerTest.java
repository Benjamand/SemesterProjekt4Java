package group3.apihandler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class APIHandlerTest {

    @Mock
    private HttpURLConnection mockConnection;

    @Mock
    private BufferedReader mockReader;

    @InjectMocks
    private App appInstance;

    @BeforeEach
    void setUp() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWarehouseInfo() throws IOException {
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
