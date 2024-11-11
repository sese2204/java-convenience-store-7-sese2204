package store.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Order {

    private Map<String, Integer> orderItems;
    private Map<String, Integer> promotionGiftItems;
    private boolean isMembershipApplied;
    private final LocalDateTime orderDate;

    public Order(Map<String, Integer> orderItems, boolean isMembershipApplied, LocalDateTime orderDate) {
        this.orderItems = new LinkedHashMap<>(orderItems);
        this.promotionGiftItems = new LinkedHashMap<>();
        this.isMembershipApplied = isMembershipApplied;
        this.orderDate = orderDate;
    }

    public Map<String, Integer> getOrderItems() {
        return orderItems;
    }

    public Map<String, Integer> getPromotionGiftItems() {
        return promotionGiftItems;
    }

    public LocalDate getOrderDate() {
        return orderDate.toLocalDate();
    }

    public boolean isMembershipApplied() {
        return isMembershipApplied;
    }

    public void applyMembership() {
        isMembershipApplied = true;
    }

    public void updateQuantity(String productName, int quantity) {
        orderItems.compute(productName, (k, currentQuantity) -> currentQuantity + quantity);
    }

    public void addPromotionGiftItem(String item, int quantity) {
        promotionGiftItems.put(item, quantity);
    }
}
