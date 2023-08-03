package com.example.demo.repository;

import com.example.demo.model.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdAndStatus로_유저_데이터를_찾아올_수_있다() {
        // Given
        // When
        Optional<UserEntity> res = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        // Then
        assertThat(res.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus로_유저_데이터가_없으면_Optional_empty_를_내려준다() {
        // Given
        // When
        Optional<UserEntity> res = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        // Then
        assertThat(res.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus로_유저_데이터를_찾아올_수_있다() {
        // Given
        // When
        Optional<UserEntity> res = userRepository.findByEmailAndStatus("test@gmail.com", UserStatus.ACTIVE);

        // Then
        assertThat(res.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus로_유저_데이터가_없으면_Optional_empty_를_내려준다() {
        // Given
        // When
        Optional<UserEntity> res = userRepository.findByEmailAndStatus("test@gmail.com", UserStatus.PENDING);

        // Then
        assertThat(res.isEmpty()).isTrue();
    }
}
