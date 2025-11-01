package com.example.demo.post.domain;

import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PostResponseTest {
    @Test
    public void Post로_PostResponse생성가능() {
        // Given

        User user = User.builder()
                .id(1L)
                .email("test@kakao.com")
                .nickname("test")
                .address("Seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                .build();
        Post post = Post.builder()
                .content("helloworld")
                .writer(user)
                .build();
        // When
        PostResponse postResponse = PostResponse.from(post);
        // Then
        assertThat(postResponse.getContent()).isEqualTo("helloworld");

    }
}
