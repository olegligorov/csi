package com.imageclassification.controllers.actors;

import com.imageclassification.models.Image;
import com.imageclassification.models.Tag;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class FetchImageE2EActor {
    private RequestSpecification specification;

    public FetchImageE2EActor(RequestSpecification specification) {
        this.specification = specification;
    }

    public Image fetchImageTags(String imageUrl) {
        JSONObject json = new JSONObject();
        try {
            json.put("url", imageUrl);
            return given()
                    .spec(specification)
                    .queryParam("noCache", true)
                    .body(json.toString())
                    .when()
                    .post("/images")
                    .as(Image.class);
        } catch (JSONException e) {
            return null;
        }
    }

    public Image getImageById(Long imageId) {
        return given()
                .pathParam("imageId", imageId)
                .when()
                .get("/images/{imageId}")
                .as(Image.class);
    }

    public List<Image> getAllImages() {
        List<Image> images = Arrays.stream(given()
                .when()
                .get("/images")
                .as(Image[].class)).toList();
        return images;
    }

    public List<Image> getAllImagesWithFetchedTags(Collection<Tag> tagCollection) {
        List<Image> images = Arrays.stream(given()
//                .spec(requestSpecification)
                .queryParam("tags", tagCollection)
                .when()
                .get("/images/tags")
                .as(Image[].class)).toList();
        return images;
    }
}
