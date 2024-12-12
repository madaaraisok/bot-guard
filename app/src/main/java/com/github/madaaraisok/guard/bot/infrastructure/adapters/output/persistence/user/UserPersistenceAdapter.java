package com.github.madaaraisok.guard.bot.infrastructure.adapters.output.persistence.user;

import com.github.madaaraisok.guard.bot.application.ports.output.UserOutputPort;
import com.github.madaaraisok.guard.bot.domain.model.common.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
class UserPersistenceAdapter implements UserOutputPort {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public User save(User user) {
        log.info("Saving user, email: {}", user.email());
        var userEntity = userPersistenceMapper.toUserEntity(user);
        userEntity = userRepository.save(userEntity);
        return userPersistenceMapper.toUser(userEntity);

    }

    public Optional<User> findByEmail(String email) {
        log.info("Finding user by email, email: {}", email);
        return this.userRepository.findByEmail(email)
                                  .map(userPersistenceMapper::toUser);
    }

    public long countBySanitizedEmail(String email) {
        log.info("Finding user by sanitized email, email: {}", email);
        return this.userRepository.countBySanitizedEmail(email);
    }

}
