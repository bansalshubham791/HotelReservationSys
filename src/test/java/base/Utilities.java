package base;


import io.restassured.specification.RequestSpecification;

import java.util.Random;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.*;

public class Utilities {
    String CONTENT_TYPE;
    String AUTHORIZATION;
    public String generateRandomRoomId() {

        final Random random = new Random();
        return String.valueOf(2000 + random.nextInt(900));
    }

    public RequestSpecification requestGetSetup() {
        baseURI = LoadProperties.getProperty("base.URL");
        //basePath = LoadProperties.getProperty("get.booking.path");
        CONTENT_TYPE = LoadProperties.getProperty("content.type");
        AUTHORIZATION = LoadProperties.getProperty("authorization");
        return given().header("Content-Type", CONTENT_TYPE).accept(CONTENT_TYPE).header("Authorization", AUTHORIZATION);
    }

}
