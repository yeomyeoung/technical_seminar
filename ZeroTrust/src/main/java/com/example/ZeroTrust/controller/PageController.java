package com.example.ZeroTrust.controller;

import com.example.ZeroTrust.entity.User;
import com.example.ZeroTrust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    // 홈 페이지
    @GetMapping("/home")
    public String home() {
        return "home"; // home.html 반환
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html 반환
    }

    // 로그인 처리
    @PostMapping("/login")
    public String doLogin(@RequestParam String userID,
                          @RequestParam String password,
                          Model model) {
                                // 🔥 입력값 확인
        System.out.println("[DEBUG] 입력된 ID: " + userID);
        System.out.println("[DEBUG] 입력된 PW: " + password);

        User user = userService.loginUser(userID, password);
        if (user == null) {
            model.addAttribute("error", "ID 또는 비밀번호가 잘못되었습니다.");
            return "login_page";
        }

        // 관리자면 Kibana로 리다이렉트
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            System.out.println("[DEBUG] 관리자 로그인 → Kibana 리다이렉트");
            return "dashboard";  // Kibana로 이동
        } else {
            System.out.println("[DEBUG] 일반 사용자 로그인 → userpage 이동");

            model.addAttribute("user", user);
            return "userpage"; // 일반 사용자 페이지
        }
    }
}
