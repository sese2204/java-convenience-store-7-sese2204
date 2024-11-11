package store.service;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.domain.Order;
import store.domain.Product;
import store.domain.ProductRepository;

public class OrderService {
    private static final Pattern ORDER_PATTERN = Pattern.compile("\\[(.*?)-(\\d+)\\]");
    private ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void processOrder(Order order) {
        addPromotionItems(order);
        productRepository.processOrderItems(order.getOrderItems());
    }

    public Order createOrder(String orderInput, boolean isMembershipApplied){
        Map<String, Integer> orderItems = parseOrderInput(orderInput);
        validItemsExist(orderItems);
        validateQuantityAvailable(orderItems);
        return new Order(orderItems, isMembershipApplied, DateTimes.now());
    }

    public boolean shouldRecommendAdditionalPurchase(String productName, int quantity, LocalDate localDate) {
        Product product = productRepository.findByName(productName).get();
        if (!product.isPromotionAvailable(localDate) || quantity >= product.getPromotionQuantity()) {
            return false;
        }

        int promotionUnit = product.getPromotion().getBuyCount() + 1;
        int remainder = quantity % promotionUnit;
        return remainder == product.getPromotion().getBuyCount();
    }

    public int getNonPromotionalPurchase(String productName, int quantity) {
        Product product = productRepository.findByName(productName).get();

        if (quantity > product.getPromotionQuantity()){
            int giftQuantity = product.getPromotionQuantity() / (product.getPromotion().getBuyCount() + 1);
            return quantity - giftQuantity * (product.getPromotion().getBuyCount() + 1);
        }
        return 0;
    }

    private void addPromotionItems(Order order) {
        for (Map.Entry<String, Integer> entry : order.getOrderItems().entrySet()) {
            addPromotionItem(order, entry.getKey(), entry.getValue());
        }
    }

    private void addPromotionItem(Order order, String productName, int quantity) {
        Product product = productRepository.findByName(productName).get();
        if(product.isPromotionAvailable(order.getOrderDate())){
            int promotionQuantity = calculatePromotionQuantity(product, quantity);
            order.addPromotionGiftItem(productName, promotionQuantity);
        }
    }

    private int calculatePromotionQuantity(Product product, int quantity) {
        int promotionQuantity = product.getPromotionQuantity();

        if(quantity >= promotionQuantity){
            return promotionQuantity / (product.getPromotion().getBuyCount() + 1);
        }

        return quantity / (product.getPromotion().getBuyCount() + 1);
    }

    private Map<String, Integer> parseOrderInput(String input) {
        Map<String, Integer> orderItems = new LinkedHashMap<>();
        Matcher matcher = ORDER_PATTERN.matcher(input);

        while (matcher.find()) {
            String productName = matcher.group(1);
            int quantity = Integer.parseInt(matcher.group(2));
            orderItems.put(productName, quantity);
        }

        return orderItems;
    }

    private void validItemsExist(Map<String, Integer> orderItems) {
        for (Map.Entry<String, Integer> entry : orderItems.entrySet()) {
            Product product = productRepository.findByName(entry.getKey())
                    .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."));
        }
    }

    private void validateQuantityAvailable(Map<String, Integer> orderItems) {
        for (Map.Entry<String, Integer> entry : orderItems.entrySet()) {
            Product product = productRepository.findByName(entry.getKey()).get();
            if (!product.isQuantityAvailable(entry.getValue())) {
                throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
            }
        }
    }
}
