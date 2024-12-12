package com.github.madaaraisok.guard.bot.infrastructure.adapters.input.rest.filter;

import com.github.madaaraisok.guard.bot.application.dto.filter.FilterEvaluationResponseDto;
import com.github.madaaraisok.guard.bot.application.ports.input.FilterEvaluationUseCase;
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

@WebMvcTest(FilterEvaluationRestAdapter.class)
class FilterEvaluationRestAdapterMvcTest {

    @MockitoBean
    private FilterEvaluationUseCase filterEvaluationUseCase;

    @MockitoBean
    private FilterEvaluationMapper filterEvaluationMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldEvaluateFilter() throws Exception {
        // given
        var id = UUID.randomUUID();
        var createdAt = Date.from(Instant.parse("2021-10-10T10:00:00Z"));

        given(filterEvaluationMapper.toFilterEvaluationResponse(any()))
            .willReturn(prepareResponse(id, 0.0, createdAt));

        // when and then
        mockMvc.perform(post("/v1/filters")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(
                       """
                           {
                               "context": {
                                   "ip": "192.168.0.1"
                               },
                               "type": "login"
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
    void shouldReturnErrorWhenTypeIsNotProvided() throws Exception {
        // when and then
        mockMvc.perform(post("/v1/filters")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(
                       """
                           {
                               "context": {
                                   "ip": "192.168.0.1"
                               }
                           }
                           """
                   ))
               .andExpect(status().is4xxClientError());
    }

    private FilterEvaluationResponseDto prepareResponse(UUID id, Double risk, Date createdAt) {
        return FilterEvaluationResponseDto.builder()
                                          .id(id)
                                          .type(EventType.LOGIN)
                                          .risk(risk)
                                          .createdAt(createdAt)
                                          .build();
    }

}