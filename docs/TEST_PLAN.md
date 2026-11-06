# ELITE CIRCLE CHAMA – TEST PLAN

## 1. Introduction

This document outlines the testing procedures for the Elite Circle Chama Management System. The purpose is to ensure that the application functions correctly and meets its requirements.

## 2. Testing Objectives

- Verify that contributions can be added successfully.
- Verify that contribution records can be viewed.
- Verify that existing contributions can be updated.
- Verify that contributions can be deleted.
- Ensure that invalid inputs are rejected.
- Verify database relationships and constraints.

## 3. Test Cases

| Test ID | Test Description | Expected Result |
|---|---|---|
| TC01 | Add a valid contribution | Contribution is saved successfully |
| TC02 | View all contributions | Contributions appear in the table |
| TC03 | Update a contribution | Updated details are saved |
| TC04 | Delete a contribution | Selected contribution is removed |
| TC05 | Enter an invalid amount | System displays an error |
| TC06 | Enter an invalid date | System displays an error |
| TC07 | Enter a non-existent Member ID | Database rejects the contribution |
| TC08 | Delete a member with contributions | Related records follow the database cascade-delete rule |

## 4. Testing Approach

The application will undergo functional testing to verify that each feature performs as expected. Database testing will also be conducted to check foreign key constraints and relationships between members and contributions.

## 5. Test Results

Test results will be recorded after executing each test case. Screenshots of successful operations and error messages will be collected as evidence.

## 6. Conclusion

Testing will help identify errors and ensure that the Elite Circle Chama Management System is reliable, functional and meets the project requirements.