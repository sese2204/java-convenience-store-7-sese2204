package store.controller;

import static store.util.PromotionLoader.loadPromotions;
import static store.util.ProductLoader.loadProducts;

import store.domain.ProductRepository;
import store.domain.PromotionRepository;

public class RepositoryController {
    private PromotionRepository promotionRepository;
    private ProductRepository productRepository;

    public RepositoryController() {
        this.promotionRepository = new PromotionRepository();
        this.productRepository = new ProductRepository();
    }

    public void set(){
        setPromotionRepository();
        setProductRepository();
    }

    public PromotionRepository getPromotionRepository() {
        return promotionRepository;
    }

    public ProductRepository getProductRepository() {
        return productRepository;
    }

    private void setPromotionRepository() {
        loadPromotions("src/main/resources/promotions.md", promotionRepository);
    }

    private void setProductRepository(){
        loadProducts("src/main/resources/products.md", productRepository, promotionRepository);
    }
}
