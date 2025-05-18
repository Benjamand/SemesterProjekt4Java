package group3.component.common.API;

import java.util.ArrayList;
import java.util.List;

public class AssemblyRecipe {
    private String productionName;
    private String name;
    private String description;
    private List<String> ingredients = new ArrayList<>();

    public AssemblyRecipe(String productionName, String name, String description, List<String> ingredients) {
        this.productionName = productionName;
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
    }
    @Override
    public String toString() {
        return "[Name: " + name + ", description: " + description + ", amount of ingredients: " + ingredients.size() + ", production name: " + productionName + "]";
    }
}