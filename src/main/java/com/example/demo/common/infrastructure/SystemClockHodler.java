package com.example.demo.common.infrastructure;


import com.example.demo.common.ClockHolder;
import org.springframework.stereotype.Component;

import java.time.Clock;

@Component
public class SystemClockHodler implements ClockHolder {
    @Override
    public long millis() {
        return Clock.systemUTC().millis();
    }
}
