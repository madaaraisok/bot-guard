package com.github.madaaraisok.guard.bot.test.v1.risks;

import com.github.madaaraisok.guard.bot.common.model.*;
import com.github.madaaraisok.guard.bot.test.TestCase;
import com.github.madaaraisok.guard.bot.test.v1.risks.model.RiskContext;
import com.github.madaaraisok.guard.bot.test.v1.risks.model.RiskRequestDto;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

class LoginRiskEvaluationTest implements TestCase {

    @Test
    void shouldEvaluateLowRiskWhenLocationIsSameTwice() {
        var user = UserFactory.newUser();
        var requestFromWarsaw = prepareRiskRequest(user, IpConstants.WARSAW_IP);

        given()
                .spec(spec())
                .body(requestFromWarsaw)
                .when()
                .post("/v1/risks")
                .then()
                .statusCode(200)
                .body("risk", is(0.f));

        given()
                .spec(spec())
                .body(requestFromWarsaw)
                .when()
                .post("/v1/risks")
                .then()
                .statusCode(200)
                .body("risk", is(0.f));
    }

    @Test
    void shouldEvaluateHighRiskForImpossibleTravel() {
        var user = UserFactory.newUser();
        var requestFromWarsaw = prepareRiskRequest(user, IpConstants.WARSAW_IP);
        var requestFromKrakow = prepareRiskRequest(user, IpConstants.KRAKOW_IP);

        given()
                .spec(spec())
                .body(requestFromWarsaw)
                .when()
                .post("/v1/risks")
                .then()
                .statusCode(200)
                .body("risk", is(0.f));

        given()
                .spec(spec())
                .body(requestFromKrakow)
                .when()
                .post("/v1/risks")
                .then()
                .statusCode(200)
                .body("risk", is(1.f));
    }

    private RiskRequestDto prepareRiskRequest(User user, String ip) {
        return new RiskRequestDto(
                new RiskContext(ip),
                EventType.LOGIN,
                EventStatus.SUCCEEDED,
                user
        );
    }

}
