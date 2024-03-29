package com.binaryho.jwtsecurity.controller;

import com.binaryho.jwtsecurity.model.User;
import com.binaryho.jwtsecurity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("home")
    public String home() {
        return "<h1>home sweet home</h1>";
    }

    @PostMapping("token")
    public String token() {
        return "<h1>내가 만든 Token~~~~</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles("ROLE_USER");
        userRepository.save(user);
        return "회원가입완료";
    }

    @GetMapping("/api/v1/user")
    public String user() {
        return "user ok";
    }

    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager ok";
    }
    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin ok";
    }
}
