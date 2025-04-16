package group3.apihandler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class App implements APIHandler {

    String baseUrl = "http://127.0.0.1:8000";

    public static void main(String[] args) throws IOException {
        App appInstance = new App();
        //System.out.println(appInstance.getWarehouseInfo());
        System.out.println(appInstance.pickWarehouseItem("3"));
        //System.out.println(appInstance.insertWarehouseItem("1337", "Stuff"));
        //System.out.println(appInstance.commandAGV("move", "assembly"));
    }

    public String commandAGV(String command, String location) throws IOException {
        URL url = new URL(baseUrl + "/agv/");

        String body = "{\"command\": " + "\"" + command + "\", \"location\":" + "\"" + location + "\"}";

        return getString(url, body);
    }

    public String pickWarehouseItem(String id) throws IOException {
        URL url = new URL(baseUrl + "/warehouse/pick");

        String body = "{\"id\":" + "\"" + id + "\"}";

        return getString(url, body);
    }

    public String insertWarehouseItem(String id, String name) throws IOException {
        URL url = new URL(baseUrl + "/warehouse/insert");

        String body = "{\"id\": " + "\"" + id + "\", \"name\":" + "\"" + name + "\"}";

        return getString(url, body);
    }

    private String getString(URL url, String body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = body.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        if (connection.getResponseCode() != 200) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                connection.disconnect();
                return response.toString();
            }
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            connection.disconnect();
            return response.toString();
        }
    }

    public Warehouse getWarehouseInfo() throws IOException {
        URL url = new URL(baseUrl + "/warehouse/getinventory");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        Warehouse warehouse = null;

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            warehouse = getWarehouseFromString(response.toString());
        }

        return warehouse;
    }

    public Warehouse getWarehouseFromString(String response) {
        List<Item> items = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

        JsonArray jsonArray = jsonObject.get("Items").getAsJsonArray();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject item = jsonElement.getAsJsonObject();
            String id = item.get("Id").getAsString();
            String content = item.get("Content").getAsString();
            items.add(new Item(id, content));
        }

        int warehouseState = jsonObject.get("State").getAsInt();
        return new Warehouse(items, warehouseState);
    }

}

