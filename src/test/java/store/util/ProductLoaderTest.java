package store.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import store.domain.Product;
import store.domain.ProductRepository;
import store.domain.Promotion;
import store.domain.PromotionRepository;

@DisplayName("상품 로더 테스트")
class ProductLoaderTest {
    private ProductRepository productRepository;
    private PromotionRepository promotionRepository;
    private String testCsvContent;

    @BeforeEach
    void setUp() {
        productRepository = new ProductRepository();
        promotionRepository = new PromotionRepository();

        // 프로모션 데이터 초기화
        promotionRepository.add(new Promotion("탄산2+1", 2, 1,
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        promotionRepository.add(new Promotion("MD추천상품", 1, 1,
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31)));
        promotionRepository.add(new Promotion("반짝할인", 1, 1,
                LocalDate.of(2024, 11, 1), LocalDate.of(2024, 11, 30)));

        testCsvContent = """
            name,price,quantity,promotion
            콜라,1000,10,탄산2+1
            콜라,1000,10,null
            사이다,1000,8,탄산2+1
            사이다,1000,7,null
            오렌지주스,1800,9,MD추천상품
            탄산수,1200,5,탄산2+1
            물,500,10,null
            비타민워터,1500,6,null
            감자칩,1500,5,반짝할인
            감자칩,1500,5,null
            초코바,1200,5,MD추천상품
            초코바,1200,5,null
            에너지바,2000,5,null
            정식도시락,6400,8,null
            컵라면,1700,1,MD추천상품
            컵라면,1700,10,null""";
    }

    @Test
    @DisplayName("같은 이름의 상품은 하나의 객체만 생성되고 수량이 올바르게 설정되어야 한다")
    void loadProducts_ShouldCreateSingleProductWithCorrectQuantities(@TempDir Path tempDir) throws IOException {
        // given
        File testFile = createTestFile(tempDir);

        // when
        ProductLoader.loadProducts(testFile.getAbsolutePath(), productRepository, promotionRepository);

        // then
        // 콜라 검증 (프로모션 + 일반)
        Optional<Product> cola = productRepository.findByName("콜라");
        assertThat(cola.get().getPrice()).isEqualTo(1000);
        assertThat(cola.get().getPromotionQuantity()).isEqualTo(10);
        assertThat(cola.get().getNormalQuantity()).isEqualTo(10);
        assertThat(cola.get().getPromotion().getName()).isEqualTo("탄산2+1");

        // 오렌지주스 검증 (프로모션만)
        Optional<Product> orangeJuice = productRepository.findByName("오렌지주스");
        assertThat(orangeJuice.get().getPrice()).isEqualTo(1800);
        assertThat(orangeJuice.get().getPromotionQuantity()).isEqualTo(9);
        assertThat(orangeJuice.get().getNormalQuantity()).isEqualTo(0);
        assertThat(orangeJuice.get().getPromotion().getName()).isEqualTo("MD추천상품");

        // 물 검증 (일반만)
        Optional<Product> water = productRepository.findByName("물");
        assertThat(water.get().getPrice()).isEqualTo(500);
        assertThat(water.get().getPromotionQuantity()).isEqualTo(0);
        assertThat(water.get().getNormalQuantity()).isEqualTo(10);
        assertThat(water.get().getPromotion()).isNull();

        // 컵라면 검증 (프로모션 + 일반)
        Optional<Product> noodle = productRepository.findByName("컵라면");
        assertThat(noodle.get().getPrice()).isEqualTo(1700);
        assertThat(noodle.get().getPromotionQuantity()).isEqualTo(1);
        assertThat(noodle.get().getNormalQuantity()).isEqualTo(10);
        assertThat(noodle.get().getPromotion().getName()).isEqualTo("MD추천상품");
    }

    private File createTestFile(Path tempDir) throws IOException {
        File testFile = tempDir.resolve("test-products.md").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(testCsvContent);
        }
        return testFile;
    }
}
