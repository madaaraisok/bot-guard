package com.github.madaaraisok.guard.bot.application.ports.output;

import com.github.madaaraisok.guard.bot.domain.model.common.User;

import java.util.Optional;

public interface UserOutputPort {

    User save(User user);

    Optional<User> findByEmail(String email);

    long countBySanitizedEmail(String email);

}
