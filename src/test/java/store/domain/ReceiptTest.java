package store.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("영수증 테스트")
class ReceiptTest {
    private ProductRepository productRepository;
    private Product colaWithPromotion;
    private Product waterWithoutPromotion;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();

        Promotion twoOnePmotion = new Promotion("탄산2+1", 2, 1,
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        colaWithPromotion = new Product("콜라", 1000);
        colaWithPromotion.setPromotion(twoOnePmotion);
        colaWithPromotion.setPromotionQuantity(10);
        colaWithPromotion.setNormalQuantity(10);

        waterWithoutPromotion = new Product("물", 500);
        waterWithoutPromotion.setNormalQuantity(10);

        productRepository.add(colaWithPromotion);
        productRepository.add(waterWithoutPromotion);
    }

    @Test
    @DisplayName("2+1 프로모션으로 1개를 증정받은 경우 3개는 멤버십 할인에서 제외되어야 한다")
    void membershipDiscount_ShouldExcludePromotionAppliedQuantity() {
        // given
        Map<String, Integer> orderItems = new HashMap<>();
        orderItems.put("콜라", 4);  // 4000원 (3개는 2+1로 묶이고, 1개만 멤버십 적용)

        Order order = new Order(orderItems,true, DateTimes.now());
        order.addPromotionGiftItem("콜라", 1);

        // when
        Receipt receipt = new Receipt(productRepository, order);

        // then
        assertThat(receipt.getTotalAmount()).isEqualTo(4000);  // 1000원 * 4개
        assertThat(receipt.getPromotionDiscount()).isEqualTo(1000);  // 증정품 1개
        assertThat(receipt.getMembershipDiscount()).isEqualTo(300);  // 1개만 * 1000원 * 0.3 = 300원
        assertThat(receipt.getFinalAmount()).isEqualTo(2700);  // 4000 - 1000 - 300
    }

    @Test
    @DisplayName("여러 개의 프로모션이 적용된 경우 각각의 프로모션 단위만큼 멤버십 할인에서 제외되어야 한다")
    void membershipDiscount_ShouldExcludeMultiplePromotionUnits() {
        // given
        Map<String, Integer> orderItems = new HashMap<>();
        orderItems.put("콜라", 7);  // 7000원 (6개는 2+1*2로 묶이고, 1개만 멤버십 적용)

        Order order = new Order(orderItems,true, DateTimes.now());

        order.addPromotionGiftItem("콜라", 2); // 2+1 프로모션으로 2개 증정

        // when
        Receipt receipt = new Receipt(productRepository, order);

        // then
        assertThat(receipt.getTotalAmount()).isEqualTo(7000);  // 1000원 * 7개
        assertThat(receipt.getPromotionDiscount()).isEqualTo(2000);  // 증정품 2개
        assertThat(receipt.getMembershipDiscount()).isEqualTo(300);  // 1개만 * 1000원 * 0.3 = 300원
        assertThat(receipt.getFinalAmount()).isEqualTo(4700);  // 7000 - 2000 - 300
    }

    @Test
    @DisplayName("프로모션과 멤버십이 모두 적용된 여러 상품을 구매할 때도 정확히 계산되어야 한다")
    void calculate_WithMultipleProductsAndPromotions() {
        // given
        Map<String, Integer> orderItems = new HashMap<>();
        orderItems.put("콜라", 7);  // 7000원 (6개는 2+1*2로 묶이고, 1개만 멤버십 적용)
        orderItems.put("물", 2);    // 1000원 (멤버십 적용)

        Order order = new Order(orderItems,true, DateTimes.now());

        order.addPromotionGiftItem("콜라", 2); // 2+1 프로모션으로 2개 증정

        // when
        Receipt receipt = new Receipt(productRepository, order);

        // then
        assertThat(receipt.getTotalAmount()).isEqualTo(8000);  // 7000 + 1000
        assertThat(receipt.getPromotionDiscount()).isEqualTo(2000);  // 증정품 2개
        assertThat(receipt.getMembershipDiscount()).isEqualTo(600);  // (콜라 1개 * 1000 + 물 2개 * 500) * 0.3
        assertThat(receipt.getFinalAmount()).isEqualTo(5400);  // 8000 - 2000 - 600
    }
}
