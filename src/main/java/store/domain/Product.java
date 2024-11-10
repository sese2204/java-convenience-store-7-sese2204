package store.domain;

import java.time.LocalDate;

public class Product {
    private String name;
    private int price;
    private int promotionQuantity;
    private int normalQuantity;
    private Promotion promotion;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
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

    public boolean isQuantityAvailable(int orderQuantity) {
        return orderQuantity <= promotionQuantity + normalQuantity;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getNormalQuantity(){
        return normalQuantity;
    }

    public int getPromotionQuantity(){
        return promotionQuantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

    public void setPromotionQuantity(int promotionQuantity) {
        this.promotionQuantity = promotionQuantity;
    }

    public void setNormalQuantity(int normalQuantity) {
        this.normalQuantity = normalQuantity;
    }
    //TODO: 검증 로직
}
