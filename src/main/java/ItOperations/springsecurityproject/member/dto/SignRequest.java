package ItOperations.springsecurityproject.member.dto;

import lombok.Getter;

/* 회원가입 Request DTO */
@Getter
public class SignRequest {

    private String id;

    private String pw;

    private String email;
}