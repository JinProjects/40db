package com.db40.library.sh;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
// import java.time.temporal.ChronoUnit; // 주석 제거 또는 사용

import com.db40.library.member.Member;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Borrow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long borrowNo; // 기본 키

    @ManyToOne
    @JoinColumn(name = "member_id") // 외래 키 컬럼명
    private Member member; // Member 엔티티와의 관계

    @ManyToOne
    @JoinColumn(name = "book_no") // 외래 키 컬럼명
    private Books bookNo; // Books 엔티티와의 관계 (타입 수정!)

    @Column(name = "book_title")
    private String bookTitle;

    @Column(name = "borrow_state")
    private String borrowState;

    @Column(name = "borrow_date", updatable = false) // 대출일은 보통 수정되지 않음
    private LocalDateTime borrowDate = LocalDateTime.now();

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "remain_days")
    private Long remainDays;

    @Column(name = "overdue_days")
    private Long overdueDays;

    @Column(name = "return_date", nullable = true)
    private LocalDateTime returnDate;

    @PrePersist
    public void setInitialDates() {
        if (this.borrowDate == null) {
            this.borrowDate = LocalDateTime.now();
        }
        // 예시: 대출 기간 14일
        // this.dueDate = this.borrowDate.plusDays(14);
        // this.remainDays = 14L;

        // 테스트용 3분
        this.dueDate = this.borrowDate.plusMinutes(3);
        this.remainDays = 3L;

        // overdueDays는 보통 스케줄러나 반납 시점에 계산되므로 여기서 초기화하지 않음
        // this.overdueDays = 0L; // 필요하다면 0으로 초기화
    }
}