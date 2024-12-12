package com.github.madaaraisok.guard.bot.test.v1.filters;

import com.github.madaaraisok.guard.bot.common.model.EventType;
import com.github.madaaraisok.guard.bot.test.v1.filters.model.FilterContextDto;
import com.github.madaaraisok.guard.bot.test.v1.filters.model.FilterEvaluationRequestDto;
import com.github.madaaraisok.guard.bot.common.model.IpConstants;
import com.github.madaaraisok.guard.bot.test.TestCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

class FilterEvaluationTest implements TestCase {

    @Test
    void shouldEvaluateFilterWhenLocationIsSame() {
        var request = prepareFilterEvaluationRequest(IpConstants.WARSAW_IP);

        for (int i = 1; i <= 5; i++) {
            given()
                .spec(spec())
                .body(request)
                .when()
                .post("/v1/filters")
                .then()
                .statusCode(200)
                .body("risk", is(expectedRisk(i)));
        }
    }

    @Test
    void shouldEvaluateFilterWhenLocationIsDifferent() {
        given()
            .spec(spec())
            .body(prepareFilterEvaluationRequest(IpConstants.KRAKOW_IP))
            .when()
            .post("/v1/filters")
            .then()
            .statusCode(200)
            .body("risk", is(0.01f));

        given()
            .spec(spec())
            .body(prepareFilterEvaluationRequest(IpConstants.POZNAN_IP))
            .when()
            .post("/v1/filters")
            .then()
            .statusCode(200)
            .body("risk", is(0.01f));
    }

    private float expectedRisk(int i) {
        return BigDecimal.valueOf(i).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN).floatValue();
    }

    private FilterEvaluationRequestDto prepareFilterEvaluationRequest(String ip) {
        return new FilterEvaluationRequestDto(
            new FilterContextDto(ip),
            EventType.LOGIN
        );
    }

}
