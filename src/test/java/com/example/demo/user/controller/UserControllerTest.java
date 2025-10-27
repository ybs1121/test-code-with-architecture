package com.example.demo.user.controller;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.dto.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void 사용자는_특정_유저의_정보를_전달받을수_있다() throws Exception {

        // Given
        // When
        // Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nickname").value("test"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.email").value("test@naver.com"));


    }

    @Test
    void 사용자는_존재하지_않는_아이디로_호출할경우_404() throws Exception {

        // Given
        // When
        // Then
        mockMvc.perform(get("/api/users/112123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 112123를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_인증코드로_계정을_활성화할_수_있다() throws Exception {

        // Given
        // When
        // Then
        mockMvc.perform(get("/api/users/2/verify?")
                        .queryParam("certificationCode", "aaaaaaaa-aaa-aaa-aaaaaaa"))
                .andExpect(status().isFound());
//                .andExpect(jsonPath("$.id").value(2))
//                .andExpect(jsonPath("$.nickname").value("test2"))
//                .andExpect(jsonPath("$.nickname").value("ACTIVE"));

        UserEntity userEntity = userJpaRepository.findById(2L).get();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }


    @Test
    void 사용자는_내_정보를_불러올떄_개인정보인_주소도_가져온다() throws Exception {

        // Given
        // When
        // Then
        mockMvc.perform(get("/api/users/me")
                        .header("EMAIL", "test@naver.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nickname").value("test"))
                .andExpect(jsonPath("$.address").value("Seoul"));

    }

    @Test
    void 사용자는_내_정보를_수정할수있다() throws Exception {

        // Given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("test-n")
                .address("busan")
                .build();
        // When
        // Then
        mockMvc.perform(put("/api/users/me")
                        .header("EMAIL", "test@naver.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nickname").value("test-n"))
                .andExpect(jsonPath("$.address").value("busan"));

    }

    @Test
    void 사용자는_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(
                        get("/api/users/2/verify")
                                .queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaac"))
                .andExpect(status().isForbidden());
    }


}