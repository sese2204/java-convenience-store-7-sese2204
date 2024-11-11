package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.regex.Pattern;
import store.error.ErrorMessage;

public class OrderInputView {
    private static final Pattern ORDER_PATTERN = Pattern.compile("^(\\[([^-]+-\\d+)\\],)*\\[([^-]+-\\d+)\\]$");
    public static final String REQUEST_ORDER = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";

    public String requestOrderInput() {
        while (true) {
            try {
                System.out.println(REQUEST_ORDER);
                String input = Console.readLine().trim();
                validateOrderFormat(input);
                return input;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validateOrderFormat(String input) {
        if (!ORDER_PATTERN.matcher(input).matches()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_RESPONSE.getMessage());
        }
        validateEachOrderItem(input);
    }

    private void validateEachOrderItem(String input) {
        String[] items = input.split(",");
        for (String item : items) {
            validateOrderItem(item.trim());
        }
    }

    private void validateOrderItem(String item) {
        validateBrackets(item);
        String content = removeBrackets(item);
        String[] parts = splitOrderContent(content);
        validateProductName(parts[0]);
        validateQuantity(parts[1]);
    }

    private void validateBrackets(String item) {
        if (!item.startsWith("[") || !item.endsWith("]")) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_RESPONSE.getMessage());
        }
    }

    private String removeBrackets(String item) {
        return item.substring(1, item.length() - 1);
    }

    private String[] splitOrderContent(String content) {
        String[] parts = content.split("-");
        if (parts.length != 2) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_RESPONSE.getMessage());
        }
        return parts;
    }

    private void validateProductName(String name) {
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_RESPONSE.getMessage());
        }
    }

    private void validateQuantity(String quantityStr) {
        try {
            int quantity = parseQuantity(quantityStr);
            validatePositiveQuantity(quantity);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_RESPONSE.getMessage());
        }
    }

    private int parseQuantity(String quantityStr) {
        return Integer.parseInt(quantityStr);
    }

    private void validatePositiveQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_RESPONSE.getMessage());
        }
    }
}
