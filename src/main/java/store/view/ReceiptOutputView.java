package store.view;

import store.domain.Order;
import store.domain.ProductRepository;
import store.domain.Receipt;

public class ReceiptOutputView {
    private static final String STORE_HEADER = "==============W 편의점================";
    private static final String PROMOTION_HEADER = "=============증 정===============";
    private static final String BORDER = "====================================";
    private static final String COLUMN_HEADER = "상품명 수량 금액";
    private static final String ORDER_FORMAT = "%s %d %,d";
    private static final String PROMOTION_FORMAT = "%s %d";
    private static final String TOTAL_FORMAT = "총구매액 %d %,d";
    private static final String DISCOUNT_FORMAT = "%s -%,d";
    private static final String FINAL_FORMAT = "내실돈 %,d";

    private final ProductRepository productRepository;

    public ReceiptOutputView(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void printReceipt(Order order, Receipt receipt) {
        printStoreHeader();
        printOrderSection(order);
        printPromotionSection(order);
        printSummarySection(order, receipt);
    }

    private void printStoreHeader() {
        System.out.println(BORDER);
        System.out.println(STORE_HEADER);
        System.out.println(COLUMN_HEADER);
    }

    private void printOrderSection(Order order) {
        order.getOrderItems().forEach((name, quantity) ->
                System.out.printf(ORDER_FORMAT + "%n",
                        name,
                        quantity,
                        calculateAmount(name, quantity)));
    }

    private int calculateAmount(String productName, int quantity) {
        return productRepository.findByName(productName)
                .map(product -> product.getPrice() * quantity)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다: " + productName));
    }

    private void printPromotionSection(Order order) {
        if (!order.getPromotionGiftItems().isEmpty()) {
            System.out.println(BORDER);
            System.out.println(PROMOTION_HEADER);
            order.getPromotionGiftItems()
                    .forEach((name, quantity) ->
                            System.out.printf(PROMOTION_FORMAT + "%n", name, quantity));
        }
    }

    private void printSummarySection(Order order, Receipt receipt) {
        System.out.println(BORDER);
        System.out.printf(TOTAL_FORMAT + "%n",
                calculateTotalQuantity(order),
                receipt.getTotalAmount());

        if (receipt.getPromotionDiscount() > 0) {
            System.out.printf(DISCOUNT_FORMAT + "%n", "행사할인", receipt.getPromotionDiscount());
        }
        if (receipt.getMembershipDiscount() > 0) {
            System.out.printf(DISCOUNT_FORMAT + "%n", "멤버십할인", receipt.getMembershipDiscount());
        }
        System.out.printf(FINAL_FORMAT + "%n", receipt.getFinalAmount());
    }

    private int calculateTotalQuantity(Order order) {
        return order.getOrderItems().values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
