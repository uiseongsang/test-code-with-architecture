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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void UserRepository가_제대로_연결되었다() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@gmail.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("thomas");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaa-aaaaa-aaaa-aaa-aaaaa");

        // When
        UserEntity res = userRepository.save(userEntity);
    	
        // Then
        assertThat(res.getId()).isNotNull();
    }

    @Test
    void findByIdAndStatus로_유저_데이터를_찾아올_수_있다() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@gmail.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("thomas");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaa-aaaaa-aaaa-aaa-aaaaa");

        // When
        userRepository.save(userEntity);
        Optional<UserEntity> res = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        // Then
        assertThat(res.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus로_유저_데이터가_없으면_Optional_empty_를_내려준다() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@gmail.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("thomas");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaa-aaaaa-aaaa-aaa-aaaaa");

        // When
        userRepository.save(userEntity);
        Optional<UserEntity> res = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        // Then
        assertThat(res.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus로_유저_데이터를_찾아올_수_있다() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@gmail.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("thomas");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaa-aaaaa-aaaa-aaa-aaaaa");

        // When
        userRepository.save(userEntity);
        Optional<UserEntity> res = userRepository.findByEmailAndStatus("test@gmail.com", UserStatus.ACTIVE);

        // Then
        assertThat(res.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus로_유저_데이터가_없으면_Optional_empty_를_내려준다() {
        // Given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("test@gmail.com");
        userEntity.setAddress("Seoul");
        userEntity.setNickname("thomas");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaaa-aaaaa-aaaa-aaa-aaaaa");

        // When
        userRepository.save(userEntity);
        Optional<UserEntity> res = userRepository.findByEmailAndStatus("test@gmail.com", UserStatus.PENDING);

        // Then
        assertThat(res.isEmpty()).isTrue();
    }
}
