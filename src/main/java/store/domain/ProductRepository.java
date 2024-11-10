package store.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductRepository {
    private List<Product> products;

    public ProductRepository() {
        this.products = new ArrayList<>();
    }

    public void add(Product product) {
        products.add(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void processOrderItems(Map<String, Integer> orderItems) {
        for (Map.Entry<String, Integer> orderItem : orderItems.entrySet()){
            removeQuantityFromProduct(orderItem.getKey(), orderItem.getValue());
        }
    }

    private void removeQuantityFromProduct(String productName, Integer quantity) {
        Product product = findProductByName(productName);
        product.removeQuantity(quantity);
    }

    private Product findProductByName(String productName) {
        return findByName(productName)
                .orElseThrow(() -> new IllegalArgumentException(
                        "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요."));
    }

    public Optional<Product> findByName(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst();
    }
}
