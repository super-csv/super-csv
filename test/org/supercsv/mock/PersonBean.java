package org.supercsv.mock;

/** bean class used for the bean reader and writer tests */
public class PersonBean extends SuperBean {
String password, street, town;
int zip;

public String getPassword() {
	return password;
}

public String getStreet() {
	return street;
}

public String getTown() {
	return town;
}

public int getZip() {
	return zip;
}

public void setPassword(final String password) {
	this.password = password;
}

public void setStreet(final String street) {
	this.street = street;
}

public void setTown(final String town) {
	this.town = town;
}

public void setZip(final int zip) {
	this.zip = zip;
}
}
