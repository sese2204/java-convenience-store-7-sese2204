package store.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class Order {
    private static final int MEMBERSHIP_DISCOUNT_RATE = 30;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    private Map<String, Integer> orderItems;
    private boolean isMembershipApplied;

    public Order(Map<String, Integer> orderItems, boolean isMembershipApplied) {
        this.orderItems = new LinkedHashMap<>(orderItems);
        this.isMembershipApplied = isMembershipApplied;
    }

    public Order updateQuantity(String item, int quantity) {
        orderItems.replace(item, quantity);
        return this;
    }
}
