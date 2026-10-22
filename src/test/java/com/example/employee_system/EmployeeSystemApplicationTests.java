package com.example.employee_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "GMAIL_APP_USERNAME=test@example.com",
        "GMAIL_APP_PASSWORD=test-password",
        "spring.mail.username=test@example.com",
        "spring.mail.password=test-password"
})
class EmployeeSystemApplicationTests {

    @Test
    void contextLoads() {
    }

}
