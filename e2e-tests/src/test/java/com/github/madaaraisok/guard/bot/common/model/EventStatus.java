package com.github.madaaraisok.guard.bot.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum EventStatus {

    @JsonProperty("succeeded")
    SUCCEEDED,

    @JsonProperty("failed")
    FAILED
}
