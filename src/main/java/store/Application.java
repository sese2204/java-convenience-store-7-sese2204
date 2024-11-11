package store;


import static store.util.PromotionLoader.loadPromotions;

import store.controller.OrderController;
import store.controller.RepositoryController;
import store.domain.PromotionRepository;

public class Application {
    public static void main(String[] args) {
        RepositoryController repositoryController = new RepositoryController();
        repositoryController.set();
        OrderController orderController = new OrderController(repositoryController.getProductRepository());
        orderController.run();
    }
}
