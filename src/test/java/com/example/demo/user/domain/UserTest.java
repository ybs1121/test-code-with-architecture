package com.example.demo.user.domain;

import com.example.demo.user.mock.TestClockHodler;
import com.example.demo.user.mock.TestUuidHolder;
import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserTest {

    @Test
    public void User는_UserCreate_객체로_생성할_수_있다(){
        // given
        UserCreate userCreate = UserCreate.builder()
                .email("test@kakao.com")
                .nickname("test")
                .address("Pangyo")
                .build();

        // when
        User user = User.from(userCreate, new TestUuidHolder("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("test@kakao.com");
        assertThat(user.getNickname()).isEqualTo("test");
        assertThat(user.getAddress()).isEqualTo("Pangyo");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    }


    @Test
    public void User는_UserUpdate_객체로_데이터를_업데이트_할_수_있다(){
        // given
        User user = User.builder()
                .id(1L)
                .email("test@kakao.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("test-k")
                .address("Pangyo")
                .build();

        // when
        user = user.update(userUpdate);

        // then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@kakao.com");
        assertThat(user.getNickname()).isEqualTo("test-k");
        assertThat(user.getAddress()).isEqualTo("Pangyo");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        assertThat(user.getLastLoginAt()).isEqualTo(100L);

    }

    @Test
    public void User는_로그인을_할_수_있고_마지막_로그인_시간이_변경된다(){
// given
        User user = User.builder()
                .id(1L)
                .email("test@kakao.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        user = user.login(new TestClockHodler(1678530673958L));

        // then
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);

    }

    @Test
    public void User는_유효한_인증코드로_계정활성화_가능(){
        // given
        User user = User.builder()
                .id(1L)
                .email("test@kakao.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        user = user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    public void User는_유효하지않는_인증코드로_계정활성화_불가능(){
        // given
        User user = User.builder()
                .id(1L)
                .email("test@kakao.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();

        // when
        // then
        assertThatThrownBy(() -> user.certificate("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaab"))
                .isInstanceOf(CertificationCodeNotMatchedException.class);

    }


}
