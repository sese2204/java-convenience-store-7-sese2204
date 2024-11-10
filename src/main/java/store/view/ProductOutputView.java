package store.view;

import java.util.List;
import store.domain.Product;

public class ProductOutputView {
    private static final String STORE_GREETING = "안녕하세요. W편의점입니다.";
    private static final String PRODUCT_LIST_HEADER = "현재 보유하고 있는 상품입니다.";
    private static final String PRODUCT_WITH_QUANTITY_FORMAT = "- %s %,d원 %d개%s";
    private static final String PRODUCT_NO_STOCK_FORMAT = "- %s %,d원 재고 없음 %s";
    private static final String PROMOTION_FORMAT = " %s";

    public void printProducts(List<Product> products) {
        printHeader();
        products.forEach(this::printProduct);
    }

    private void printHeader() {
        System.out.println(STORE_GREETING);
        System.out.println(PRODUCT_LIST_HEADER);
    }

    private void printProduct(Product product) {
        if (isPromotionalProduct(product)) {
            printPromotionalStock(product);
            printNormalStock(product);
            return;
        }
        printNormalStock(product);
    }

    private boolean isPromotionalProduct(Product product) {
        return product.getPromotion() != null;
    }

    private void printPromotionalStock(Product product) {
        String promotionText = formatPromotionText(product);
        printStock(product, product.getPromotionQuantity(), promotionText);
    }

    private void printNormalStock(Product product) {
        printStock(product, product.getNormalQuantity(), "");
    }

    private String formatPromotionText(Product product) {
        if (!isPromotionalProduct(product)) {
            return "";
        }
        return String.format(PROMOTION_FORMAT, product.getPromotion().getName());
    }

    private void printStock(Product product, int quantity, String additionalText) {
        if (hasStock(quantity)) {
            printWithStock(product, quantity, additionalText);
            return;
        }
        printWithoutStock(product, additionalText);
    }

    private boolean hasStock(int quantity) {
        return quantity > 0;
    }

    private void printWithStock(Product product, int quantity, String additionalText) {
        System.out.printf(PRODUCT_WITH_QUANTITY_FORMAT + "%n",
                product.getName(),
                product.getPrice(),
                quantity,
                additionalText);
    }

    private void printWithoutStock(Product product, String additionalText) {
        System.out.printf(PRODUCT_NO_STOCK_FORMAT + "%n",
                product.getName(),
                product.getPrice(),
                additionalText);
    }
}
