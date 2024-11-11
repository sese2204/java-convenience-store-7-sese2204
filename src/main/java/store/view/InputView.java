package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.regex.Pattern;
import store.error.ErrorMessage;

public class InputView {
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final Pattern ORDER_PATTERN = Pattern.compile("^(\\[([^-]+-\\d+)\\],)*\\[([^-]+-\\d+)\\]$");

    public static final String REQUEST_ORDER = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    public static final String RECOMMEND_ADDITIONAL_PURCHASE = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    public static final String NOTICE_NON_PROMOTIONAL_PURCHASE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    public static final String ASK_MEMBERSHIP_APPLY = "멤버십 할인을 받으시겠습니까? (Y/N)";
    public static final String ASK_CONTINUE_SHOPPING = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

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

    public boolean recommendAdditionalPurchase(String productName) {
        System.out.printf(RECOMMEND_ADDITIONAL_PURCHASE + "%n", productName);
        return readYesNo();
    }

    public boolean noticeNonPromotionalPurchase(String productName, int quantity) {
        System.out.printf(NOTICE_NON_PROMOTIONAL_PURCHASE + "%n", productName, quantity);
        return readYesNo();
    }

    public boolean readMembershipApply() {
        System.out.println(ASK_MEMBERSHIP_APPLY);
        return readYesNo();
    }

    public boolean readContinueShopping() {
        System.out.println(ASK_CONTINUE_SHOPPING);
        return readYesNo();
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

    private boolean readYesNo() {
        while (true) {
            try {
                String input = Console.readLine().trim().toUpperCase();
                validateYesNo(input);
                return YES.equals(input);
            }catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void validateYesNo(String input) {
        if (!YES.equals(input) && !NO.equals(input)) {
            throw new IllegalArgumentException(ErrorMessage.INVALID_RESPONSE.getMessage());
        }
    }
}
