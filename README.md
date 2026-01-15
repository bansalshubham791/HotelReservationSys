# 🏨 Hotel Booking API Automation Project

This project is an automated API testing framework for a **Hotel Booking system**, built using **Java, Maven, Cucumber, TestNG, and RestAssured**.  
It validates key hotel booking functionalities such as room availability, room details, and booking reports.

---

## 📌 Project Objectives

- Validate hotel booking APIs using business-oriented BDD scenarios
- Ensure room availability and booking rules are enforced
- Generate clear and readable test execution reports
- Support scalable and maintainable API automation

---

## 🧰 Tech Stack

- **Java v11**
- **Maven v3.9.11**
- **Cucumber (BDD) v7.33.0**
- **TestNG v7.11.0**
- **RestAssured v5.5.6**
- **Extent Reports v5.1.1**

---
## 🔍covered scenarios

**Successful hotel room booking**

**create a booking with incorrect field values**

**create a booking when user give checkout date as earlier than check in date**

**Successful hotel room booking creation, modification and cancellation**

**Get the details of the room by room id**

**Get the room booking summary**

**Hotel manager generates a booking report**

**Guest attempts to view details of a non-existing room**

**Guest views details of an available hotel room**

**Guest searches for available rooms for a valid stay period**

**Update an existing booking**

**create an auth token**

**Cancel an existing booking successfully**

**Cancel booking without authentication**

**Cancel a non-existing booking**

**Unauthorized user attempts to retrieve booking details**

**Attempt to retrieve booking details using an invalid room ID**

**User attempts login with different invalid credentials**

**Successful hotel room booking creation, modification and cancellation**
---

## 🔍 APIs Covered

- **Get Room Details**  
  `GET /api/room/{id}`

- **Search Available Rooms**  
  `GET /api/room?checkin={date}&checkout={date}`

- **Booking Report**  
  `GET /api/report`

- **Login Auth**  
  `POST /api/auth/login 

- **Create Booking**  
  `POST api/booking

- **Create Booking**  
  `POST api/booking

- **Cancel Booking**  
  `DELETE api/booking/<bookingid>

- **Booking Details**  
  `GET api/booking

- **Modify Booking**  
  `PUT api/booking/



---

## ▶️ How to Run Tests from CMD
1️⃣ Clone the Repository
git clone https://github.com/bansalshubham791/HotelReservationSys.git
cd HotelReservationSys

2️⃣ Clean and Compile the Project
mvn clean compile

3️⃣ Run All Tests
mvn test

4️⃣ Run Specific Feature File (Optional)
mvn test -Dcucumber.options="--glue src/test/java/stepDefinitions src/test/resources/features/getRoomDetails.feature"

5️⃣ View Reports

Extent Reports: target/extent-reports/

TestReport/Hotel Reservation API Test Report.html

Cucumber HTML Reports: target/cucumber-reports/
