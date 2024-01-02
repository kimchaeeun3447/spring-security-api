package ItOperations.springsecurityproject.member.controller;

import ItOperations.springsecurityproject.common.ApiUtils;
import ItOperations.springsecurityproject.common.CommonResponse;
import ItOperations.springsecurityproject.member.dto.LoginRequest;
import ItOperations.springsecurityproject.member.dto.SignRequest;
import ItOperations.springsecurityproject.member.dto.SignResponse;
import ItOperations.springsecurityproject.member.service.AuthService;
import ItOperations.springsecurityproject.security.token.TokenDto;
import ItOperations.springsecurityproject.security.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;

    // 로그인 API
    @PostMapping(value = "/login")
    public ResponseEntity<CommonResponse<SignResponse>> login(@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(ApiUtils.success(authService.login(request), 200, "로그인 성공"), HttpStatus.OK);
    }

    // 회원가입 API
    @PostMapping(value = "/register")
    public ResponseEntity<CommonResponse<SignResponse>> register(@RequestBody SignRequest request) throws Exception {
        return new ResponseEntity<>(ApiUtils.success(authService.register(request), 201, "회원가입 성공"), HttpStatus.CREATED);
    }

    // 토큰 재발급 API
    @GetMapping(value = "/refresh")
    public ResponseEntity<CommonResponse<TokenDto>> refresh(@RequestBody TokenDto tokenDto) throws Exception {
        return new ResponseEntity<>(ApiUtils.success(tokenService.refreshAccessToken(tokenDto), 200, "토큰 재발급 성공"), HttpStatus.OK);
    }

    /* 요청 보낸 token이 인증되었고, 권한에 맞는 URL로 요청할 경우에 접근 가능*/
    // 사용자 정보 조회 API - USER 권한이 있는 회원만
    @GetMapping(value = "/user/info")
    public ResponseEntity<CommonResponse<SignResponse>> getUser(@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(ApiUtils.success(authService.getMember(request.getBody().getId()), 200, "사용자 회원 정보 조회 성공"), HttpStatus.OK);
    }

    // 관리자 정보 조회 API - ADMIN 권한이 있는 회원만
    @GetMapping(value = "/admin/info")
    public ResponseEntity<CommonResponse<SignResponse>> getAdmin(@RequestBody LoginRequest request) throws Exception {
        return new ResponseEntity<>(ApiUtils.success(authService.getMember(request.getBody().getId()), 200, "관리자 회원 정보 조회 성공"), HttpStatus.OK);
    }
}
