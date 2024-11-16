package store.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import store.domain.Promotion;
import store.domain.PromotionRepository;

public class PromotionLoader {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final int EXPECTED_COLUMNS = 5;

    public static void loadPromotions(String filePath, PromotionRepository repository) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            processFile(br, repository);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFile(BufferedReader br, PromotionRepository repository) throws IOException {
        br.readLine(); // Skip header
        String line;
        while ((line = br.readLine()) != null) {
            processLine(line, repository);
        }
    }

    private static void processLine(String line, PromotionRepository repository) {
        String[] values = line.split(",");
        if (isValidLine(values)) {
            repository.add(createPromotion(values));
        }
    }

    private static boolean isValidLine(String[] values) {
        return values.length == EXPECTED_COLUMNS;
    }

    private static Promotion createPromotion(String[] values) {
        return new Promotion(
                values[0],
                parseInteger(values[1]),
                parseInteger(values[2]),
                parseDate(values[3]),
                parseDate(values[4])
        );
    }

    private static int parseInteger(String value) {
        return Integer.parseInt(value.trim());
    }

    private static LocalDate parseDate(String value) {
        return LocalDate.parse(value.trim(), DATE_FORMATTER);
    }

}
