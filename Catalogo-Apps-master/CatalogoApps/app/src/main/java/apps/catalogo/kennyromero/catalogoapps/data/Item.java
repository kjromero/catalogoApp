package apps.catalogo.kennyromero.catalogoapps.data;



public class Item {

    private String category;

    public Item() {
        super();
    }

    public Item(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
