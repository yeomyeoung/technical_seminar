package com.example.ZeroTrust.controller;

import com.example.ZeroTrust.entity.User;
import com.example.ZeroTrust.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;


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
                          Model model,
                          HttpSession session) {
    
        System.out.println("[DEBUG] 입력된 ID: " + userID);
        System.out.println("[DEBUG] 입력된 PW: " + password);
    
        User user = userService.loginUser(userID, password);
    
        if (user == null) {
            model.addAttribute("error", "ID 또는 비밀번호가 잘못되었습니다.");
            return "login"; // 잘못된 이름 수정
        }
    
        session.setAttribute("loginUser", user); // Redis에 세션 저장됨
        session.setMaxInactiveInterval(1800);    // 세션 유효시간 30분 (선택)
        
        System.out.println("[DEBUG] 세션 ID: " + session.getId());
        
        System.out.println("[DEBUG] 로그인 성공! 세션 ID: " + session.getId());
    
        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/admin/dashboard";
        } else {
            return "redirect:/user/mypage";
        }
    }

    // 관리자 페이지
    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "dashboard";  // dashboard.html
    }

    // 사용자 페이지
    @GetMapping("/user/mypage")
    public String userPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loginUser");
        if (user == null || "ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "userpage";  // userpage.html
    }
}
