package base;


import io.cucumber.datatable.DataTable;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;
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
    public RequestSpecification requestPutSetup(DataTable dataTable){
        baseURI = LoadProperties.getProperty("base.URL");
        CONTENT_TYPE = LoadProperties.getProperty("content.type");
        AUTHORIZATION = LoadProperties.getProperty("authorization");
        Map<String, String> row =
                dataTable.asMaps(String.class, String.class).get(0);

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("roomid", Integer.parseInt(row.get("roomid")));
        updateRequest.put("firstname", row.get("firstname"));
        updateRequest.put("lastname", row.get("lastname"));
        updateRequest.put("depositpaid", Boolean.valueOf(row.get("depositpaid")));

        updateRequest.put("bookingdates", Map.of(
                "checkin", row.get("checkin"),
                "checkout", row.get("checkout")
        ));
        return given().header("Content-Type", CONTENT_TYPE).accept(CONTENT_TYPE).header("Authorization", AUTHORIZATION).body(updateRequest);
    }

}
