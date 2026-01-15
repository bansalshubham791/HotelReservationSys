package stepDefinitions;

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
import static org.hamcrest.Matchers.*;
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
    private RequestSpecification roomAvailabilityRequest;
    private Response roomAvailabilityResponse;
    private RequestSpecification reportRequest;
    private Response reportResponse;
    JSONObject bookingdates = new JSONObject();

    @Given("the user submits valid login credentials:")
    public void the_user_submits_valid_login_credentials(DataTable dataTable) {
        List<Map<String, String>> credentials = dataTable.asMaps(String.class, String.class);
        JSONObject obj = new JSONObject();
        obj.put("username", credentials.get(0).get("username"));
        ;
        obj.put("password", credentials.get(0).get("password"));
        loginRequest = given().body(obj.toString());
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
        assertEquals(loginResponse.getStatusCode(), 200,
                "Unexpected status code");
    }

    @And("the user submits the booking")
    public void theUserSubmitsTheBooking() {
        bookingResponse = bookingSpec.when().post("https://automationintesting.online/api/booking");
    }

    @Then("the booking should be successfully created")
    public void theBookingShouldBeSuccessfullyCreated() {
        bookingResponse.then().log().all();
        assertEquals(bookingResponse.getStatusCode(), 201,
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
        bookingResponse = bookingSpec.when().get("api/room/" + roomid);
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
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath
                        ("jsonSchema/" + schemaFileName));
    }


    @Then("the user gets {string} error message")
    public void theUserGetsErrorMessage(String expectedFieldError) {
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

    @When("the user asks the room booking summary for roomid{string}")
    public void theUserAsksTheRoomSummaryForRoomID(String roomid) {
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

    @Then("the booking should be successfully cancelled")
    public void theBookingShouldBeSuccessfullyCancelledWithResponseCode() {

        assertEquals(cancelResponse.getStatusCode(), 200,
                "some problem with room cancellation");
    }

    @And("the booking should no longer be retrievable")
    public void theBookingShouldNoLongerBeRetrievable() {
        String bookingId = bookingResponse.getBody().jsonPath().getString("bookingid");
        bookingResponse = requestGetSetup()
                .cookie("token", authToken)
                .when()
                .get("api/booking/" + bookingId);

        assertEquals(404, bookingResponse.statusCode(),
                "Booking should not be retrievable after cancellation"
        );
    }

    @Given("a booking exists with booking id")
    public void aBookingExistsWithBookingId() {
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

    @Then("the booking should not be successfully cancelled")
    public void theBookingShouldNotBeSuccessfullyCancelled() {
        assertEquals(cancelResponse.getStatusCode(), 401,
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
        assertNotNull(roomResponse.getBody().jsonPath().getString("description"));
    }

    @And("the room should display its price per night")
    public void theRoomShouldDisplayItsPricePerNight() {
        assertNotNull(roomResponse.getBody().jsonPath().getString("roomPrice"));
    }

    @And("the room should list the amenities available to the guest")
    public void theRoomShouldListTheAmenitiesAvailableToTheGuest() {
        assertNotNull(roomResponse.getBody().jsonPath().getString("features"));
    }

    @When("the guest requests details for a room that does not exist like {int}")
    public void theGuestRequestsDetailsForARoomThatDoesNotExistLike(int roomid) {
        roomRequest = requestGetRoomDetailsSetup().cookie("token", authToken);
        roomResponse = roomRequest.get("api/room/" + roomid);
    }

    @Then("the system should inform the guest that the room could not be found")
    public void theSystemShouldInformTheGuestThatTheRoomNoCouldNotBeFound() {
        roomResponse.then().log().body();
        assertEquals(roomResponse.getStatusCode(), 500,
                "Unexpected error");
    }

    @Given("the hotel accepts bookings for future dates")
    public void theHotelAcceptsBookingsForFutureDates() {
        roomAvailabilityRequest = requestGetRoomAvailabilitySetup().cookie("token", authToken);
    }

    @When("the guest searches for available rooms with a check-in date of {string}")
    public void theGuestSearchesForAvailableRoomsWithACheckInDateOf(String checkin) {
        roomAvailabilityRequest = roomAvailabilityRequest.queryParam("checkin", checkin);
    }

    @And("the check-out date is {string}")
    public void theCheckOutDateIs(String checkout) {
        roomAvailabilityRequest = roomAvailabilityRequest.queryParam("checkout", checkout);
    }

    @Then("the system should return the list of available rooms")
    public void theSystemShouldReturnTheListOfAvailableRooms() {
        roomAvailabilityResponse = roomAvailabilityRequest.get("api/room");
        roomAvailabilityResponse.then().log().body();
        roomAvailabilityResponse.then()
                .statusCode(200)
                .body("rooms", notNullValue())
                .body("rooms.size()", greaterThanOrEqualTo(0));
    }

    @Given("the hotel has booking data available")
    public void theHotelHasBookingDataAvailable() {
        reportRequest = requestGetReportSetup().cookie("token", authToken);
    }

    @When("the hotel manager requests the booking report")
    public void theHotelManagerRequestsTheBookingReport() {
        reportResponse = reportRequest.get("api/report");
    }

    @Then("the system should generate the booking report")
    public void theSystemShouldGenerateTheBookingReport() {
        reportResponse.then().log().body();
        assertEquals(reportResponse.getStatusCode(), 200,
                "booking report is not generated");
    }

    @Given("the user attempts to log in to the hotel booking system")
    public void theUserAttemptsToLogInToTheHotelBookingSystem() {
        if (loginRequest == null) {
            loginRequest = given();
        }
        loginRequest.header("Authorization", "automationintesting.online");
        loginRequest.header("Content-Type", "application/json");
    }

    @When("the user submits the following credentials")
    public void theUserSubmitsTheFollowingCredentials(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", data.get("username"));
        requestBody.put("password", data.get("password"));
        loginRequest = given().body(requestBody.toString());
    }

    @Then("the system should return the response {string}")
    public void theSystemShouldReturnTheResponse(String statusCode) {
        loginResponse = loginRequest.when().post("https://automationintesting.online/api/auth/login");
        assertEquals(loginResponse.getStatusCode(), Integer.parseInt(statusCode),
                "Unexpected error");
    }

    @When("user tries to create a booking:")
    public void userTriesToCreateABooking(DataTable dataTable) {
        List<Map<String, String>> rows =
                dataTable.asMaps(String.class, String.class);

        Map<String, String> row = rows.get(0);
        JSONObject bookingRequestBody = new JSONObject();
        bookingRequestBody.put("firstname", row.get("firstname"));
        bookingRequestBody.put("lastname", row.get("lastname"));
        bookingRequestBody.put("email", row.get("email"));
        bookingRequestBody.put("phone", row.get("phone"));
        bookingRequestBody.put("roomid", generateRandomRoomId());
        bookingRequestBody.put("bookingdates", bookingdates);
        bookingRequestBody.put("depositpaid", Boolean.parseBoolean(row.get("depositpaid")));
        if (bookingSpec == null) {
            bookingSpec = given();
        }
        bookingSpec = given()
                .cookie("token", authToken)
                .header("Content-Type", "application/json")
                .header("Authorization", "automationintesting.online")
                .body(bookingRequestBody.toString());
    }

    @And("the user is unable to create a booking")
    public void theUserIsUnableToCreateABooking() {
        assertEquals(bookingResponse.getStatusCode(), 409,
                "Unexpected status code");

    }

    @Given("user gives checkin date as {string} and checkout date as {string}")
    public void userGivesDateAndDate(String checkin, String checkout) {
        bookingdates.put("checkin", checkin);
        bookingdates.put("checkout", checkout);
    }

    @Given("the user is not authenticated")
    public void theUserIsNotAuthenticated() {
        bookingSpec = requestGetSetup();
    }

    @Then("the system should deny access")
    public void theSystemShouldDenyAccess() {
        assertEquals(bookingResponse.getStatusCode(), 401,
                "Unexpected status code");
    }

    @And("the booking should not be cancelled")
    public void theBookingShouldNotBeCancelled() {
        assertEquals(cancelResponse.getStatusCode(), 500,
                "Unexpected status code");
        cancelResponse.then().log().body();
    }

    @Then("the system should not show any valid room")
    public void theSystemShouldNotShowAnyValidRoom() {
        bookingResponse.then().log().body();
        bookingResponse.then().body("rooms", nullValue());
    }

    @When("the user asks the room booking summary for roomid {int}")
    public void theUserAsksTheRoomBookingSummaryForRoomid(int roomid) {
        bookingSpec = bookingSpec.queryParam("roomid", String.valueOf(roomid));
        bookingResponse = bookingSpec.when().get("api/booking");
    }


    @And("the user should see an auth error message {string}")
    public void theUserShouldSeeAnAuthErrorMessage(String message) {
        assertEquals(bookingResponse.getBody().jsonPath().getString("error"), message,
                "Unexpected Auth");
    }
}