package group3.component.common.API;

public class Item {
    private String id;
    private String content;
    
    public Item(String id, String content) {
        this.id = id;
        this.content = content;
    }

    @Override
    public String toString() {
        return "[ID: " + id + ", Content: " + content + "]";
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }
}
