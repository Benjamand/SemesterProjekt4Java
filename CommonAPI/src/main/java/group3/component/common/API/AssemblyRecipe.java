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

    public String getProductionName() {
        return productionName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public int getIngredientCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    @Override
    public String toString() {
        return "[Name: " + name +
                ", description: " + description +
                ", amount of ingredients: " + getIngredientCount() +
                ", production name: " + productionName + "]";
    }
}
