package com.imageclassification.controllers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class TagControllerFunctionalTest {
    private static RequestSpecBuilder builder;
    private static RequestSpecification reqSpec;

    @BeforeEach
    void setUp() {
        builder = new RequestSpecBuilder();
        builder.setBaseUri("http://localhost:8080");
        builder.setContentType(ContentType.JSON);
        reqSpec = builder.build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void testGetAllTags() {
        given()
                .spec(reqSpec)
                .when()
                .get("/tags")
                .then()
                .statusCode(200);
    }

    @Test
    public void testPushImageAndGetAllTagsShouldBeGreaterThanZero() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("url", "https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg");
        given()
                .spec(reqSpec)
                .body(json.toString())
                .when()
                .post("/images")
                .then()
                .statusCode(201);

        given()
                .spec(reqSpec)
                .when()
                .get("/tags")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

}
