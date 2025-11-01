package com.example.demo.medium;


import com.example.demo.user.mock.FakeMailSender;
import com.example.demo.user.service.CertificationService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어보냈는지_확인한다(){
        // Given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);
        // When
        certificationService.send("test@test.com", 1L, "aaaaaaaa-aaa-aaa-aaaaaaa");

        // Then
        assertThat(fakeMailSender.getEmail()).isEqualTo("test@test.com");
        assertThat(fakeMailSender.getTitle()).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.getContent()).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaaaaaa-aaa-aaa-aaaaaaa");

    }
}