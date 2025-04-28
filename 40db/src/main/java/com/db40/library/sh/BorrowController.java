package com.db40.library.sh;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Optional 추가

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException; // 예외 처리 추가
import org.springframework.http.HttpStatus; // HttpStatus 추가

import com.db40.library.member.*;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowRepository borrowRepository; // 이름 변경 (inOutRepository -> borrowRepository)
    private final MemberRepository memberRepository;
    private final BooksRepository booksRepository;

    @GetMapping("/borrow/alllist")
    public String allList(Model model) {
        List<Borrow> allList = borrowRepository.findAll();
        allList.sort(new BorrowComparator());
        model.addAttribute("allList", allList);
        return "borrow/inout_admin";
    }

    @PostMapping("/borrow")
    public String borrow(@RequestParam("bookNo") Integer bookNoInt, // Integer로 받음
                         Principal principal) {

        // 1. 사용자 확인
        Member member = memberRepository.findByMemberId(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        // 2. 책 확인 (Integer를 Long으로 변환하여 조회)
        Long bookNo = Long.valueOf(bookNoInt); // Integer를 Long으로 변환
        Books book = booksRepository.findById(bookNo) // findById 사용 (반환 타입 Optional<Books>)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "책 정보를 찾을 수 없습니다: " + bookNo));

        // 3. 대출 기록 생성 및 저장
        Borrow borrow = new Borrow(); // 변수명 borrow로 변경
        borrow.setMember(member);
        borrow.setBookNo(book); // Books 객체 자체를 설정
        borrow.setBookTitle(book.getBookTitle()); // Books 객체에서 제목 가져오기
        borrow.setBorrowState("대출 중");

        borrowRepository.save(borrow);

        // 4. 대출 목록 페이지로 리다이렉트 (경로 확인 필요)
        return "redirect:/borrow/inoutUser"; // 경로 수정
    }

    @GetMapping("/borrow/inoutUser")
    public String userBorrowList(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/member/login"; // 로그인되지 않았으면 로그인 페이지로
        }
        Member member = memberRepository.findByMemberId(principal.getName()).orElse(null);
        if (member != null) {
            List<Borrow> borrowList = borrowRepository.findByMemberId(member); // findByMemberId가 Member 객체를 받도록 구현되어 있어야 함
            model.addAttribute("borrowList", borrowList);
        }
        return "/borrow/inout_user";
    }

    @PostMapping("/borrow/bookReturn")
    public String bookReturn(@RequestParam("bookNo") Long bookId, Principal principal) {

        // 1. 사용자 확인
        Member member = memberRepository.findByMemberId(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));

        // 2. 해당 사용자의 대출 기록 조회
        List<Borrow> userBorrows = borrowRepository.findByMemberId(member);
        Borrow targetBorrow = null; // 변수명 targetBorrow로 변경

        // 3. 반납할 대출 기록 찾기 (수정된 로직)
        for (Borrow borrow : userBorrows) {
            Books relatedBook = borrow.getBookNo(); // 연관된 Books 객체 가져오기

            // Books 객체와 그 ID가 null이 아니고, 입력받은 bookId와 같으며, 아직 반납되지 않은 경우
            if (relatedBook != null && relatedBook.getBookNo() != null && relatedBook.getBookNo().equals(bookId) && borrow.getReturnDate() == null) {
                targetBorrow = borrow;
                break;
            }
        }

        // 4. 찾은 대출 기록 처리
        if (targetBorrow != null) {
            String currentState = targetBorrow.getBorrowState();
            targetBorrow.setReturnDate(LocalDateTime.now());

            if ("대출 중".equals(currentState)) {
                targetBorrow.setBorrowState("정상 반납");
            } else if ("연체".equals(currentState)) {
                targetBorrow.setBorrowState("연체 반납");
                // 연체 시 경고 횟수 증가 (null 체크는 위에서 했으므로 생략 가능)
                int currentWarnings = member.getMemberWarning() != null ? member.getMemberWarning() : 0;
                member.setMemberWarning(currentWarnings + 1);
                memberRepository.save(member); // 변경된 회원 정보 저장
            }
            // 그 외 상태는 변경하지 않음 (이미 반납된 경우 등)

            borrowRepository.save(targetBorrow); // 변경된 대출 정보 저장
        } else {
            // 해당 책 대출 기록을 찾지 못하거나 이미 반납된 경우 (필요시 로그 또는 메시지 처리)
            System.out.println("반납할 대출 기록을 찾을 수 없습니다. Book ID: " + bookId + ", Member: " + principal.getName());
        }

        // 5. 대출 목록 페이지로 리다이렉트 (경로 확인 필요)
        return "redirect:/borrow/inoutUser"; // 경로 수정
    }
}