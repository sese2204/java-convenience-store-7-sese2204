package store.domain;

public class Product {
    private String name;
    private int price;
    private int promotionQuantity;
    private int normalQuantity;


    public Product(String name, int price, int promotionQuantity, int normalQuantity) {
        this.name = name;
        this.price = price;
        this.promotionQuantity = promotionQuantity;
        this.normalQuantity = normalQuantity;
    }

    public void removeStock(int orderQuantity) {
        if (orderQuantity <= promotionQuantity) {
            promotionQuantity -= orderQuantity;
        }
        if (orderQuantity > promotionQuantity) {
            promotionQuantity = 0;
            normalQuantity -= orderQuantity - promotionQuantity;
        }
    }
    //TODO: 검증 로직
}
