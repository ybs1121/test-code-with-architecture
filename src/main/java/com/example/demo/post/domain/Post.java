package com.example.demo.post.domain;

import com.example.demo.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Clock;

@AllArgsConstructor
@Builder
@Getter
public class Post {


    private Long id;

    private String content;

    private Long createdAt;

    private Long modifiedAt;

    private User writer;

    public static Post from(User user, PostCreate postCreate) {
        return Post.builder()
                .content(postCreate.getContent())
                .createdAt(Clock.systemUTC().millis())
                .writer(user)
                .build();
    }

    public Post update(PostUpdate postUpdate) {
        return  Post.builder()
                .id(id)
                .content(postUpdate.getContent())
                .modifiedAt(Clock.systemUTC().millis())
                .writer(writer)
                .build();
    }
}
