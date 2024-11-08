package store.domain;

public class Product {
    private final String name;
    private final int price;
    private final int promotionQuantity;
    private final int normalQuantity;

    public Product(String name, int price, int promotionQuantity, int normalQuantity) {
        this.name = name;
        this.price = price;
        this.promotionQuantity = promotionQuantity;
        this.normalQuantity = normalQuantity;
    }
}
