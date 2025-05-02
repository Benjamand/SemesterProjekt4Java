package group3.component.common.API;

import java.util.List;

public class Warehouse {
    List<Item> items;
    int state;

    public Warehouse(List<Item> items, int state) {
        this.items = items;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Items: " + items.size() + ", State: " + state;
    }

    public List<Item> getItems() {
        return items;
    }

    public int getState() {
        return state;
    }
}
