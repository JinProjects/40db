package com.db40.library.sh; // 네 프로젝트 패키지에 맞게 수정해줘

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired; // Autowired 사용 시 필요

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

// RecommandController에서 사용한 Recommand 서비스와 ObjectMapper를 여기서도 사용해야 해
// RecommendationService와 RecommandBookInfo 클래스는 네 프로젝트에 있다고 가정할게!
// import your.package.service.RecommendationService; // 네 서비스 패키지에 맞게 수정
// import your.package.domain.RecommandBookInfo; // 네 도메인/모델 패키지에 맞게 수정


// 메인 페이지를 담당할 컨트롤러야!
@Controller
public class MainController {

    private final Recommand recommandService;
    private final ObjectMapper objectMapper;

    public MainController(Recommand recommandService, ObjectMapper objectMapper) {
        this.recommandService = recommandService;
        this.objectMapper = objectMapper;
    }

    // 여기에 GetMapping 경로를 네 실제 메인 페이지 경로("/main")로 바꿔줘!
    // Spring이 context-path인 "/40db"와 이 "/main"을 합쳐서 "/40db/main"으로 인식할 거야.
    @GetMapping("/main")
    public String showMainPage(Model model) {
        List<RecommandBookInfo> recommendedBooks = Collections.emptyList();

        try {
            String recommandedBooksJson = recommandService.getRecommnadedBooks();
            Map<String, Object> responseMap = objectMapper.readValue(recommandedBooksJson, new TypeReference<Map<String, Object>>() {});
            Object itemObject = responseMap.get("item");

            if (itemObject instanceof List) {
                recommendedBooks = objectMapper.convertValue(itemObject, new TypeReference<List<RecommandBookInfo>>() {});
            }

            model.addAttribute("recommendedBooks", recommendedBooks);

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("recommendedBooks", Collections.emptyList());
        }

        return "main";
    }

    // 만약 다른 메인 페이지 관련 요청 (예: 검색 결과)이 있다면 여기에 추가 메소드를 만들겠지?
}