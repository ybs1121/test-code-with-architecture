package com.example.demo.user.mock;


import com.example.demo.common.ClockHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClockHodler implements ClockHolder {

    private final long millis;

    @Override
    public long millis() {
        return millis;
    }
}
