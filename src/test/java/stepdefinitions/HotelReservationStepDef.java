package stepdefinitions;

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

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;

public class HotelReservationStepDef extends Utilities {

    private Response loginResponse;
    private Response bookingResponse;
    protected String authToken;
    private RequestSpecification bookingSpec;
    private RequestSpecification loginRequest;
    private Response updateResponse;
    private Response cancelResponse;
    private RequestSpecification roomRequest;
    private Response roomResponse;

    @Given("the user submits valid login credentials:")
    public void the_user_submits_valid_login_credentials(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", data.get("username"));
        requestBody.put("password", data.get("password"));

        loginRequest = given().body(requestBody.toString());
        loginRequest.header("Authorization", "automationintesting.online");
        loginRequest.header("Content-Type", "application/json");
        //loginRequest = requestPostLoginSetup(dataTable);
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
        if (bookingSpec == null){
            bookingSpec = given();
        }
        bookingSpec = requestPostSetup(dataTable);
    }

    @And("the user submits the booking")
    public void theUserSubmitsTheBooking() {
        bookingResponse = bookingSpec.when().post("api/booking");
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

    @Given("the user wants to check the room details")
    public void theUserWantsToCheckTheRoomDetails() {
        bookingSpec = requestGetSetup().cookie("token", authToken);
    }

    @When("the user asks the details of the room by:")
    public void theUserAsksTheDetailsOfTheRoomByRoomIdRoomid(String roomid) {
        bookingResponse = bookingSpec.when().get("api/room/"+roomid);
    }

    @Then("details of the room is available:")
    public void detailsOfTheRoomIsAvailable(DataTable dataTable) {
        List<String> expectedFields = dataTable.asList(String.class);
        for (String field : expectedFields) {
            Object value = bookingResponse.jsonPath().get(field);
            assertNotNull(
                    value.toString(),
                    field + " should be present in response"
            );
        }
    }

    @And("the response matches with json schema {string}")
    public void theResponseMatchesWithJsonSchema(String schemaFileName) {
        bookingResponse.then().log().body();
        bookingResponse.then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchema/"+schemaFileName));
    }


    @Then("the user should see response with incorrect {string}")
    public void theUserShouldSeeResponseWithIncorrect(String expectedFieldError) {
        String responseBody = bookingResponse.getBody().asString();
        assertTrue(
                responseBody.contains(expectedFieldError),
                "Expected error message not found. Actual response: " + responseBody
        );
    }
    @Given("the user wants to check the room booking summary")
    public void theUserWantsToCheckTheRoomSummary() {
        bookingSpec = requestGetSetup().cookie("token", authToken);
    }

    @When("the user asks the room booking summary for {string}")
    public void theUserAsksTheRoomSummaryFor(String roomid) {
        bookingSpec = bookingSpec.queryParam("roomid", roomid);
        bookingResponse = bookingSpec.when().get("api/booking");

    }

    @Then("the room booking summary response should be successful")
    public void theBookingSummaryResponseShouldBeSuccessful() {

        assertEquals(200, bookingResponse.statusCode(),
                "Expected HTTP 200 for booking summary");
    }

    @Then("the booking should be successfully updated with response code {int}")
    public void theBookingShouldBeSuccessfullyUpdatedWithResponseCode(Integer expectedStatus) {
        assertEquals(updateResponse.getStatusCode(), expectedStatus.intValue(),
                "Unexpected status code");
    }


    @When("the user updates the existing booking with the following details:")
    public void theUserUpdatesTheExistingBookingWithTheFollowingDetails(DataTable dataTable) {
        String bookingId = bookingResponse.getBody().jsonPath().getString("bookingid");
        updateResponse = requestPutSetup(dataTable)
                        .cookie("token", authToken)
                        .when()
                        .put("api/booking/" + bookingId);
        updateResponse.then().log().body();
    }

    @When("the user cancels the booking")
    public void theUserCancelsTheBooking() {

        // bookingId should already be set from a previous step
        String bookingId = bookingResponse.getBody().jsonPath().getString("bookingid");
        cancelResponse = requestDeleteSetup()
                        .cookie("token", authToken)
                        .when()
                        .delete("api/booking/" + bookingId);
        cancelResponse.then().log().body();
    }
    @Then("the booking should be successfully cancelled with response code {int}")
    public void theBookingShouldBeSuccessfullyCancelledWithResponseCode(int statusCode) {

        assertEquals(cancelResponse.getStatusCode(), statusCode,
                "Unexpected status code");
    }
    @And("the booking should no longer be retrievable")
    public void theBookingShouldNoLongerBeRetrievable() {
        String bookingId = bookingResponse.getBody().jsonPath().getString("bookingid");
        bookingResponse =   requestGetSetup()
                            .cookie("token", authToken)
                            .when()
                            .get("api/booking/" + bookingId);

        assertEquals(404, bookingResponse.statusCode(),
                "Booking should not be retrievable after cancellation"
        );
    }

    @Given("a booking exists with booking id")
    public void aBookingExistsWithBookingId() {
        //String bookingId = bookingResponse.getBody().jsonPath().getString("bookingid");
        assertTrue(bookingResponse.getBody().asString().contains("bookingid"),
                "Response does not indicate missing parameter. Actual response: " + bookingResponse.getBody().asString());

    }

    @When("the user attempts to cancel the booking without authentication")
    public void theUserAttemptsToCancelTheBookingWithoutAuthentication() {
        String bookingId = bookingResponse.getBody().jsonPath().getString("bookingid");
        cancelResponse = requestDeleteSetup()
                .when()
                .delete("api/booking/" + bookingId);    //cookie removed from request
    }

    @Then("the booking should not be successfully cancelled with response code {int}")
    public void theBookingShouldNotBeSuccessfullyCancelledWithResponseCode(int statusCode) {
        assertEquals(cancelResponse.getStatusCode(), statusCode,
                "Unexpected status code");
        cancelResponse.then().log().body();
    }

    @And("the user should see an error message {string}")
    public void theUserShouldSeeAnErrorMessage(String message) {
        assertEquals(cancelResponse.getBody().jsonPath().getString("error"), message,
                "Unexpected error");
    }

    @Given("no booking exists with booking id {string}")
    public void noBookingExistsWithBookingId(String bookingId) {
        cancelResponse = requestDeleteSetup()
                .cookie("token", authToken)
                .when()
                .delete("api/booking/" + bookingId);
    }

    @When("the guest requests details for room number {int}")
    public void theGuestRequestsDetailsForRoomNumber(int roomid) {
        roomResponse = roomRequest.get("api/room/" + roomid);
    }

    @Given("the hotel offers rooms for booking")
    public void theHotelOffersRoomsForBooking() {
        roomRequest = requestGetRoomDetailsSetup().cookie("token", authToken);
    }

    @Then("the system should provide the room information for room number {int}")
    public void theSystemShouldProvideTheRoomInformation(int roomid) {
        roomResponse.then().log().body();
        assertEquals(Integer.parseInt(roomResponse.getBody().jsonPath().getString("roomid")), roomid,
                "Unexpected error");
    }

    @And("the room should have a name and description")
    public void theRoomShouldHaveANameAndDescription() {
        assertNotNull(roomResponse.getBody().jsonPath().getString("roomName"));
        assertNotNull(roomResponse.getBody().jsonPath().getString("roomName"));
    }
}
