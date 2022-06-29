package edu.umcs.bookystore.dtos;

public class UserInfoDto {

    private String email;
    private String firstName;
    private String lastName;

    public UserInfoDto() {
    }

    public UserInfoDto(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getFullNameAndEmail() {
        return String.format("%s (%s)", getFullName(), email);
    }

}
