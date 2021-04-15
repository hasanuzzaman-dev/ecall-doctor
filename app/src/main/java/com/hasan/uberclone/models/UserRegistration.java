package com.hasan.uberclone.models;

public class UserRegistration {

    private String RegistrationCode;
    private int UserType;
    private String firstName;
    private String lastName;
    private String PhoneNumber;
    private String Email;
    private String gender;
    private String dateOfBirth;
    private boolean termsAndCondition;

    public UserRegistration() {
    }

    public UserRegistration(String registrationCode, int userType, String firstName, String lastName,
                            String phoneNumber, String email, String gender,
                            String dateOfBirth, boolean termsAndCondition) {
        RegistrationCode = registrationCode;
        UserType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        PhoneNumber = phoneNumber;
        Email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.termsAndCondition = termsAndCondition;
    }

    public String getRegistrationCode() {
        return RegistrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        RegistrationCode = registrationCode;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
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

    @Override
    public String toString() {
        return "UserRegistration{" +
                "RegistrationCode='" + RegistrationCode + '\'' +
                ", UserType=" + UserType +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", PhoneNumber='" + PhoneNumber + '\'' +
                ", Email='" + Email + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", termsAndCondition=" + termsAndCondition +
                '}';
    }
}
