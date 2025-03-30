package group3.apihandler;

import java.util.List;
import java.util.Map;

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
