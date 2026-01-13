package stepdefinitions;

import base.BookingDates;
import base.BookingRequest;
import base.Utilities;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static io.restassured.RestAssured.*;
import static org.testng.AssertJUnit.assertNotNull;

public class HotelReservationStepDef extends Utilities {

    private Response loginResponse;
    private Response bookingResponse;
    protected String authToken;
    private RequestSpecification bookingSpec;
    private RequestSpecification loginRequest;


    @Given("the user submits valid login credentials:")
    public void the_user_submits_valid_login_credentials(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", data.get("username"));
        requestBody.put("password", data.get("password"));

        loginRequest = given().body(requestBody.toString());
        loginRequest.header("Authorization", "automationintesting.online");
        loginRequest.header("Content-Type", "application/json");
    }
    @Then("the system should authenticate the user")
    public void theSystemShouldAuthenticateTheUser() {
        loginResponse = loginRequest.when().post("https://automationintesting.online/api/auth/login");
        authToken = loginResponse.getBody().jsonPath().getString("token");
         loginResponse.then().log().all();
    }
    @And("the user should receive a valid session")
    public void theUserShouldReceiveAValidSession() {
        bookingSpec = given()
                .cookie("token", authToken);
       // System.out.println(bookingSpec.cookie("token").toString());
    }

    @Given("the user enters guest details")
    public void theUserEntersGuestDetails(DataTable dataTable) {

        List<Map<String, String>> rows =
                dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.get(0);

        JSONObject bookingRequestBody = new JSONObject();

        // Build bookingdates object
        JSONObject bookingdates = new JSONObject();

        bookingdates.put("checkin", row.get("checkin"));
        bookingdates.put("checkout", row.get("checkout"));

        bookingRequestBody.put("firstname", row.get("firstname"));
        bookingRequestBody.put("lastname", row.get("lastname"));
        bookingRequestBody.put("email", row.get("email"));
        bookingRequestBody.put("phone", row.get("phone"));
        bookingRequestBody.put("roomid", Integer.parseInt(row.get("roomid")));
        bookingRequestBody.put("bookingdates", bookingdates);
        bookingRequestBody.put("depositpaid", Boolean.parseBoolean(row.get("depositpaid")));

        //JSONObject jsonObject = new JSONObject(bookingRequest);
        if (bookingSpec == null){
            bookingSpec = given();
        }
        bookingSpec = bookingSpec.body(bookingRequestBody.toString());

        bookingSpec.header("Authorization", "automationintesting.online");
        bookingSpec.header("Content-Type", "application/json");

    }

    @And("the user submits the booking")
    public void theUserSubmitsTheBooking() {
       // baseURI = "https://automationintesting.online/";

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

    @Then("validate the response with json schema {string}")
    public void validate_the_response_with_json_schema(String schemaFileName) {
        bookingResponse.then().log().body();
        bookingResponse.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaFileName));
    }

    @Given("the user wants to check the room details")
    public void theUserWantsToCheckTheRoomDetails() {
        bookingSpec = requestGetSetup().cookie("token", authToken);

       /* bookingSpec = given()
                .header("Content-Type", "application/json")
                .header("Authorization", "automationintesting.online")
                .cookie("token", authToken);*/
    }

    @When("the user asks the details of the room by:")
    public void theUserAsksTheDetailsOfTheRoomByRoomIdRoomid(String roomid) {
        bookingResponse = bookingSpec.when().get("https://automationintesting.online/api/booking/"+roomid);
        bookingResponse.then().log().body();
        System.out.println(bookingResponse.body().toString());
    }

    @Then("details of the room is available:")
    public void detailsOfTheRoomIsAvailable(DataTable dataTable) {
        List<String> expectedFields = dataTable.asList();
        for (String field : expectedFields) {
            assertNotNull(
                    bookingResponse.jsonPath().get(field),
                    field + " should be present in response"
            );
        }
    }
}
