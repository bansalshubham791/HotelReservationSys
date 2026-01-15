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

    /**
     * generateRandomRoomId() method is used to generate random no.
     *
     * @return String
     */
    public String generateRandomRoomId() {

        final Random random = new Random();
        return String.valueOf(2000 + random.nextInt(900));
    }

    /**
     * requestGetSetup() method is used for creating request for getting the booking details.
     *
     * @return RequestSpecification
     */
    public RequestSpecification requestGetSetup() {
        baseURI = LoadProperties.getProperty("base.URL");
        CONTENT_TYPE = LoadProperties.getProperty("content.type");
        AUTHORIZATION = LoadProperties.getProperty("authorization");
        return given().header("Content-Type", CONTENT_TYPE).accept(CONTENT_TYPE).header("Authorization", AUTHORIZATION);
    }

    /**
     * requestPutSetup() is used for creating request for modifying the booking details.
     *
     * @param dataTable
     * @return RequestSpecification
     */
    public RequestSpecification requestPutSetup(DataTable dataTable) {
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

    /**
     * requestDeleteSetup() is used for creating request for deleting any booking.
     *
     * @return RequestSpecification
     */
    public RequestSpecification requestDeleteSetup() {
        baseURI = LoadProperties.getProperty("base.URL");
        CONTENT_TYPE = LoadProperties.getProperty("content.type");
        AUTHORIZATION = LoadProperties.getProperty("authorization");
        return given().header("Content-Type", CONTENT_TYPE).accept(CONTENT_TYPE).header("Authorization", AUTHORIZATION);
    }

    /**
     * @return RequestSpecification
     */
    public RequestSpecification requestGetRoomDetailsSetup() {
        baseURI = LoadProperties.getProperty("base.URL");
        CONTENT_TYPE = LoadProperties.getProperty("content.type");
        AUTHORIZATION = LoadProperties.getProperty("authorization");
        return given().header("Content-Type", CONTENT_TYPE).accept(CONTENT_TYPE).header("Authorization", AUTHORIZATION);
    }

    /**
     * @return RequestSpecification
     */
    public RequestSpecification requestGetRoomAvailabilitySetup() {
        baseURI = LoadProperties.getProperty("base.URL");
        CONTENT_TYPE = LoadProperties.getProperty("content.type");
        AUTHORIZATION = LoadProperties.getProperty("authorization");
        return given().header("Content-Type", CONTENT_TYPE).accept(CONTENT_TYPE).header("Authorization", AUTHORIZATION);
    }

    /**
     * @return RequestSpecification
     */
    public RequestSpecification requestGetReportSetup() {
        baseURI = LoadProperties.getProperty("base.URL");
        CONTENT_TYPE = LoadProperties.getProperty("content.type");
        AUTHORIZATION = LoadProperties.getProperty("authorization");
        return given().header("Content-Type", CONTENT_TYPE).accept(CONTENT_TYPE).header("Authorization", AUTHORIZATION);
    }

}
