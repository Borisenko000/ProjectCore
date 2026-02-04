package gatling;


import com.github.javafaker.Faker;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;


public class UserCreatingSimulation extends Simulation {

    private static final HttpProtocolBuilder HTTP_PROTOCOL_BUILDER = setupProtocolForSimulation();

    private static final Iterator<Map<String, Object>> FEED_DATA = setupTestFeedData();

    private static final ChainBuilder GET_SCENARIO_CHAIN_BUILDER = getScenario();
    private static final ChainBuilder POST_SCENARIO_CHAIN_BUILDER = postScenario();
    private static final ChainBuilder DELETE_SCENARIO_CHAIN_BUILDER = deleteScenario();

    private static final ScenarioBuilder FULL_SCENARIO_BUILDER = buildFullScenario();
    private static final ScenarioBuilder RATE_LIMIT_SCENARIO_BUILDER = buildRateLimitScenario();

    {
        setUp(
                FULL_SCENARIO_BUILDER.injectOpen(
                        rampUsersPerSec(1).to(5).during(60),
                        constantUsersPerSec(5).during(60),
                        rampUsersPerSec(5).to(1).during(60)
                ),
                RATE_LIMIT_SCENARIO_BUILDER.injectOpen(
                        nothingFor(180),
                        constantUsersPerSec(10).during(60)
                )
        ).protocols(HTTP_PROTOCOL_BUILDER);
    }

    private static HttpProtocolBuilder setupProtocolForSimulation() {
        return http
                .baseUrl("http://localhost:8080")
                .contentTypeHeader("application/json")
                .acceptHeader("application/json");
    }

    private static Iterator<Map<String, Object>> setupTestFeedData() {
        Faker faker = new Faker();
        Iterator<Map<String, Object>> iterator;
        iterator = Stream.generate(() -> {
                    Map<String, Object> stringObjectMap = new HashMap<>();
                    stringObjectMap.put("name", faker.name().username());
                    stringObjectMap.put("password", faker.internet().password(8, 16));
                    return stringObjectMap;
                })
                .iterator();
        return iterator;
    }

    private static ChainBuilder postScenario() {
        ChainBuilder createUser = feed(FEED_DATA)
                .exec(
                        http("POST /users- create")
                                .post("/users")
                                .body(StringBody("{" +
                                        "\"name\": \"${name}\"," +
                                        "\"password\": \"${password}\"" +
                                        "}")).asJson()
                                .check(status().in(200, 201))
                                .check(jsonPath("$.id").saveAs("id"))
                )
                .exec(session -> {
                    return session;
                });
        return createUser;
    }

    private static ChainBuilder getScenario() {
        ChainBuilder getUser100Times =
                doIf(session -> session.contains("id")).then(
                        repeat(100, "i").on(
                                exec(
                                        http("GET /users/{id} - repeat #{i}")
                                                .get(session -> "/users/" + session.getString("id"))
                                                .check(status().is(200))
                                )
                        )
                );
        return getUser100Times;
    }

    private static ChainBuilder deleteScenario() {
        ChainBuilder deleteUser =
                doIf(session -> session.contains("id")).then(
                        exec(

                                http("DELETE /users/{id} ")
                                        .delete(session -> "/users/" + session.getString("id"))
                                        .check(status().in(200, 204))
                        )
                );
        return deleteUser;
    }

    private static ScenarioBuilder buildFullScenario() {
        return scenario("buld CREATE,GET 100 TIMES,DELETE builder").exec(POST_SCENARIO_CHAIN_BUILDER)
                .exec(GET_SCENARIO_CHAIN_BUILDER)
                .exec(DELETE_SCENARIO_CHAIN_BUILDER);
    }

    private static ScenarioBuilder buildRateLimitScenario() {
        ChainBuilder rateLimitBuilder = feed(FEED_DATA)
                .exec(
                        http("RATE-POST /users")
                                .post("/users")
                                .header("X-API-KEY", "Polina")
                                .body(StringBody("{" +
                                        "\"name\": \"${name}\"," +
                                        "\"password\": \"${password}\"" +
                                        "}")).asJson()
                                .check(status().in(200, 201, 429))
                                .check(jsonPath("$.id").optional().saveAs("id"))
                );

        return scenario("RATE LIMIT : POST as one client").exec(rateLimitBuilder);
    }

}
