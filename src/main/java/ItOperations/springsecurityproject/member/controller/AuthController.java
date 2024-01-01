package ItOperations.springsecurityproject.member.controller;

import ItOperations.springsecurityproject.member.dto.LoginRequest;
import ItOperations.springsecurityproject.member.dto.SignRequest;
import ItOperations.springsecurityproject.member.dto.SignResponse;
import ItOperations.springsecurityproject.member.repository.MemberRepository;
import ItOperations.springsecurityproject.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberRepository memberRepository;
    private final AuthService authService;

    // 로그인 API
    @PostMapping(value = "/login")
    public ResponseEntity<SignResponse> login(@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(authService.login(request), HttpStatus.OK);
    }

    // 회원가입 API
    @PostMapping(value = "/register")
    public ResponseEntity<Boolean> register(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }
}
