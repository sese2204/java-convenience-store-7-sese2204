package store.domain;

import java.time.LocalDate;

public class Product {
    private String name;
    private int price;
    private int promotionQuantity;
    private int normalQuantity;
    private Promotion promotion;

    public Product(String name, int price, int promotionQuantity, int normalQuantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.promotionQuantity = promotionQuantity;
        this.normalQuantity = normalQuantity;
        this.promotion = promotion;
    }

    public void removeQuantity(int orderQuantity) {
        if (orderQuantity <= promotionQuantity) {
            promotionQuantity -= orderQuantity;
        }
        if (orderQuantity > promotionQuantity) {
            promotionQuantity = 0;
            normalQuantity -= orderQuantity - promotionQuantity;
        }
    }

    public boolean isPromotionAvailable(LocalDate date) {
        return !date.isBefore(promotion.getStartDate()) && !date.isAfter(promotion.getEndDate());
    }

    public String getName() {
        return name;
    }
    //TODO: 검증 로직
}
