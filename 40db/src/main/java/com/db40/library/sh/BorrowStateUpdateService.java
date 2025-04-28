package com.db40.library.sh;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowStateUpdateService {
    private final BorrowRepository inoutRepository;

    @Transactional
    @Scheduled(cron = "0 * * * * *") 
    public void updateBorrowStatesAndOverdueDays() {
        LocalDateTime now = LocalDateTime.now();

        // 1. "대출 중"인 책들 상태 업데이트
        List<Borrow> borrowingBooks = inoutRepository.findByBorrowState("대출 중");
        for (Borrow inOut : borrowingBooks) {
            if (now.isAfter(inOut.getDueDate())) {
                // 연체 시작: 상태 변경, remainDays 0, overdueDays 초기 계산
                inOut.setBorrowState("연체");
                inOut.setRemainDays(0L);
                long initialOverdueMinutes = ChronoUnit.MINUTES.between(inOut.getDueDate(), now);
                inOut.setOverdueDays(initialOverdueMinutes < 0 ? 0 : initialOverdueMinutes); // 음수 방지
            } else {
                // 아직 대출 중: 남은 시간 계산
                long remainMinutes = ChronoUnit.MINUTES.between(now, inOut.getDueDate());
                inOut.setRemainDays(remainMinutes < 0 ? 0 : remainMinutes); 
             
            }
            // 변경된 내용 저장 (대출 중 -> 연체 or 남은 시간 업데이트)
             inoutRepository.save(inOut);
        }

        // 2. 이미 "연체" 상태인 책들 overdueDays 업데이트
        List<Borrow> overdueBooks = inoutRepository.findByBorrowState("연체");
        for (Borrow inOut : overdueBooks) {
            long currentOverdueMinutes = ChronoUnit.MINUTES.between(inOut.getDueDate(), now);
            inOut.setOverdueDays(currentOverdueMinutes < 0 ? 0 : currentOverdueMinutes); 
            inoutRepository.save(inOut); // 연체 시간 업데이트 저장
        }
    }
}