package com.imageclassification.controllers;

import com.imageclassification.controllers.actors.FetchImageE2EActor;
import com.imageclassification.models.Image;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ImageControllerE2ETest {

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
    /**
     * Test steps:
     * fetch an image
     * fetch another image
     * get all images and assert whether those two are in the list
     * get all images with the tags of one of the images and validate if it is in the list
     */
    public void testFetchImageE2E() {
        FetchImageE2EActor actor = new FetchImageE2EActor(reqSpec);
        Image firstImage = actor.fetchImageTags("https://mcdn.wallpapersafari.com/medium/71/45/ZFKajr.jpg");
        Image getFirstImageWithId = actor.getImageById(firstImage.getId());
        Assertions.assertEquals(firstImage.getChecksum(), getFirstImageWithId.getChecksum());
        Assertions.assertEquals(firstImage.getUrl(), getFirstImageWithId.getUrl());

        Image secondImage = actor.fetchImageTags("https://docs.imagga.com/static/images/docs/sample/japan-605234_1280.jpg");
        Image getSecondImageWithId = actor.getImageById(secondImage.getId());
        Assertions.assertEquals(secondImage.getChecksum(), getSecondImageWithId.getChecksum());
        Assertions.assertEquals(secondImage.getUrl(), getSecondImageWithId.getUrl());
    }

}
