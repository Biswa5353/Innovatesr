package com.test.ImaginnovateTest.entity;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
class Employee {

    @Id
    @NotBlank(message = "Employee ID is required.")
    private String employeeId;

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @ElementCollection
    @NotEmpty(message = "At least one phone number is required.")
    private List<@Pattern(regexp = "\\d{10}", message = "Invalid phone number.") String> phoneNumbers;

    @NotNull(message = "Date of joining is required.")
    private LocalDate dateOfJoining;

    @NotNull(message = "Salary per month is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be greater than 0.")
    private BigDecimal salaryPerMonth;


}

class EmployeeTaxResponse {

    private String employeeId;
    private String firstName;
    private String lastName;
    private BigDecimal yearlySalary;
    private BigDecimal taxAmount;
    private BigDecimal cessAmount;

    public EmployeeTaxResponse(String employeeId, String firstName, String lastName, BigDecimal yearlySalary, BigDecimal taxAmount, BigDecimal cessAmount) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.yearlySalary = yearlySalary;
        this.taxAmount = taxAmount;
        this.cessAmount = cessAmount;
    }


}
