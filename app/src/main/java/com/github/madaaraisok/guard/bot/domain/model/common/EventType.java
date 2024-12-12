package com.github.madaaraisok.guard.bot.domain.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {

    @JsonProperty("login")
    LOGIN,

    @JsonProperty("registration")
    REGISTRATION
}
