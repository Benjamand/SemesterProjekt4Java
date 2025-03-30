package group3.apihandler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class App implements APIHandler {

    String baseUrl = "http://127.0.0.1:8000";

    public static void main(String[] args) throws IOException {
        App appInstance = new App();
        appInstance.getWarehouseInfo();
    }

    public Warehouse getWarehouseInfo() throws IOException {
        URL url = new URL(baseUrl +"/warehouse/getinventory");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        List<Item> items = new ArrayList<>();
        int warehouseState = -1;

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);

            JsonArray jsonArray = jsonObject.get("Items").getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
                JsonObject item = jsonElement.getAsJsonObject();
                String id = item.get("Id").getAsString();
                String content = item.get("Content").getAsString();
                items.add(new Item(id, content));
            }

            warehouseState = jsonObject.get("State").getAsInt();
        }

        Warehouse warehouse = new Warehouse(items, warehouseState);
        return warehouse;
    }

}

