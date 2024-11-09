package store.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class Order {
    private static final int MEMBERSHIP_DISCOUNT_RATE = 30;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    private Map<String, Integer> orderItems;
    private Map<String, Integer> pormotionGiftItems;
    private boolean isMembershipApplied;
    private final LocalDateTime orderDate;

    public Order(Map<String, Integer> orderItems, boolean isMembershipApplied, LocalDateTime orderDate) {
        this.orderItems = new LinkedHashMap<>(orderItems);
        this.isMembershipApplied = isMembershipApplied;
        this.orderDate = orderDate;
    }

    public Map<String, Integer> getOrderItems() {
        return orderItems;
    }

    public Map<String, Integer> getPormotionGiftItems() {
        return pormotionGiftItems;
    }

    public LocalDate getOrderDate() {
        return orderDate.toLocalDate();
    }

    public boolean isMembershipApplied() {
        return isMembershipApplied;
    }

    public Order updateQuantity(String item, int quantity) {
        orderItems.replace(item, quantity);
        return this;
    }

    public void addPromotionGiftItem(String item, int quantity) {
        pormotionGiftItems.put(item, quantity);
    }
}
