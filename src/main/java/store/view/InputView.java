package store.view;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    private static final String YES = "Y";
    private static final String NO = "N";
    private static final String ERROR_INVALID_RESPONSE = "[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.";

    public static final String REQUEST_ORDER = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    public static final String RECOMMEND_ADDITIONAL_PURCHASE = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    public static final String NOTICE_NON_PROMOTIONAL_PURCHASE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    public static final String ASK_MEMBERSHIP_APPLY = "멤버십 할인을 받으시겠습니까? (Y/N)";
    public static final String ASK_CONTINUE_SHOPPING = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public String requestOrder() {
        System.out.println(REQUEST_ORDER);
        return Console.readLine();
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

    private boolean readYesNo() {
        while (true) {
            String input = Console.readLine().trim().toUpperCase();
            validateYesNo(input);
            return YES.equals(input);
        }
    }

    private void validateYesNo(String input) {
        if (!YES.equals(input) && !NO.equals(input)) {
            throw new IllegalArgumentException(ERROR_INVALID_RESPONSE);
        }
    }
}
