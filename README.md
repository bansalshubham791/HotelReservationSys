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

---

## 🔍 APIs Covered

- **Get Room Details**  
  `GET /api/room/{id}`

- **Search Available Rooms**  
  `GET /api/room?checkin={date}&checkout={date}`

- **Booking Report**  
  `GET /api/report`

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
mvn test -Dcucumber.options="--glue src/test/java/stepdefinitions src/test/resources/features/get_room_details.feature"

5️⃣ View Reports

Extent Reports: target/extent-reports/
Cucumber HTML Reports: target/cucumber-reports/
