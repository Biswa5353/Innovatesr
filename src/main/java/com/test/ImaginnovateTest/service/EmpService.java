package com.test.ImaginnovateTest.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void addEmployee(Employee employee) {
        if (employeeRepository.existsById(employee.getEmployeeId())) {
            throw new IllegalArgumentException("Employee ID already exists.");
        }
        employeeRepository.save(employee);
    }

    public List<EmployeeTaxResponse> getTaxDetails() {
        LocalDate currentDate = LocalDate.now();
        LocalDate financialYearStart = LocalDate.of(currentDate.getYear(), 4, 1);
        if (currentDate.getMonthValue() < 4) {
            financialYearStart = financialYearStart.minusYears(1);
        }

        return employeeRepository.findAll().stream().map(employee -> {
            BigDecimal yearlySalary = calculateYearlySalary(employee, financialYearStart);
            BigDecimal tax = calculateTax(yearlySalary);
            BigDecimal cess = calculateCess(yearlySalary);

            return new EmployeeTaxResponse(
                    employee.getEmployeeId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    yearlySalary,
                    tax,
                    cess
            );
        }).collect(Collectors.toList());
    }

    private BigDecimal calculateYearlySalary(Employee employee, LocalDate financialYearStart) {
        LocalDate doj = employee.getDateOfJoining();
        if (doj.isAfter(financialYearStart)) {
            long fullMonths = ChronoUnit.MONTHS.between(doj.withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
            long remainingDays = ChronoUnit.DAYS.between(doj, doj.withDayOfMonth(1).plusMonths(1));
            BigDecimal fullMonthSalary = employee.getSalaryPerMonth().multiply(BigDecimal.valueOf(fullMonths));
            BigDecimal partialMonthSalary = employee.getSalaryPerMonth().divide(BigDecimal.valueOf(30), 2, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(remainingDays));
            return fullMonthSalary.add(partialMonthSalary);
        } else {
            return employee.getSalaryPerMonth().multiply(BigDecimal.valueOf(12));
        }
    }

    private BigDecimal calculateTax(BigDecimal yearlySalary) {
        BigDecimal tax = BigDecimal.ZERO;
        if (yearlySalary.compareTo(BigDecimal.valueOf(250000)) > 0) {
            BigDecimal slab1 = yearlySalary.min(BigDecimal.valueOf(500000)).subtract(BigDecimal.valueOf(250000));
            tax = tax.add(slab1.multiply(BigDecimal.valueOf(0.05)));
        }
        if (yearlySalary.compareTo(BigDecimal.valueOf(500000)) > 0) {
            BigDecimal slab2 = yearlySalary.min(BigDecimal.valueOf(1000000)).subtract(BigDecimal.valueOf(500000));
            tax = tax.add(slab2.multiply(BigDecimal.valueOf(0.10)));
        }
        if (yearlySalary.compareTo(BigDecimal.valueOf(1000000)) > 0) {
            BigDecimal slab3 = yearlySalary.subtract(BigDecimal.valueOf(1000000));
            tax = tax.add(slab3.multiply(BigDecimal.valueOf(0.20)));
        }
        return tax;
    }

    private BigDecimal calculateCess(BigDecimal yearlySalary) {
        if (yearlySalary.compareTo(BigDecimal.valueOf(2500000)) > 0) {
            return yearlySalary.subtract(BigDecimal.valueOf(2500000)).multiply(BigDecimal.valueOf(0.02));
        }
        return BigDecimal.ZERO;
    }
}
