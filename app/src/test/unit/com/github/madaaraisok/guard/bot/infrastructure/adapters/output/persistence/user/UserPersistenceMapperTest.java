package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.user;

import com.github.madaaraisok.guard.bot.domain.model.common.User;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.user.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserPersistenceMapperTest {

    private final UserPersistenceMapper underTest = new UserPersistenceMapperImpl();

    @Test
    void shouldMapUserEntityToUser() {
        // given
        var id = UUID.randomUUID();
        var email = "user+1@example.com";
        var sanitizedEmail = "user@example.com";
        var userEntity = new UserEntity(id, email, sanitizedEmail, new Date(), new Date());

        // when
        var result = underTest.toUser(userEntity);

        // then
        assertThat(result).extracting(User::id, User::email)
                          .containsExactly(id, email);

    }

    @Test
    void shouldMapUserToUserEntity() {
        // given
        var id = UUID.randomUUID();
        var email = "user+1@example.com";
        var user = new User(id, email);

        // when
        var result = underTest.toUserEntity(user);

        // then
        assertThat(result).extracting(UserEntity::getId, UserEntity::getEmail)
                          .containsExactly(id, email);
    }

}