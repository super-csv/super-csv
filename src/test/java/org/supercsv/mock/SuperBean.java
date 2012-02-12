package org.supercsv.mock;

/**
 * @author Kasper B. Graversen
 */
public class SuperBean {
	String firstname;
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(final String firstname) {
		this.firstname = firstname;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstname == null) ? 0 : firstname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( !(obj instanceof SuperBean) )
			return false;
		SuperBean other = (SuperBean) obj;
		if( firstname == null ) {
			if( other.firstname != null )
				return false;
		} else if( !firstname.equals(other.firstname) )
			return false;
		return true;
	}
	
}
