package com.example.demo.user.domain;


import com.example.demo.common.ClockHolder;
import com.example.demo.common.UuidHolder;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;


@AllArgsConstructor
@Builder
@Getter
public class User {

    private Long id;
    private String email;
    private String nickname;
    private String address;
    private String certificationCode;
    private UserStatus status;
    private Long lastLoginAt;

    public static User from(UserCreate userCreate, UuidHolder uuidHolder) {
        return User.builder()
                .email(userCreate.getEmail())
                .nickname(userCreate.getNickname())
                .address(userCreate.getAddress())
                .status(UserStatus.PENDING)
                .certificationCode(uuidHolder.random())
                .build();
    }

    public User update(UserUpdate userUpdate) {
        return User.builder()
                .id(id)
                .email(email)
                .nickname(userUpdate.getNickname())
                .address(userUpdate.getAddress())
                .certificationCode(certificationCode)
                .status(status)
                .lastLoginAt(lastLoginAt)
                .build();
    }

    public User login(ClockHolder clockHolder) {
        return User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .certificationCode(certificationCode)
                .status(status)
                .lastLoginAt(clockHolder.millis())
                .build();
    }

    public User certificate(String certificationCode) {
        if (!this.certificationCode.equals(certificationCode)) {
            throw new CertificationCodeNotMatchedException();
        }
        return User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .address(address)
                .certificationCode(certificationCode)
                .status(UserStatus.ACTIVE)
                .lastLoginAt(lastLoginAt)
                .build();
    }
}
