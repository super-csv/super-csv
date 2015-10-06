/*
 * Copyright 2007 Kasper B. Graversen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.mock;

import java.sql.Time;
import java.util.Date;

/**
 * Bean to use when testing bean reading/writing.
 * 
 * @author Kasper B. Graversen
 * @author James Bassett
 * @author Pietro Aragona
 */
public class PersonBean {
	
	private String firstName;
	
	private String lastName;
	
	private Date birthDate;
	
	private Time birthTime;

	private Boolean married;
	
	private Integer numberOfKids;
	
	private String favouriteQuote;
	
	private String email;
	
	/**
	 * Default constructor.
	 */
	public PersonBean() {
	}
	
	/**
	 * Constructs a PersonBean.
	 * 
	 * @param firstName
	 * @param lastName
	 * @param birthDate
	 * @param married
	 * @param numberOfKids
	 * @param favouriteQuote
	 * @param email
	 */
	public PersonBean(final String firstName, final String lastName, final Date birthDate, final Time birthTime,
		final Boolean married, final Integer numberOfKids, final String favouriteQuote, final String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.married = married;
		this.numberOfKids = numberOfKids;
		this.favouriteQuote = favouriteQuote;
		this.email = email;
		this.birthTime = birthTime;
	}
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	
	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	/**
	 * @return the birthTime
	 */
	public Time getBirthTime() {
		return birthTime;
	}
	
	/**
	 * @param birthTime
	 *            the birthTime to set
	 */
	public void setBirthTime(Time birthTime) {
		this.birthTime = birthTime;
	}
	
	/**
	 * @return the married
	 */
	public Boolean getMarried() {
		return married;
	}
	
	/**
	 * @param married
	 *            the married to set
	 */
	public void setMarried(Boolean married) {
		this.married = married;
	}
	
	/**
	 * @return the numberOfKids
	 */
	public Integer getNumberOfKids() {
		return numberOfKids;
	}
	
	/**
	 * @param numberOfKids
	 *            the numberOfKids to set
	 */
	public void setNumberOfKids(Integer numberOfKids) {
		this.numberOfKids = numberOfKids;
	}
	
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the favouriteQuote
	 */
	public String getFavouriteQuote() {
		return favouriteQuote;
	}
	
	/**
	 * @param favouriteQuote
	 *            the favouriteQuote to set
	 */
	public void setFavouriteQuote(String favouriteQuote) {
		this.favouriteQuote = favouriteQuote;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((birthTime == null) ? 0 : birthTime.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((favouriteQuote == null) ? 0 : favouriteQuote.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((married == null) ? 0 : married.hashCode());
		result = prime * result + ((numberOfKids == null) ? 0 : numberOfKids.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( this == obj ) {
			return true;
		}
		if( obj == null ) {
			return false;
		}
		if( !(obj instanceof PersonBean) ) {
			return false;
		}
		PersonBean other = (PersonBean) obj;
		if( birthDate == null ) {
			if( other.birthDate != null ) {
				return false;
			}
		} else if( !birthDate.equals(other.birthDate) ) {
			return false;
		}
		if( birthTime == null ) {
			if( other.birthTime != null ) {
				return false;
			}
		} else if( !birthTime.toString().equals(other.birthTime.toString()) ) {
			return false;
		}
		if( email == null ) {
			if( other.email != null ) {
				return false;
			}
		} else if( !email.equals(other.email) ) {
			return false;
		}
		if( favouriteQuote == null ) {
			if( other.favouriteQuote != null ) {
				return false;
			}
		} else if( !favouriteQuote.equals(other.favouriteQuote) ) {
			return false;
		}
		if( firstName == null ) {
			if( other.firstName != null ) {
				return false;
			}
		} else if( !firstName.equals(other.firstName) ) {
			return false;
		}
		if( lastName == null ) {
			if( other.lastName != null ) {
				return false;
			}
		} else if( !lastName.equals(other.lastName) ) {
			return false;
		}
		if( married == null ) {
			if( other.married != null ) {
				return false;
			}
		} else if( !married.equals(other.married) ) {
			return false;
		}
		if( numberOfKids == null ) {
			if( other.numberOfKids != null ) {
				return false;
			}
		} else if( !numberOfKids.equals(other.numberOfKids) ) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.format(
			"PersonBean [firstName=%s, lastName=%s, birthDate=%s, birthTime=%s, married=%s, numberOfKids=%s, favouriteQuote=%s, email=%s]",
			firstName, lastName, birthDate, birthTime, married, numberOfKids, favouriteQuote, email);
	}
	
}
