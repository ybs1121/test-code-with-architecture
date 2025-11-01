package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PostTest {

    @Test
    public void PostCreate로_게시물을_생성할_수있다() {
        // Given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloworld")
                .build();

        User writer =  User.builder()
                .id(1L)
                .email("test@naver.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode(UUID.randomUUID().toString())
                .build();

        // When
        Post post = Post.from(writer, postCreate);
        // Then
        assertThat(post.getContent()).isEqualTo("helloworld");
        assertThat(post.getWriter().getEmail()).isEqualTo("test@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("test");


    }
}
