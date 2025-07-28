package com.example.ZeroTrust.service;

import com.example.ZeroTrust.entity.User;
import com.example.ZeroTrust.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 로그인 메서드: userID와 password 검증 후 User 반환
    public User loginUser(String userID, String rawPassword) {
        // 🔥 입력 값 디버깅
        System.out.println("[DEBUG] 입력된 ID: " + userID);
        System.out.println("[DEBUG] 입력된 PW: " + rawPassword);

        User user = userRepository.findByUserID(userID);

        if (user == null) {
            System.out.println("[DEBUG] DB에서 해당 ID를 찾지 못했습니다.");
            return null; // 존재하지 않음
        }

        // 🔥 DB에서 가져온 정보 디버깅
        System.out.println("[DEBUG] DB에서 찾은 User: " + user.toString());
        System.out.println("[DEBUG] DB에 저장된 해시: " + user.getPassword());
        System.out.println("[DEBUG] DB에 저장된 role: " + user.getRole());

        boolean passwordMatch = passwordEncoder.matches(rawPassword, user.getPassword());
        System.out.println("[DEBUG] 비밀번호 매칭 결과: " + passwordMatch);

        if (passwordMatch) {
            System.out.println("[DEBUG] 로그인 성공!");
            return user;
        }

        System.out.println("[DEBUG] 비밀번호 불일치!");
        return null; // 비밀번호 불일치
    }
}
