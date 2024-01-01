package ItOperations.springsecurityproject.member.controller;

import ItOperations.springsecurityproject.member.repository.MemberRepository;
import ItOperations.springsecurityproject.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final MemberRepository memberRepository;
    private final AuthService authService;
}
