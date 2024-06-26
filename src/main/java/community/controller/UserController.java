package community.controller;

import community.dto.user.AddUserRequest;
import community.dto.user.CheckDuplicateRequest;
import community.dto.user.DeleteUserIdsRequest;
import community.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 api")
@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public String signup(AddUserRequest request) {
        userService.save(request);  // 회원 가입(저장)
        return "redirect:/login";   // 회원 가입 처리 후 로그인 페이지로 강제 이동
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteUsers(@RequestBody DeleteUserIdsRequest request) {
        try {
            userService.deleteUsers(request.getUserIds());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // 다른 예외 처리 (예: 500 Internal Server Error 반환)
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/checkEmail")
    public ResponseEntity<Void> checkEmail(@RequestBody CheckDuplicateRequest request) {
        System.out.println(request.getEmail());
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PostMapping("/checkNickName")
    public ResponseEntity<Void> checkNickName(@RequestBody CheckDuplicateRequest request) {
        System.out.println(request.getNickName());
        if (userService.existsByNickName(request.getNickName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
