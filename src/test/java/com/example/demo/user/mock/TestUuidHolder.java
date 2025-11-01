package com.example.demo.user.mock;


import com.example.demo.common.UuidHolder;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class TestUuidHolder implements UuidHolder {

    private final String uuid;


    @Override
    public String random() {
        return uuid;
    }
}
