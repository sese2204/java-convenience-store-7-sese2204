package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.error.ErrorMessage;

public class YesNoInputView {
    private static final String YES = "Y";
    private static final String NO = "N";

    public static final String RECOMMEND_ADDITIONAL_PURCHASE = "현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";
    public static final String NOTICE_NON_PROMOTIONAL_PURCHASE = "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    public static final String ASK_MEMBERSHIP_APPLY = "멤버십 할인을 받으시겠습니까? (Y/N)";
    public static final String ASK_CONTINUE_SHOPPING = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

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
