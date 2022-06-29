package edu.umcs.bookystore.dtos.payu;

public class BuyerDto {

	private String email;
	private String firstName;
	private String lastName;
	private String language;

	public BuyerDto() {
	}

	public BuyerDto(String email, String firstName, String lastName, String language) {
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.language = language;
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
