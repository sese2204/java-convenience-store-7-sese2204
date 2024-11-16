package store.domain;

import java.util.ArrayList;
import java.util.List;

public class PromotionRepository {
    private List<Promotion> promotions;

    public PromotionRepository() {
        this.promotions = new ArrayList<>();
    }

    public void add(Promotion promotion) {
        promotions.add(promotion);
    }

    public Promotion findByName(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
