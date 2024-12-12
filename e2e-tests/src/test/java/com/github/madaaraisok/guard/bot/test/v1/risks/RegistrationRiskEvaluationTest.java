package com.github.madaaraisok.guard.bot.test.v1.risks;

import com.github.madaaraisok.guard.bot.common.model.*;
import com.github.madaaraisok.guard.bot.test.TestCase;
import com.github.madaaraisok.guard.bot.test.v1.risks.model.RiskContext;
import com.github.madaaraisok.guard.bot.test.v1.risks.model.RiskRequestDto;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

class RegistrationRiskEvaluationTest implements TestCase {

    @Test
    void shouldEvaluateLowRiskWhenEmailIsNotTemporary() {
        var user = UserFactory.newUserWithEmailDomain("gmail.com");
        given()
                .spec(spec())
                .body(prepareRiskRequest(user))
                .when()
                .post("/v1/risks")
                .then()
                .statusCode(200)
                .body("risk", is(0.f));
    }

    @Test
    void shouldEvaluateHighRiskWhenEmailIsTemporary() {
        var user = UserFactory.newUserWithEmailDomain("blacklisted.domain.com");
        given()
                .spec(spec())
                .body(prepareRiskRequest(user))
                .when()
                .post("/v1/risks")
                .then()
                .statusCode(200)
                .body("risk", is(1.f));
    }

    @Test
    void shouldEvaluateHighRiskWhenEmailAliasIsUsed() {
        var emailDomain = UserFactory.randomEmailDomain();
        Stream.of("t.esttest", "te.sttest", "tes.ttest", "test.test").forEach(name -> {
            var user = UserFactory.newUserWithEmail(name, emailDomain);
            given()
                .spec(spec())
                .body(prepareRiskRequest(user))
                .when()
                .post("/v1/risks")
                .then()
                .statusCode(200);
        });

        var user = UserFactory.newUserWithEmail("testtest", emailDomain);
        given()
            .spec(spec())
            .body(prepareRiskRequest(user))
            .when()
            .post("/v1/risks")
            .then()
            .statusCode(200)
            .body("risk", is(1.f));
    }

    private RiskRequestDto prepareRiskRequest(User user) {
        return new RiskRequestDto(
                new RiskContext(IpConstants.WARSAW_IP),
                EventType.REGISTRATION,
                EventStatus.SUCCEEDED,
            user
        );
    }

}
