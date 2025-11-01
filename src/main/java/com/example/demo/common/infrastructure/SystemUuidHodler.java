package com.example.demo.common.infrastructure;


import com.example.demo.common.UuidHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SystemUuidHodler implements UuidHolder {
    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }
}
