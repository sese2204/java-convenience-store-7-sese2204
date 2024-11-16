package store;

import store.controller.OrderController;
import store.controller.RepositoryController;

public class Application {
    public static void main(String[] args) {
        RepositoryController repositoryController = new RepositoryController();
        repositoryController.set();
        OrderController orderController = new OrderController(repositoryController.getProductRepository());
        orderController.run();
    }
}
