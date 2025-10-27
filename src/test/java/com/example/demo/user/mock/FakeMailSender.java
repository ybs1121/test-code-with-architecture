package com.example.demo.user.mock;

import com.example.demo.user.service.port.MailSender;
import lombok.Data;

@Data
public class FakeMailSender implements MailSender {
    private String email;
    private String title;
    private String content;

    @Override
    public void send(String email, String title, String content) {
        this.email = email;
        this.title = title;
        this.content = content;
    }
}
