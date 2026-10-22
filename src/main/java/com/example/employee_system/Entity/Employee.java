package com.example.employee_system.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "phone_number", length = 25)
    private String phoneNumber;
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;
    @Column(name = "position", nullable = false)
    private String position;
    @Column(name = "is_verified", columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
    private boolean isVerified;
    @Column(name = "account_creation_token")
    private String accountCreationToken;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    @JsonProperty("department_id")
    private Department department;
//    @OneToOne(mappedBy = "employee", cascade = CascadeType.REMOVE)
//    private UserAccount userAccount;

    public UUID getDepartment() {
        return department.getId();
    }

//    public UUID getUserAccount() {
//        return userAccount.getId();
//    }
}
