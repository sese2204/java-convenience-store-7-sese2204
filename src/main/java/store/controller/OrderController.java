package store.controller;

import java.util.Map;
import store.domain.Order;
import store.domain.ProductRepository;
import store.domain.Receipt;
import store.service.OrderService;
import store.view.YesNoInputView;
import store.view.OrderInputView;
import store.view.ProductOutputView;
import store.view.ReceiptOutputView;

public class OrderController {
    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final YesNoInputView yesNoInputView;
    private final OrderInputView orderInputView;
    private final ProductOutputView productOutputView;
    private final ReceiptOutputView receiptOutputView;

    public OrderController(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.orderService = new OrderService(productRepository);
        this.yesNoInputView = new YesNoInputView();
        this.orderInputView = new OrderInputView();
        this.productOutputView = new ProductOutputView();
        this.receiptOutputView = new ReceiptOutputView(productRepository);
    }

    public void run(){
        do {
            productOutputView.printProducts(productRepository.getProducts());
            Order order = getOrderWithRetry();
            validateAdditionalPurchase(order);
            validateNonPromotionalPurchase(order);
            handleMembership(order);
            orderService.processOrder(order);
            Receipt receipt = new Receipt(productRepository, order);
            receiptOutputView.printReceipt(order, receipt);
        }while (yesNoInputView.readContinueShopping());
    }

    private void validateNonPromotionalPurchase(Order order) {
        for (Map.Entry<String, Integer> entry : order.getOrderItems().entrySet()){
            String productName = entry.getKey();
            int quantity = entry.getValue();
            int nonPromotionalPurchase = orderService.getNonPromotionalPurchase(productName, quantity, order.getOrderDate());
            handleNonPromotionalPurchase(order, productName, nonPromotionalPurchase);
        }
    }

    private void handleNonPromotionalPurchase(Order order, String productName, int nonPromotionalPurchase) {
        if (nonPromotionalPurchase > 0){
            if(!yesNoInputView.noticeNonPromotionalPurchase(productName, nonPromotionalPurchase)){
                order.updateQuantity(productName, -nonPromotionalPurchase);
            }
        }
    }

    private void handleMembership(Order order) {
        if(yesNoInputView.readMembershipApply()){
            order.applyMembership();
        }
    }

    private void validateAdditionalPurchase(Order order) {
        for (Map.Entry<String, Integer> entry : order.getOrderItems().entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();

            if (orderService.shouldRecommendAdditionalPurchase(productName, quantity, order.getOrderDate())) {
                tryAdditionalPurchase(order, productName);
            }
        }
    }

    private void tryAdditionalPurchase(Order order, String productName) {
        if (yesNoInputView.recommendAdditionalPurchase(productName)) {
            order.updateQuantity(productName, 1);
        }
    }

    private Order getOrderWithRetry(){
        while(true){
            try{
                String input = orderInputView.requestOrderInput();
                return orderService.createOrder(input, false);
            }catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
