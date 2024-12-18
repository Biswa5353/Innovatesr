package com.test.ImaginnovateTest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<Employee, String> {
}
