package com.example.demo.service;

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
// sql 파일을 여러 개 지정해서 실행가능
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender mailSender;

    @Test
    void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // Given
        String email = "test@gmail.com";

        // When
        UserEntity res = userService.getByEmail(email);

        // Then
        assertThat(res.getNickname()).isEqualTo("thomas");
    }

    @Test
    void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
        // Given
        String email = "test1@gmail.com";

        // When
        // Then
        assertThatThrownBy(() -> {
            userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getById은_ACTIVE_상태인_유저를_찾아올_수_있다() {
        // Given
        // When
        UserEntity res = userService.getById(1);

        // Then
        assertThat(res.getNickname()).isEqualTo("thomas");
    }

    @Test
    void getById은_PENDING_상태인_유저를_찾아올_수_없다() {
        // Given
        // When
        // Then
        assertThatThrownBy(() -> {
            userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void userCreateDto를_이용해서_유저를_생성할_수있다() {
        // Given
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("test2@gmail.com")
                .address("Daegu")
                .nickname("Huang")
                .build();

        // org.springframework.mail.MailAuthenticationException: Authentication failed
        // 이메일 발송 실패로 Mockito를 이용해서 해결
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // When
        UserEntity res = userService.create(userCreateDto);

        // Then
        assertThat(res.getId()).isNotNull();
        assertThat(res.getStatus()).isEqualTo(UserStatus.PENDING);
        // assertThat(res.getCertificationCode()).isEqualTo("???") // FIXME

    }

    @Test
    void userCreateDto를_이용해서_유저를_수정할_수있다() {
        // Given
        UserUpdateDto userUpdateDto = UserUpdateDto.builder()
                .address("Gumi")
                .nickname("Huang-SW")
                .build();

        // When
        UserEntity res = userService.update(1,userUpdateDto);

        // Then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("Gumi");
        assertThat(userEntity.getNickname()).isEqualTo("Huang-SW");
        assertThat(userEntity.getEmail()).isEqualTo("test@gmail.com");
    }

    @Test
    void user를_로그인_시키면_마지막_로그인_시간이_변경된다() {
        // Given
        // When
        userService.login(1);

        // Then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
        // assertThat(res.getCertificationCode()).isEqualTo("???") // FIXME
    }

    @Test
    void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
        // Given
        // When
        userService.verifyEmail(2,"aaaaaaa-aaaaa-aaaa-aaa-aaaab");

        // Then
        UserEntity userEntity = userService.getById(2);
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING_상태의_사용자는_잘못된_인증_코드로_받으면_에러를_던진다() {
        // Given
        // When
        // Then
        assertThatThrownBy( () ->
                userService.verifyEmail(2,"aaaaaaa-aaaaa-aaaa-aaa-aaaaa")
                ).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}
