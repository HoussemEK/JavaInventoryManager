package model;

public class Item {
    private int id;
    private String name;
    private int quantity;
    private double cost;

    
    public Item(int id, String name, int quantity, double cost) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Item [id=" + id + ", name=" + name + ", quantity=" + quantity + ", cost=" + cost + "]";
    }
}
