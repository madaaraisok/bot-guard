package com.github.madaaraisok.guard.bot.domain.model.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventStatus {

    @JsonProperty("succeeded")
    SUCCEEDED,

    @JsonProperty("failed")
    FAILED
}