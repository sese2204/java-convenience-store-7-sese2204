package store.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import store.domain.Product;
import store.domain.ProductRepository;
import store.domain.Promotion;
import store.domain.PromotionRepository;

public class ProductLoader {
    public static void loadProducts(String filePath, ProductRepository productRepository,
                                    PromotionRepository promotionRepository) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            processFile(br, productRepository, promotionRepository);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFile(BufferedReader br, ProductRepository productRepository,
                                    PromotionRepository promotionRepository) throws IOException {
        br.readLine(); // Skip header
        String line;
        while ((line = br.readLine()) != null) {
            processLine(line, productRepository, promotionRepository);
        }
    }

    private static void processLine(String line, ProductRepository productRepository,
                                    PromotionRepository promotionRepository) {
        String[] values = line.split(",");
        if (isValidLine(values)) {
            String name = values[0].trim();
            int price = parseInteger(values[1]);
            int quantity = parseInteger(values[2]);
            String promotionName = values[3].trim();
            Promotion promotion = "null".equals(promotionName) ? null : promotionRepository.findByName(promotionName);

            Optional<Product> existingProduct = productRepository.findByName(name);
            if (existingProduct.isEmpty()) {
                createNewProduct(productRepository, name, price, quantity, promotion);
            } else {
                updateExistingProduct(existingProduct.orElse(null), quantity, promotion);
            }
        }
    }

    private static void createNewProduct(ProductRepository productRepository,
                                         String name, int price, int quantity, Promotion promotion) {
        Product product = new Product(name, price);
        if (promotion != null) {
            product.setPromotionQuantity(quantity);
            product.setPromotion(promotion);
        } else {
            product.setNormalQuantity(quantity);
        }
        productRepository.add(product);
    }

    private static void updateExistingProduct(Product product, int quantity, Promotion promotion) {
        if (promotion != null) {
            product.setPromotionQuantity(quantity);
            product.setPromotion(promotion);
        } else {
            product.setNormalQuantity(quantity);
        }
    }

    private static boolean isValidLine(String[] values) {
        return values.length == 4;
    }

    private static int parseInteger(String value) {
        return Integer.parseInt(value.trim());
    }
}