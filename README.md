# 📍 UK Geographic Distance Service

A Kotlin + Spring Boot REST API that calculates the **straight-line distance** (in kilometers) between two UK postcodes, based on latitude and longitude.  
Includes bonus features like authentication, error handling, Swagger documentation, and in-memory DB persistence via H2.

---

## 🚀 Features

✅ REST API for distance calculation  
✅ Update postcode coordinates  
✅ H2 database (file-based) with Flyway migration  
✅ Swagger UI for easy testing  
✅ Full validation and structured error responses  
✅ Basic authentication
✅ Unit and integration test coverage using JUnit5 + MockMvc

---

## 🛠 Tech Stack
- Kotlin + Spring Boot
- Maven
- Spring Security
- Spring Data JPA
- H2 Database
- Flyway
- Swagger / OpenAPI
- JUnit 5 + MockMvc + Mockito

---

## 🚀 Getting Started (Run Locally)

1. **Clone the repo**
   ```bash
   git clone https://github.com/sufiyuddin/geo-service.git
   cd geo-service

2. First Options
   **Run the application using maven command**
   ```bash
   ./mvnw spring-boot:run
   
3. Second Options
   **Run the application by packaging**
   ```bash
    ./mvnw clean package
    java -jar target/geo-service-0.0.1-SNAPSHOT.jar


4. Authentication
   ```bash
   Username    : admin
   Password    : secret

5. API Endpoints
   ```bash
   - GET /api/distance?from={postcode1}&to={postcode2}
   - Returns the distance between two postcodes in unit km

   - PUT /api/coordinates/{postcode}
   - Update postcode and its coordinates.

   - Open: http://localhost:8080
   - curl -u admin:secret "http://localhost:8080/api/distance?from=AB10%201XG&to=AB11%205QN"
   - curl -u admin:secret -X PUT "http://localhost:8080/api/coordinates/AB21%200TF" -H "Content-Type: application/json" -d "{\"latitude\":2.0,\"longitude\":1.0}"

---

## Access UI

- Swagger UI: http://localhost:8080/swagger-ui/index.html?configUrl=/api-docs/swagger-config
- H2 Console: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:file:./data/postcode_db
- Username: sa
- Password: (leave empty)