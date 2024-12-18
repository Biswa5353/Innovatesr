package com.test.ImaginnovateTest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@Validated
class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<String> storeEmployee(@Valid @RequestBody Employee employee) {
        employeeService.addEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee stored successfully.");
    }

    @GetMapping("/tax-deduction")
    public ResponseEntity<List<EmployeeTaxResponse>> calculateTax() {
        List<EmployeeTaxResponse> taxDetails = employeeService.getTaxDetails();
        return ResponseEntity.ok(taxDetails);
    }
}
