package com.example.employee_system;

import com.example.employee_system.Dto.loginRequest;
import com.example.employee_system.Entity.Department;
import com.example.employee_system.Entity.Employee;
import com.example.employee_system.Entity.UserAccount;
import com.example.employee_system.Repository.DepartmentRepo;
import com.example.employee_system.Repository.EmployeeRepo;
import com.example.employee_system.Repository.UserAccountRepo;
import com.example.employee_system.Service.EmailService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.MySQLContainer;

import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {

    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8");
    @MockitoBean
    private EmailService emailService;
    @Autowired
    private DepartmentRepo departmentRepo;
    @Autowired
    private EmployeeRepo employeeRepo;
    @Autowired
    private UserAccountRepo userAccountRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Department department;
    private Employee employeeAdmin;
    private String adminToken;
    private String userToken;

    @LocalServerPort()
    private Integer port;

    @BeforeAll
    static void beforeAll() {
        mysql.start();
    }

    @AfterAll
    static void afterAll() {
        mysql.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.sql.init.mode", () -> "never");
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = "http://localhost:" + port;
        userAccountRepo.deleteAll();
        employeeRepo.deleteAll();
        departmentRepo.deleteAll();
        department = new Department(null, "IT");
        departmentRepo.save(department);
        employeeAdmin = createEmployee("moudar", "alaskari", "moudaralaskari576@gmail.com");
        Employee employee1 = createEmployee("ali", "alaskari", "ali@gmail.com");
        Employee employee2 = createEmployee("essa", "alaskari", "essa@gmail.com");
        Employee employee3 = createEmployee("omar", "alaskari", "omar@gmail.com");
        employeeRepo.saveAll(List.of(employeeAdmin, employee2, employee3, employee1));
        UserAccount adminAccount = createUserAccount(employeeAdmin, "admin", "password", "ADMIN");
        UserAccount userAccount = createUserAccount(employee1, "user", "password", "USER");
        userAccountRepo.saveAll(List.of(adminAccount, userAccount));
        adminToken = getToken("admin", "password");
        userToken = getToken("user", "password");

        assertNotNull(adminToken, "Admin token is null");
        assertFalse(adminToken.isBlank(), "Admin token is blank");

    }

    @Test
    void shouldGetAllEmployeeAsAdmin() {
        given()
                .log().all()
                .header("Authorization", "Bearer " + adminToken)
                .when()
                .get("/employee?page=1&size=100")
                .then()
                .statusCode(200)
                .body("data.content", hasSize(4))
                .body("data.content[0].firstName", notNullValue());
    }

    @Test
    void shouldNotGetAllEmployeeAsUser() {
        given()
                .log().all()
                .header("Authorization", "Bearer " + userToken)
                .when()
                .get("/employee?page=1&size=100")
                .then()
                .statusCode(403);
    }

    private Employee createEmployee(String firstName, String lastName, String email) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setEmail(email);
        employee.setPosition("Developer");
        employee.setPhoneNumber("0992140442");
        employee.setHireDate(LocalDate.now());
        employee.setVerified(true);
        employee.setDepartment(department);
        return employee;
    }

    private UserAccount createUserAccount(Employee employee, String username, String password, String role) {
        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setRole(role);
        account.setEmployee(employee);
        return account;
    }

    private String getToken(String username, String password) {
        loginRequest login = new loginRequest(username, password);
        return given()
                .contentType("application/json")
                .body(login)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getString("data");
    }

}
