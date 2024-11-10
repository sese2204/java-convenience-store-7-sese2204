package store.util;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import store.domain.Promotion;
import store.domain.PromotionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DisplayName("프로모션 로더 테스트")
class PromotionLoaderTest {
    private PromotionRepository promotionRepository;
    private String testCsvContent;

    @BeforeEach
    void setUp() {
        promotionRepository = new PromotionRepository();
        testCsvContent = """
            name,buy,get,start_date,end_date
            탄산2+1,2,1,2024-01-01,2024-12-31
            MD추천상품,1,1,2024-01-01,2024-12-31
            반짝할인,1,1,2024-11-01,2024-11-30""";
    }

    @Test
    @DisplayName("프로모션 파일을 읽어 모든 프로모션 정보가 정상적으로 로드되어야 한다")
    void loadPromotions_ShouldLoadAllPromotionsFromFile(@TempDir Path tempDir) throws IOException {
        // given
        File testFile = createTestFile(tempDir);

        // when
        PromotionLoader.loadPromotions(testFile.getAbsolutePath(), promotionRepository);

        // then
        Promotion carbonPromotion = promotionRepository.findByName("탄산2+1");
        assertThat(carbonPromotion.getBuyCount()).isEqualTo(2);
        assertThat(carbonPromotion.getFreeCount()).isEqualTo(1);
        assertThat(carbonPromotion.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(carbonPromotion.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31));

        Promotion mdPromotion = promotionRepository.findByName("MD추천상품");
        assertThat(mdPromotion.getBuyCount()).isEqualTo(1);
        assertThat(mdPromotion.getFreeCount()).isEqualTo(1);
        assertThat(mdPromotion.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(mdPromotion.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31));

        Promotion flashPromotion = promotionRepository.findByName("반짝할인");
        assertThat(flashPromotion.getBuyCount()).isEqualTo(1);
        assertThat(flashPromotion.getFreeCount()).isEqualTo(1);
        assertThat(flashPromotion.getStartDate()).isEqualTo(LocalDate.of(2024, 11, 1));
        assertThat(flashPromotion.getEndDate()).isEqualTo(LocalDate.of(2024, 11, 30));
    }

    private File createTestFile(Path tempDir) throws IOException {
        File testFile = tempDir.resolve("test-promotions.md").toFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(testCsvContent);
        }
        return testFile;
    }
}
