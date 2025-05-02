package group3.StorageGUI;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Item {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty content;

    public Item(int id, String content) {
        this.id = new SimpleIntegerProperty(id);
        this.content = new SimpleStringProperty(content);
    }

    public int getId() {
        return id.get();
    }

    public String getContent() {
        return content.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty contentProperty() {
        return content;
    }
}
