package community.controller;

import community.domain.user.UserEntity;
import community.dto.user.ArticleDto;
import community.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@Tag(name = "아티클 CRUD")
@RestController
@RequestMapping("/api/articles")
@Slf4j
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    // 게시물 등록 api
    @PostMapping
    public ResponseEntity<ArticleDto.ArticleResponseDto> addArticle(
            @RequestBody ArticleDto.ArticleRequestDto request,
            @AuthenticationPrincipal UserEntity user
    ) {
        if (request == null || user == null) { // 요청 또는 사용자가 null인 경우 처리
            return ResponseEntity.badRequest().build(); // 잘못된 요청 응답
        }

        ArticleDto.ArticleResponseDto responseDto = articleService.save(request, user);
        if (responseDto == null) { // 응답이 null인 경우 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 내부 서버 오류 응답
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    //게시물 단 건 조회 api
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto.ArticleResponseDto> getArticle(@PathVariable Long id) {
        ArticleDto.ArticleResponseDto article = articleService.getArticleById(id);
        return ResponseEntity.ok(article);
    }


    //게시글 단 건 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteArticleById(@PathVariable Long id, @AuthenticationPrincipal UserEntity user) {
        boolean deleted = articleService.deleteById(id, user);
        if (deleted) {
            return ResponseEntity.noContent().build(); // 삭제 성공
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 삭제 실패
        }
    }


    //게시물 단 건 수정 api
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDto.ArticleResponseDto> updateBoard(
            @PathVariable Long id,
            @RequestBody ArticleDto.ArticleRequestDto request,
            @AuthenticationPrincipal UserEntity user  //
    ) {
        log.info("title: " + request.getTitle());
        log.info("content: " + request.getContent());
        ArticleDto.ArticleResponseDto updatedArticle = articleService.updateArticle(id, request, user);
        log.info("new-title: " + updatedArticle.getTitle());
        log.info("new-content: " + updatedArticle.getContent());
        return ResponseEntity.ok(updatedArticle);
    }

    // 다른 필요한 API 메서드들을 추가 가능
    // 나중에 게시글 목록 페이지에서 다른 카테고리 누르면 category에 해당하는 모든 글 조회 가능
    /*@GetMapping
    public ResponseEntity<List<ArticleDto.ArticleResponseDto>> getAllArticlesByCategory(@PathVariable CategoryType type) {
        List<ArticleDto.ArticleResponseDto> articles = articleService.getAllArticlesByCategory(type);
        return ResponseEntity.ok(articles);
    }*/

}
