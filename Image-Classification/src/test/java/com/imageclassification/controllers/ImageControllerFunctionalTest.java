package com.imageclassification.controllers;

import com.imageclassification.dtos.ImageDTO;
import com.imageclassification.models.Image;
import com.imageclassification.models.Tag;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class ImageControllerFunctionalTest {
    private static RequestSpecBuilder builder;
    private static RequestSpecification reqSpec;

    @BeforeAll
    static void setUp() {
        builder = new RequestSpecBuilder();
        builder.setBaseUri("http://localhost");
        builder.setContentType(ContentType.JSON);
        reqSpec = builder.build();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void testFetchImageTags() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("url", "https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg");
        given()
                .spec(reqSpec)
                .body(json.toString())
                .when()
                .post("/images")
                .then()
                .statusCode(201);
    }

    @Test
    public void testFetchImageTagsWithNoCache() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("url", "https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg");
        given()
                .spec(reqSpec)
                .queryParam("noCache", true)
                .body(json.toString())
                .when()
                .post("/images")
                .then()
                .statusCode(201);
    }

    @Test
    public void testFetchInvalidImageTags() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("url", "https://invalidimage.jpg");
        given()
                .spec(reqSpec)
                .body(json.toString())
                .when()
                .post("/images")
                .then()
                .statusCode(400);
    }

    @Test
    public void testPostImageAndThenGetImageById() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("url", "https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg");
        given()
                .spec(reqSpec)
                .queryParam("noCache", true)
                .body(json.toString())
                .when()
                .post("/images ")
                .then()
                .statusCode(201);

        Long id = 1L;
        given()
                .pathParam("imageId", id)
                .when()
                .get("/images/{imageId}")
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void testGetImageByInvalidId() {
        Long id = -1L;
        given()
                .pathParam("imageId", id)
                .when()
                .get("/images/{imageId}")
                .prettyPeek()
                .then()
                .assertThat()
                .statusCode(404);
    }

    @Test
    public void testGetAllImagesPaged() {
        given()
                .queryParam("order", "asc")
                .queryParam("pageNumber", 0)
                .queryParam("pageSize", 10)
                .when()
                .get("/images")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0))
                .body("size()", lessThanOrEqualTo(10));
    }

    @Test
    public void testGetAllImagesWithoutPaging() {
        given()
                .when()
                .get("/images")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    public void testGetAllImagesWithPagingInvalidPageNumber() {
        given()
                .queryParam("order", "asc")
                .queryParam("pageNumber", -1)
                .queryParam("pageSize", 5)
                .when()
                .get("/images")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetAllImagesWithPagingInvalidOrder() {
        given()
                .queryParam("order", "invalid")
                .queryParam("pageNumber", 5)
                .queryParam("pageSize", 10)
                .when()
                .get("/images")
                .then()
                .statusCode(400);
    }

    @Test
    public void testGetAllImagesWithValidTags() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("url", "https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg");
        given()
                .spec(reqSpec)
                .queryParam("noCache", true)
                .body(json.toString())
                .when()
                .post("/images")
                .then()
                .statusCode(201);

        List<String> tags = List.of("mountains", "landscape");
        given()
                .queryParam("tags", tags)
                .when()
                .get("/images/tags")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    public void testGetAllImagesWithEmptyTagsShouldReturnEmptyList() {
        given()
                .queryParam("tags", List.of())
                .when()
                .get("/images/tags")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

}