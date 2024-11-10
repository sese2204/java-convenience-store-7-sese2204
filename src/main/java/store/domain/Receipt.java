package store.domain;

public class Receipt {
    private static final double MEMBERSHIP_DISCOUNT_RATE = 0.3;
    private static final int MAX_MEMBERSHIP_DISCOUNT = 8000;

    private final ProductRepository productRepository;
    private final Order order;
    private final int totalQuantity;
    private final int totalAmount;
    //private final int promotionDiscount;
    //private final int membershipDiscount;
    //private final int finalAmount;

    public Receipt(ProductRepository productRepository, Order order) {
        this.productRepository = productRepository;
        this.order = order;
        this.totalQuantity = calculateTotalAmount()
        this.totalAmount = calculateTotalAmount();
        //this.promotionDiscount = calculatePromotionDiscount();
        //this.membershipDiscount = calculateMembershipDiscount();
        //this.finalAmount = totalAmount - promotionDiscount - membershipDiscount;
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
}
