package com.db40.library.sh;

import java.util.Comparator;

public class BorrowComparator implements Comparator<Borrow> {

    @Override
    public int compare(Borrow o1, Borrow o2) {
        // 첫 번째 기준: borrowState가 "연체"인 경우를 먼저 오게 합니다.
        boolean isOverdue1 = "연체".equals(o1.getBorrowState());
        boolean isOverdue2 = "연체".equals(o2.getBorrowState());

        if (isOverdue1 && !isOverdue2) {
            return -1; // o1이 "연체"이고 o2는 아니면 o1이 먼저 옵니다.
        } else if (!isOverdue1 && isOverdue2) {
            return 1; // o2가 "연체 중"이고 o1는 아니면 o2가 먼저 옵니다.
        } else {
            // 두 항목 모두 "연체"이거나 둘 다 아닌 경우
            // 두 번째 기준: overdueDays가 큰 순서대로 (내림차순) 정렬합니다.

            Long overdueDays1 = o1.getOverdueDays();
            Long overdueDays2 = o2.getOverdueDays();

            if (isOverdue1 && isOverdue2) {
                // 두 항목 모두 "연체"인 경우: overdueDays는 null이 아니라는 가정 하에 비교
                return overdueDays2.compareTo(overdueDays1); // 내림차순
            } else {
                // 두 항목 모두 "연체 중"이 아닌 경우: overdueDays가 null일 수 있으므로 null 처리 필요
                 if (overdueDays1 == null && overdueDays2 == null) {
                    return 0; // 둘 다 null이면 순서 변경 없음
                } else if (overdueDays1 == null) {
                    return 1; // overdueDays1만 null이면 o2가 먼저 옵니다.
                } else if (overdueDays2 == null) {
                    return -1; // overdueDays2만 null이면 o1이 먼저 옵니다.
                } else {
                    // 둘 다 null이 아니면 overdueDays 값을 비교하여 내림차순 정렬
                    return overdueDays2.compareTo(overdueDays1);
                }
            }
        }
    }
}