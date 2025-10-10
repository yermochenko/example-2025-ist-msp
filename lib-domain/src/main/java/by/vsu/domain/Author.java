package by.vsu.domain;

import java.util.Optional;

public class Author extends Entity {
	private String firstName;
	private String middleName;
	private String lastName;
	private Integer birthYear;
	private Integer deathYear;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Optional<String> getMiddleName() {
		return Optional.ofNullable(middleName);
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Integer getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(Integer birthYear) {
		this.birthYear = birthYear;
	}

	public Optional<Integer> getDeathYear() {
		return Optional.ofNullable(deathYear);
	}

	public void setDeathYear(Integer deathYear) {
		this.deathYear = deathYear;
	}
}
