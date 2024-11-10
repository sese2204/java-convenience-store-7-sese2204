package store.domain;

import java.util.List;

public class PromotionRespository {
    private List<Promotion> promotions;

    public PromotionRespository(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public Promotion findByName(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
