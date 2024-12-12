package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.user;

import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import static org.assertj.core.api.Assertions.assertThat;

@EnableJpaAuditing
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository underTest;

    @Test
    void shouldFindUserByEmail() {
        // given
        var email = "test@example.com";
        var userEntity = UserEntity.builder()
                                   .email(email)
                                   .sanitizedEmail(email)
                                   .build();

        underTest.save(userEntity);

        // when
        var result = underTest.findByEmail(email);

        // then
        assertThat(result).isPresent()
                          .get()
                          .extracting("email")
                          .isEqualTo(email);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        // given
        var email = "nonexistent@example.com";

        // when
        var result = underTest.findByEmail(email);

        // then
        assertThat(result).isNotPresent();
    }

}