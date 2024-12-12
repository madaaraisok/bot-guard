package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.user;

import com.github.madaaraisok.guard.bot.domain.model.common.User;
import com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.user.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserPersistenceAdapterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserPersistenceMapper userPersistenceMapper;

    @InjectMocks
    private UserPersistenceAdapter underTest;

    @Test
    void shouldSaveUser() {
        // given
        var user = new User(UUID.randomUUID(), "test@example.com");
        var userEntity = new UserEntity();

        given(userPersistenceMapper.toUserEntity(user)).willReturn(userEntity);
        given(userRepository.save(userEntity)).willReturn(userEntity);
        given(userPersistenceMapper.toUser(userEntity)).willReturn(user);

        // when
        var result = underTest.save(user);

        // then
        assertThat(result).isEqualTo(user);

        verify(userRepository).save(userEntity);
    }

    @Test
    void shouldFindUserByEmail() {
        // given
        var email = "test@example.com";
        var userEntity = new UserEntity();
        var user = new User(UUID.randomUUID(), email);

        given(userRepository.findByEmail(email)).willReturn(Optional.of(userEntity));
        given(userPersistenceMapper.toUser(userEntity)).willReturn(user);

        // when
        var result = underTest.findByEmail(email);

        // then
        assertThat(result).isPresent().contains(user);

        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        // given
        var email = "test@example.com";

        given(userRepository.findByEmail(email)).willReturn(Optional.empty());

        // when
        var result = underTest.findByEmail(email);

        // then
        assertThat(result).isEmpty();

        verify(userRepository).findByEmail(email);
    }

    @Test
    void shouldCountBySanitizedEmail() {
        // given
        var email = "user@example.com";

        given(userRepository.countBySanitizedEmail(email)).willReturn(7L);

        // when
        long result = underTest.countBySanitizedEmail(email);

        // then
        assertThat(result).isEqualTo(7);
    }

}