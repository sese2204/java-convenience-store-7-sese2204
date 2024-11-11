package store.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import store.domain.Order;
import store.domain.Product;
import store.domain.ProductRepository;
import store.domain.Promotion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("주문 서비스 테스트")
class OrderServiceTest {
    private ProductRepository productRepository;
    private OrderService orderService;
    private Product colaWithPromotion;
    private Product waterWithoutPromotion;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        orderService = new OrderService(productRepository);

        Promotion twoOnePmotion = new Promotion("탄산2+1", 2, 1,
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        colaWithPromotion = new Product("콜라", 1000);
        colaWithPromotion.setPromotion(twoOnePmotion);
        colaWithPromotion.setPromotionQuantity(10);
        colaWithPromotion.setNormalQuantity(10);

        waterWithoutPromotion = new Product("물", 500);
        waterWithoutPromotion.setNormalQuantity(10);

        // Repository에 상품 추가
        productRepository.add(colaWithPromotion);
        productRepository.add(waterWithoutPromotion);
    }

    @Test
    @DisplayName("주문 입력 문자열이 올바르게 파싱되어야 한다")
    void createOrder_ShouldParseOrderInputCorrectly() {
        // given
        String orderInput = "[콜라-2][물-1]";

        // when
        Order order = orderService.createOrder(orderInput, false);

        // then
        Map<String, Integer> orderItems = order.getOrderItems();
        assertThat(orderItems)
                .hasSize(2)
                .containsEntry("콜라", 2)
                .containsEntry("물", 1);
    }

    @Test
    @DisplayName("주문 수량이 재고보다 많으면 예외가 발생해야 한다")
    void createOrder_ShouldThrowException_WhenQuantityExceedsStock() {
        // given
        String orderInput = "[콜라-21]"; // 재고(20)보다 많은 수량

        // when/then
        assertThatThrownBy(() -> orderService.createOrder(orderInput, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }

    @Test
    @DisplayName("목록에 없는 입력이 들어오면 예외가 발생해야 한다")
    void createOrder_ShouldThrowException_WhenProductNotExist() {
        // given
        String orderInput = "[제로콜라-20]"; // 존재하지 않는 상품

        // when/then
        assertThatThrownBy(() -> orderService.createOrder(orderInput, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    @Test
    @DisplayName("프로모션 상품 구매 시 증정 상품이 추가되어야 한다")
    void processOrder_ShouldAddPromotionItems() {
        // given
        String orderInput = "[콜라-6]"; // 2+1 프로모션으로 2개 증정
        Order order = orderService.createOrder(orderInput, false);

        // when
        orderService.processOrder(order);

        // then
        assertThat(order.getPromotionGiftItems())
                .containsEntry("콜라", 2);
    }

    @Test
    @DisplayName("추가 구매 추천이 필요한 경우를 올바르게 판단해야 한다")
    void shouldRecommendAdditionalPurchase_ShouldReturnCorrectResult() {
        // given
        LocalDate currentDate = LocalDate.of(2024, 3, 1);

        // when/then
        assertThat(orderService.shouldRecommendAdditionalPurchase("콜라", 2, currentDate)).isTrue(); // 2개 구매 시 1개 더 사면 1개 증정
        assertThat(orderService.shouldRecommendAdditionalPurchase("콜라", 3, currentDate)).isFalse(); // 이미 2+1 완성
        assertThat(orderService.shouldRecommendAdditionalPurchase("콜라", 11, currentDate)).isFalse(); // 프로모션 적용 불가능
    }

    @Test
    @DisplayName("프로모션이 적용되지 않는 구매 수량을 올바르게 계산해야 한다")
    void getNonPromotionalPurchase_ShouldCalculateCorrectly() {
        // when/then
        assertThat(orderService.GetNonPromotionalPurchase("콜라", 11)).isEqualTo(2); // 9개는 2+1로 처리, 2개는 일반 구매
        assertThat(orderService.GetNonPromotionalPurchase("콜라", 3)).isEqualTo(0); // 모두 2+1로 처리
        assertThat(orderService.GetNonPromotionalPurchase("콜라", 2)).isEqualTo(0); // 프로모션 수량 미달
    }

    @Test
    @DisplayName("프로모션 증정 수량이 올바르게 계산되어야 한다")
    void processOrder_ShouldCalculatePromotionQuantityCorrectly() {
        // given
        String orderInput = "[콜라-9]"; // 2+1 프로모션으로 3개 증정
        Order order = orderService.createOrder(orderInput, false);

        // when
        orderService.processOrder(order);

        // then
        assertThat(order.getPromotionGiftItems())
                .containsEntry("콜라", 3);
    }

    @Test
    @DisplayName("프로모션이 없는 상품은 증정 상품이 추가되지 않아야 한다")
    void processOrder_ShouldNotAddPromotionItemsForNonPromotionalProducts() {
        // given
        String orderInput = "[물-3]";
        Order order = orderService.createOrder(orderInput, false);

        // when
        orderService.processOrder(order);

        // then
        assertThat(order.getPromotionGiftItems()).isEmpty();
    }
}
