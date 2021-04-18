package com.hasan.uberclone.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class User implements Serializable {

    private String userId;
    private String userType;
    private String firstName;
    private String lastName;
    private String PhoneNumber;
    private String Email;
    private String gender;
    private String dateOfBirth;
    private boolean termsAndCondition;
    private boolean isNewRegistered;

    public User() {
    }

    public User(String userId, String userType, String firstName, String lastName,
                String phoneNumber, String email, String gender,
                String dateOfBirth, boolean termsAndCondition,boolean isNewRegistered) {
        this.userId = userId;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        PhoneNumber = phoneNumber;
        Email = email;
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
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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
                ", userType=" + userType +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Email='" + Email + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", termsAndCondition=" + termsAndCondition +
                ", isNewRegistered=" + isNewRegistered +
                '}';
    }
}
