package com.github.madaaraisok.guard.bot.infrastructure.adapters.input.rest.risk;

import com.github.madaaraisok.guard.bot.application.dto.risk.RiskEvaluationResponseDto;
import com.github.madaaraisok.guard.bot.application.ports.input.RiskEvaluationUseCase;
import com.github.madaaraisok.guard.bot.domain.model.common.EventType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RiskEvaluationRestAdapter.class)
class RiskEvaluationRestAdapterWebTest {

    @MockitoBean
    private RiskEvaluationUseCase riskEvaluationUseCase;

    @MockitoBean
    private RiskEvaluationMapper riskEvaluationMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldEvaluateRisk() throws Exception {
        // given
        var id = UUID.randomUUID();
        var createdAt = Date.from(Instant.parse("2021-10-10T10:00:00Z"));

        given(riskEvaluationMapper.toRiskEvaluationResponse(any()))
            .willReturn(prepareResponse(id, createdAt));

        // when and then
        mockMvc.perform(post("/v1/risks")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(
                       """
                           {
                               "context": {
                                   "ip": "192.168.0.1",
                                   "headers": [
                                       {
                                           "name": "User-Agent",
                                           "value": "Mozilla/5.0"
                                       }
                                   ]
                               },
                               "type": "login",
                               "status": "succeeded",
                               "user": {
                                   "email": "user@domain.com"
                               }
                           }
                           """
                   ))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", equalTo(id.toString())))
               .andExpect(jsonPath("$.type", equalTo("login")))
               .andExpect(jsonPath("$.risk", equalTo(0.0)))
               .andExpect(jsonPath("$.createdAt", equalTo("2021-10-10T10:00:00.000+00:00")));
    }

    @Test
    void shouldValidEmail() throws Exception {
        // when and then
        mockMvc.perform(post("/v1/risks")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(
                       """
                           {
                               "context": {
                                   "ip": "192.168.0.1",
                                   "headers": [
                                       {
                                           "name": "User-Agent",
                                           "value": "Mozilla/5.0"
                                       }
                                   ]
                               },
                               "type": "login",
                               "status": "succeeded",
                               "user": {
                                   "email": "@domain.com"
                               }
                           }
                           """
                   ))
               .andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnErrorWhenTypeIsNotProvided() throws Exception {
        // when and then
        mockMvc.perform(post("/v1/risks")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(
                       """
                           {
                               "context": {
                                   "ip": "192.168.0.1"
                               },
                               "status": "succeeded",
                               "user": {
                                   "email": "user@domain.com"
                               }
                           }
                           """
                   ))
               .andExpect(status().is4xxClientError());
    }

    private RiskEvaluationResponseDto prepareResponse(UUID id, Date createdAt) {
        return RiskEvaluationResponseDto.builder()
                                        .id(id)
                                        .type(EventType.LOGIN)
                                        .risk(0.0)
                                        .createdAt(createdAt)
                                        .build();
    }

}