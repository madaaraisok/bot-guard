package com.github.madaaraisok.guard.bot.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventType {

    @JsonProperty("login")
    LOGIN,

    @JsonProperty("registration")
    REGISTRATION
}
