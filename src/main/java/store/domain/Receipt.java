package store.domain;

import java.util.Map;
import java.util.Map.Entry;

public class Receipt {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    private final ProductRepository productRepository;
    private final Order order;
    private final int totalQuantity;
    private final int totalAmount;
    private final int promotionDiscount;
    private final int membershipDiscount;
    private final int finalAmount;

    public Receipt(ProductRepository productRepository, Order order) {
        this.productRepository = productRepository;
        this.order = order;
        this.totalQuantity = calculateTotalQuantity();
        this.totalAmount = calculateTotalAmount();
        this.promotionDiscount = calculatePromotionDiscount();
        this.membershipDiscount = calculateMembershipDiscount();
        this.finalAmount = totalAmount - promotionDiscount - membershipDiscount;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalAmount() {
        return finalAmount;
    }

    private int calculateTotalQuantity() {
        return order.getOrderItems().entrySet().stream()
                .mapToInt(entry -> {
                    return entry.getValue();
                })
                .sum();
    }

    private int calculateTotalAmount() {
        return order.getOrderItems().entrySet().stream()
                .mapToInt(entry -> {
                    String productName = entry.getKey();
                    int quantity = entry.getValue();
                    int price = productRepository.findByName(productName).get().getPrice();
                    return price * quantity;
                })
                .sum();
    }

    private int calculatePromotionDiscount() {
        return order.getPromotionGiftItems().entrySet().stream()
                .mapToInt(entry -> {
                    String productName = entry.getKey();
                    int giftQuantity = entry.getValue();
                    int price = productRepository.findByName(productName).get().getPrice();
                    return price * giftQuantity;
                })
                .sum();
    }

    private int calculateMembershipDiscount() {
        if (!order.isMembershipApplied()) {
            return 0;
        }

        int totalDiscountAmount = calculateTotalMembershipDiscountAmount();
        return Math.min(totalDiscountAmount, MAX_MEMBERSHIP_DISCOUNT);
    }

    private int calculateTotalMembershipDiscountAmount() {
        return order.getOrderItems().entrySet().stream()
                .mapToInt(this::calculateMembershipDiscountForProduct)
                .sum();
    }

    private int calculateMembershipDiscountForProduct(Map.Entry<String, Integer> entry) {
        String productName = entry.getKey();
        int orderQuantity = entry.getValue();
        Product product = productRepository.findByName(productName).get();

        int membershipQuantity = calculateMembershipQuantity(productName, orderQuantity);
        return calculateDiscountAmount(membershipQuantity, product.getPrice());
    }

    private int calculateMembershipQuantity(String productName, int orderQuantity) {
        int promotionAppliedQuantity = calculatePromotionAppliedQuantity(productName);
        return orderQuantity - promotionAppliedQuantity;
    }

    private int calculatePromotionAppliedQuantity(String productName) {
        Product product = productRepository.findByName(productName).get();
        int giftQuantity = order.getPromotionGiftItems().getOrDefault(productName, 0);

        if (product.getPromotion() != null && giftQuantity > 0) {
            int promotionUnit = product.getPromotion().getBuyCount() + 1;
            return giftQuantity * promotionUnit;
        }
        return 0;
    }

    private int calculateDiscountAmount(int quantity, int price) {
        return (int) (price * Math.max(0, quantity) * MEMBERSHIP_DISCOUNT_RATE);
    }
}
