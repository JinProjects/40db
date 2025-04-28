package com.db40.library.sh;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany; // 추가

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_no")
    private Long bookNo; // 기본 키 (Borrow의 bookNo와 이름 겹치지 않게 주의)

    @Column(name = "book_isbn", length = 100)
    private String bookIsbn;

    @Column(name = "book_intro", length = 300) // 길이를 늘리는 것을 고려해보세요.
    private String bookIntro;

    @ManyToOne
    @JoinColumn(name = "book_category_no")
    private Category category; // Category 엔티티와의 관계

    @Column(name = "book_title", nullable = false, length = 200)
    private String bookTitle;

    @Column(name = "book_author", nullable = false, length = 100)
    private String bookAuthor;

    @Column(name = "book_publisher", length = 100)
    private String bookPublisher;

    @Column(name = "book_cover", nullable = true, length = 500) // 표지 URL은 길 수 있으므로 길이 조정 및 nullable 허용 고려
    private String bookCover;

    @Column(name = "book_published_date", length = 100) // 타입 변경 고려 (LocalDate or String)
    private String bookPublishedDate;

    @Column(name = "book_description", columnDefinition = "TEXT") // 책 설명은 길 수 있으므로 TEXT 타입 고려
    private String bookDescription;

    @Column(name = "book_entered_date", updatable = false) // 입고일은 보통 수정되지 않음
    @CreationTimestamp
    private LocalDateTime bookEnteredDate;

    @Column(name = "book_hit")
    @ColumnDefault("0")
    private Integer bookHit = 0; // 초기값 설정

    // Borrow 엔티티와의 관계 설정 (Books 하나에 여러 Borrow가 가능)
    @OneToMany(mappedBy = "bookNo") // Borrow 엔티티의 'bookNo' 필드에 의해 매핑됨
    private List<Borrow> borrows = new ArrayList<>(); // 필드 이름을 borrows로 변경

    // toString() 메서드는 필요에 따라 유지하거나 수정
    @Override
    public String toString() {
        return "Books [bookNo=" + bookNo + ", bookIsbn=" + bookIsbn + ", bookTitle=" + bookTitle
                + ", bookAuthor=" + bookAuthor + ", bookPublisher=" + bookPublisher + ", bookPublishedDate="
                + bookPublishedDate + ", bookEnteredDate=" + bookEnteredDate + ", bookHit=" + bookHit + "]";
    }
}