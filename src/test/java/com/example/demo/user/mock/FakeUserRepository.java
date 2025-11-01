package com.example.demo.user.mock;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class FakeUserRepository implements UserRepository {

    private final AtomicLong counter = new AtomicLong(0);
    private final List<User> data = new ArrayList<>();


    @Override
    public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
        return data.stream().filter(user -> user.getId() == id && user.getStatus() == userStatus).findFirst();
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
        return data.stream().filter(user -> user.getEmail().equals(email) && user.getStatus() == userStatus).findFirst();
    }

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId() == 0) {
            User saveUser = User.builder()
                    .id(counter.incrementAndGet())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .address(user.getAddress())
                    .status(user.getStatus())
                    .certificationCode(user.getCertificationCode())
                    .lastLoginAt(user.getLastLoginAt())
                    .build();
            data.add(saveUser);
            return saveUser;
        } else {
            data.removeIf(item -> Objects.equals(item.getId(), user.getId()));
            data.add(user);
            return user;
        }


    }

    @Override
    public Optional<User> findById(long id) {
        return data.stream().filter(user -> user.getId() == id).findFirst();
    }
}
