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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((town == null) ? 0 : town.hashCode());
		result = prime * result + zip;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if( this == obj )
			return true;
		if( !super.equals(obj) )
			return false;
		if( !(obj instanceof PersonBean) )
			return false;
		PersonBean other = (PersonBean) obj;
		if( password == null ) {
			if( other.password != null )
				return false;
		} else if( !password.equals(other.password) )
			return false;
		if( street == null ) {
			if( other.street != null )
				return false;
		} else if( !street.equals(other.street) )
			return false;
		if( town == null ) {
			if( other.town != null )
				return false;
		} else if( !town.equals(other.town) )
			return false;
		if( zip != other.zip )
			return false;
		return true;
	}
	
}
