package com.example.demo.repository;

import com.example.demo.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;


//    @Test
//    void UserRepository_가_제대로_연결되었다() {
//        //given
//        UserEntity userEntity = new UserEntity();
//        userEntity.setEmail("test@naver.com");
//        userEntity.setAddress("Seoul");
//        userEntity.setNickname("test");
//        userEntity.setStatus(UserStatus.ACTIVE);
//        userEntity.setCertificationCode("aaaaaaaa-aaa-aaa-aaaaaaa");
//
//        //when
//        UserEntity result = userRepository.save(userEntity);
//
//        //then
//        assertThat(result.getId()).isNotNull();
//    }

    @Test
    void findByIdAndStatus로_유저_데이터를_찾아올수_있다() {
        // Given


        // When
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);
        // Then
        assertThat(result.isPresent()).isTrue();

    }

    @Test
    void findByIdAndStatus_데이터가_Optional_Empty를_내려준다() {
        // Given


        // When
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);
        // Then
        assertThat(result.isEmpty()).isTrue();
    }


    @Test
    void findByEmailAndStatus로_유저_데이터를_찾아올수_있다() {
        // Given


        // When
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("test@naver.com", UserStatus.ACTIVE);
        // Then
        assertThat(result.isPresent()).isTrue();

    }

    @Test
    void findByEmailAndStatus_데이터가_Optional_Empty를_내려준다() {
        // Given


        // When
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("test@naver.com", UserStatus.PENDING);
        // Then
        assertThat(result.isEmpty()).isTrue();
    }

}