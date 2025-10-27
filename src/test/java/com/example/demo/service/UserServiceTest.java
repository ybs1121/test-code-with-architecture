package com.example.demo.service;

import com.example.demo.user.exception.CertificationCodeNotMatchedException;
import com.example.demo.user.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserCreate;
import com.example.demo.user.domain.dto.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

//import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    void getByEmail_Active상태인유저_찾아올수있다() {
        // Given
        String email = "test@naver.com";
        // When
        UserEntity result = userService.getByEmail(email);
        // Then
        assertThat(result.getNickname()).isEqualTo("test");
    }

    @Test
    void getByEmail_PENDING_상태인유저_찾을_수_없다() {
        // Given
        String email = "test2@naver.com";
        // When
        // Then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getByEmail(email);

        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void getById_Active상태인유저_찾아올수있다() {
        // Given
        String email = "test@naver.com";
        // When
        UserEntity result = userService.getById(1);
        // Then
        assertThat(result.getNickname()).isEqualTo("test");
    }

    @Test
    void getById_PENDING_상태인유저_찾을_수_없다() {
        // Given
        String email = "test2@naver.com";
        // When
        // Then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getById(2);

        }).isInstanceOf(ResourceNotFoundException.class);

    }


    @Test
    void userCreateDto_를_이용해서_유저를_생성할_수_있다() {
        // Given
        UserCreate createDto = UserCreate.builder()
                .email("test3@test.com")
                .address("NewYork")
                .nickname("test3")
                .build();
        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        // When
        UserEntity result = userService.create(createDto);
        // Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
//        assertThat(result.getCertificationCode()).isEqualTo("??");
    }

    @Test
    void userUpdateDto_를_이용해서_유저를_수정할_수_있다() {
        // Given
        UserUpdate updateDto = UserUpdate.builder()
                .address("NewYork")
                .nickname("test3-n")
                .build();

        // When
        UserEntity result = userService.update(1, updateDto);
        // Then
        UserEntity userEntity = userService.getById(result.getId());
        assertThat(result.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("NewYork");
        assertThat(userEntity.getNickname()).isEqualTo("test3-n");
//        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
    }

    @Test
    void 로그인() {
        // Given

        // When
        userService.login(1);
        // Then
        UserEntity userEntity = userService.getById(1);

//        assertThat(userEntity.getLastLoginAt()).isEqualTo(???);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0);

    }

    @Test
    void PENDING_상태의_사용자는_인증코드로_ACTIVE_시킬수있다() {
        // Given

        // When
        userService.verifyEmail(2, "aaaaaaaa-aaa-aaa-aaaaaaa");
        // Then
        UserEntity user = userService.getById(2);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증코드를_입력하면_에러를_반환한다() {
        // Given

        // When Then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaaaa-aaa-aaa-aaaaaaa1232");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);

    }


}