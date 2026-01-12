package stepdefinitions;

import base.BookingDates;
import base.BookingRequest;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class HotelReservationStepDef {
    BookingDates dates;
    String requestBody;
    int bookingId;

    private Response loginResponse;
    private Response bookingResponse;
    protected String authToken;
    private RequestSpecification bookingSpec;
    private Map<String, String> bookingRequest;
    private RequestSpecification loginRequest;


    @Given("the user submits valid login credentials:")
    public void the_user_submits_valid_login_credentials(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", data.get("username"));
        requestBody.put("password", data.get("password"));
        //System.out.println(requestBody.toString());
        loginRequest = given().body(requestBody.toString());
        loginRequest.header("Authorization", "automationintesting.online");
        loginRequest.header("Content-Type", "application/json");
    }
    @Then("the system should authenticate the user")
    public void theSystemShouldAuthenticateTheUser() {
        loginResponse = loginRequest.when().post("https://automationintesting.online/api/auth/login");
        authToken = loginResponse.getBody().jsonPath().getString("token");
        System.out.println(authToken);
        // loginResponse.then().log().all();
    }
    @And("the user should receive a valid session")
    public void theUserShouldReceiveAValidSession() {
        bookingSpec = given()
                .cookie("token", authToken);
    }

    @Given("the user enters guest details")
    public void theUserEntersGuestDetails(DataTable dataTable) {
        bookingRequest = new HashMap<>();

        List<Map<String, String>> rows =
                dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.get(0);

        // Build bookingdates object
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", row.get("checkin"));
        bookingDates.put("checkout", row.get("checkout"));

        bookingRequest.put("firstname", row.get("firstname"));
        bookingRequest.put("lastname", row.get("lastname"));
        bookingRequest.put("email", row.get("email"));
        bookingRequest.put("phone", row.get("phone"));
        bookingRequest.put("roomType", row.get("roomType"));
        bookingRequest.put("bookingDates", row.get("bookingDates"));

        JSONObject jsonObject = new JSONObject(bookingRequest);
        if (bookingSpec == null){
            bookingSpec = given();
        }
        bookingSpec.body(jsonObject);

        bookingSpec.queryParam("checkin", row.get("checkin"));
        bookingSpec.queryParam("checkout", row.get("checkout"));
        bookingSpec.header("Authorization", "automationintesting.online");
        bookingSpec.header("Content-Type", "application/json");
        System.out.println(bookingSpec);
        //bookingSpec.body(bookingRequest);
    }

    @And("the user submits the booking")
    public void theUserSubmitsTheBooking() {
        bookingResponse = bookingSpec.when().post("https://automationintesting.online/api/booking");
    }

    @Then("the booking should be successfully created with response code {int}")
    public void theBookingShouldBeSuccessfullyCreatedWithResponseCode(Integer expectedStatus) {
        bookingResponse.then().log().all();
        assertEquals(bookingResponse.getStatusCode(), expectedStatus.intValue(),
                "Unexpected status code");
    }

    @And("a confirmation with booking ID is displayed")
    public void aConfirmationWithBookingIDIsDisplayed() {
        assertTrue(bookingResponse.getBody().asString().contains("bookingid"),
                "Response does not indicate missing parameter. Actual response: " + bookingResponse.getBody().asString());
    }
}
