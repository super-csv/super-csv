package org.supercsv.features;

import java.math.BigDecimal;
import java.util.Date;

public class FeatureBean {
	
	private String firstName;
	private String lastName;
	private int age;
	private Date birthDate;
	private BigDecimal savings;
	
	public FeatureBean() {
	}
	
	public FeatureBean(String firstName, String lastName, int age) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
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
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public Date getBirthDate() {
		return birthDate;
	}
	
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	public BigDecimal getSavings() {
		return savings;
	}
	
	public void setSavings(BigDecimal savings) {
		this.savings = savings;
	}
}
