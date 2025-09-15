package com.bmt.webapp.models;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CustomerDto {

    @NotEmpty(message = "The First Name is required")
    private String firstName;

    @NotEmpty(message = "The Last Name is required")
    private String lastName;

    @NotEmpty(message = "The Email is required")
    @Email
    private String email;

    private String phone;
    
    @NotEmpty(message = "Date of Birth is required")
    private String dateOfBirth;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String planType;
    private String idType;
    private String idNumber;

    @NotEmpty(message = "The Status is required")
    private String status; // Active, Inactive

    // ✅ Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Custom validation method for age requirement
    public boolean isValidAge() {
        if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
            return false;
        }
        
        try {
            LocalDate birthDate = LocalDate.parse(dateOfBirth);
            LocalDate currentDate = LocalDate.now();
            LocalDate minimumDate = currentDate.minusYears(16);
            
            return birthDate.isBefore(minimumDate) || birthDate.isEqual(minimumDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
