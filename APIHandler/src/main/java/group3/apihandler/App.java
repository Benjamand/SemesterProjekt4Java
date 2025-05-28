package group3.apihandler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import group3.component.common.API.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class App implements IWarehouseAPIProcessingService, IInstructionAPIProcessingService {

    String baseUrl = "http://127.0.0.1:8000";

    public static void main(String[] args) throws IOException {
        App appInstance = new App();
        //System.out.println(appInstance.getWarehouseInfo());
        //System.out.println(appInstance.pickWarehouseItem("3"));
        //System.out.println(appInstance.insertWarehouseItem("1337", "Stuff"));
        //System.out.println(appInstance.commandAGV("move", "assembly"));
        //System.out.println(appInstance.getAssemblyRecipes());
        //System.out.println(appInstance.getAssemblyInventory());
        //System.out.println(appInstance.commandAssembly("2", "simple_drone"));
        System.out.println(appInstance.getWarehouseInfo());
        //System.out.println(appInstance.pickWarehouseItem("3"));
        //System.out.println(appInstance.commandAGV("move", "warehouse"));
        //System.out.println(appInstance.commandAGV("pick", "warehouse", "3"));
        //System.out.println(appInstance.commandAGV("move", "assembly"));
        //System.out.println(appInstance.commandAGV("put", "assembly", "3"));
        //System.out.println(appInstance.getAssemblyInventory());
    }

    public String commandAGV(String command, String location) throws IOException {
        return commandAGV(command, location, null);
    }

    public String commandAGV(String command, String location, String id) throws IOException {
        URL url = new URL(baseUrl + "/agv/");

        String body = "";
        if (id != null) {
            body = "{\"command\": \"" + command + "\", \"location\": \"" + location + "\", \"id\": \"" + id + "\"}";
        } else {
            body = "{\"command\": \"" + command + "\", \"location\": \"" + location + "\"}";
        }


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
        URL url = new URL(baseUrl + "/warehouse/inventory");
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

            System.out.println(response.toString());
            warehouse = getWarehouseFromString(response.toString());
        }

        return warehouse;
    }

    public List<AssemblyRecipe> getAssemblyRecipes() throws IOException {
        URL url = new URL(baseUrl + "/assembly/recipes");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        List<AssemblyRecipe> assemblyRecipes = new ArrayList<>();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            assemblyRecipes =  getAssemblyRecipesFromString(response.toString());

        }

        return assemblyRecipes;
    }

    public String commandAssembly(String processId, String droneName) throws IOException {
        URL url = new URL(baseUrl + "/assembly/");

        String body = "{\"ProcessID\": " + "\"" + processId + "\", \"Drone\":" + "\"" + droneName + "\"}";

        return getString(url, body);
    }

    public List<String> getAssemblyInventory() throws IOException {
        URL url = new URL(baseUrl + "/assembly/inventory");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        int responseCode = con.getResponseCode();

        List<String> inventory = new ArrayList<>();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            inventory =  getAssemblyInventoryFromString(response.toString());

        }


        return inventory;
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

    public List<AssemblyRecipe> getAssemblyRecipesFromString(String response) {
        List<AssemblyRecipe> assemblyRecipes = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            JsonObject recipe = entry.getValue().getAsJsonObject();
            String productionName = entry.getKey();
            String name = recipe.get("name").getAsString();
            String description = recipe.get("description").getAsString();
            List<String> ingredients = new ArrayList<>();
            for (JsonElement ingredient: recipe.getAsJsonArray("ingredients")) {
                ingredients.add(ingredient.getAsString());
            }

            assemblyRecipes.add(new AssemblyRecipe(productionName ,name, description, ingredients));
        }
        return assemblyRecipes;
    }

    public List<String> getAssemblyInventoryFromString(String response) {
        List<String> inventory = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String ingredient = entry.getValue().getAsString();
            inventory.add(ingredient);
        }
        return inventory;
    }
}

