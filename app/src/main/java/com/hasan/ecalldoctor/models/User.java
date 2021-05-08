package com.hasan.ecalldoctor.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class User implements Serializable {

    private String userId;
    private String userType;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String address;
    private String bloodGroup;
    private String gender;
    private String dateOfBirth;
    private boolean termsAndCondition;
    private boolean isNewRegistered;

    public User() {
    }

    public User(String userId, String userType, String firstName, String lastName,
                String phoneNumber, String email,String address, String bloodGroup, String gender,
                String dateOfBirth, boolean termsAndCondition, boolean isNewRegistered) {
        this.userId = userId;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.termsAndCondition = termsAndCondition;
        this.isNewRegistered = isNewRegistered;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isTermsAndCondition() {
        return termsAndCondition;
    }

    public void setTermsAndCondition(boolean termsAndCondition) {
        this.termsAndCondition = termsAndCondition;
    }

    public boolean isNewRegistered() {
        return isNewRegistered;
    }

    public void setNewRegistered(boolean newRegistered) {
        isNewRegistered = newRegistered;
    }

    @NotNull

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userType='" + userType + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", bloodGroup='" + bloodGroup + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", termsAndCondition=" + termsAndCondition +
                ", isNewRegistered=" + isNewRegistered +
                '}';
    }
}
