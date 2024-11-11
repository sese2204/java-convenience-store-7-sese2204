package store.controller;

import camp.nextstep.edu.missionutils.DateTimes;
import store.domain.Order;
import store.domain.ProductRepository;
import store.service.OrderService;
import store.view.InputView;
import store.view.ProductOutputView;
import store.view.ReceiptOutputView;

public class OrderController {
    private ProductRepository productRepository;
    private OrderService orderService;
    private InputView inputView;
    private ProductOutputView productOutputView;
    private ReceiptOutputView receiptOutputView;

    public OrderController(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.orderService = new OrderService(productRepository);
        this.inputView = new InputView();
        this.productOutputView = new ProductOutputView();
        this.receiptOutputView = new ReceiptOutputView();
    }

    public void run(){
        productOutputView.printProducts(productRepository.getProducts());
        Order order = getOrderWithRetry();
    }

    private Order getOrderWithRetry(){
        while(true){
            try{
                String input = inputView.requestOrderInput();
                return orderService.createOrder(input, false);
            }catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
