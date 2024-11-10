package store.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("상품 프로모션 테스트")
class ProductTest {
    private Product product;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        promotion = new Promotion("탄산2+1", 2, 1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));
        product = new Product("콜라", 1000);
        product.setPromotion(promotion);
    }

    @Test
    @DisplayName("프로모션 기간 내의 날짜는 true를 반환해야 한다")
    void isPromotionAvailable_ShouldReturnTrue_WhenDateIsWithinPromotionPeriod() {
        // given
        LocalDate dateInPromotionPeriod = DateTimes.now().toLocalDate();

        // when
        boolean isAvailable = product.isPromotionAvailable(dateInPromotionPeriod);

        // then
        assertThat(isAvailable).isTrue();
    }

    @Test
    @DisplayName("프로모션 시작일에는 true를 반환해야 한다")
    void isPromotionAvailable_ShouldReturnTrue_OnStartDate() {
        // given
        LocalDate startDate = LocalDate.of(2024, 1, 1);

        // when
        boolean isAvailable = product.isPromotionAvailable(startDate);

        // then
        assertThat(isAvailable).isTrue();
    }

    @Test
    @DisplayName("프로모션 종료일에는 true를 반환해야 한다")
    void isPromotionAvailable_ShouldReturnTrue_OnEndDate() {
        // given
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        // when
        boolean isAvailable = product.isPromotionAvailable(endDate);

        // then
        assertThat(isAvailable).isTrue();
    }

    @Test
    @DisplayName("프로모션 시작일 이전 날짜는 false를 반환해야 한다")
    void isPromotionAvailable_ShouldReturnFalse_BeforeStartDate() {
        // given
        LocalDate dateBeforePromotion = LocalDate.of(2023, 12, 31);

        // when
        boolean isAvailable = product.isPromotionAvailable(dateBeforePromotion);

        // then
        assertThat(isAvailable).isFalse();
    }

    @Test
    @DisplayName("프로모션 종료일 이후 날짜는 false를 반환해야 한다")
    void isPromotionAvailable_ShouldReturnFalse_AfterEndDate() {
        // given
        LocalDate dateAfterPromotion = LocalDate.of(2025, 1, 1);

        // when
        boolean isAvailable = product.isPromotionAvailable(dateAfterPromotion);

        // then
        assertThat(isAvailable).isFalse();
    }

    @Test
    @DisplayName("프로모션이 설정되지 않은 상품은 NullPointerException이 발생해야 한다")
    void isPromotionAvailable_ShouldThrowException_WhenPromotionIsNull() {
        // given
        Product productWithoutPromotion = new Product("물", 500);
        LocalDate date = LocalDate.now();

        // when/then
        assertThatThrownBy(() -> productWithoutPromotion.isPromotionAvailable(date))
                .isInstanceOf(NullPointerException.class);
    }
}
