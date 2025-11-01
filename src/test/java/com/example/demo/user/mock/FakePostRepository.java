package com.example.demo.user.mock;

import com.example.demo.post.domain.Post;
import com.example.demo.post.service.port.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakePostRepository implements PostRepository {

    private final AtomicLong counter = new AtomicLong(0);
    private final List<Post> data = new ArrayList<>();

    @Override
    public Optional<Post> findById(long id) {
        return data.stream().filter(data -> data.getId() == id).findAny();
    }

    @Override
    public Post save(Post post) {
        if (post.getId() == null || post.getId() == 0) {
            Post savedPost = Post.builder()
                    .id(counter.incrementAndGet()) // id 생성기 사용 가정
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .writer(post.getWriter())
                    .build();
            data.add(savedPost);
            return savedPost;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), post.getId()));
            data.add(post);
            return post;
        }
    }
}
