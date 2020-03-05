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
 * Superclass pojo to use when testing bean reading/writing.
 * 
 * @author James Bassett
 * @author Pietro Aragona
 * @author Chen Guoping
 */
public class CustomerPojo {

	private String customerNo;
	private String firstName;
	private String lastName;
	private Date birthDate;
	private Time birthTime;
	private String mailingAddress;
	private Boolean married;
	private Integer numberOfKids;
	private String favouriteQuote;
	private String email;
	private long loyaltyPoints;
	
	/**
	 * Default Constructor.
	 */
	public CustomerPojo() {
	}
	
	/**
	 * Constructs a CustomerPojo.
	 * 
	 * @param customerNo
	 * @param firstName
	 * @param lastName
	 * @param birthDate
	 * @param birthTime
	 * @param mailingAddress
	 * @param married
	 * @param numberOfKids
	 * @param favouriteQuote
	 * @param email
	 * @param loyaltyPoints
	 */
	public CustomerPojo(final String customerNo, final String firstName, final String lastName, final Date birthDate,
		final Time birthTime, final String mailingAddress, final Boolean married, final Integer numberOfKids,
		final String favouriteQuote, final String email, final long loyaltyPoints) {

		this.customerNo = customerNo;
		this.firstName = firstName;
		this.lastName = lastName;
		this.birthDate = birthDate;
		this.birthTime = birthTime;
		this.mailingAddress = mailingAddress;
		this.married = married;
		this.numberOfKids = numberOfKids;
		this.favouriteQuote = favouriteQuote;
		this.email = email;
		this.loyaltyPoints = loyaltyPoints;
	}

	public boolean equals(final Object obj) {
		if( this == obj ) {
			return true;
		}
		if( !super.equals(obj) ) {
			return false;
		}
		if( !(obj instanceof CustomerPojo) ) {
			return false;
		}
		CustomerPojo other = (CustomerPojo) obj;
		if( customerNo == null ) {
			if( other.customerNo != null ) {
				return false;
			}
		} else if( !customerNo.equals(other.customerNo) ) {
			return false;
		}
		if( loyaltyPoints != other.loyaltyPoints ) {
			return false;
		}
		if( mailingAddress == null ) {
			if( other.mailingAddress != null ) {
				return false;
			}
		} else if( !mailingAddress.equals(other.mailingAddress) ) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return String.format(
			"CustomerPojo [customerNo=%s, firstName=%s, lastName=%s, birthDate=%s, birthTime=%s, mailingAddress=%s, married=%s, numberOfKids=%s, favouriteQuote=%s, email=%s, loyaltyPoints=%s]",
			customerNo, firstName, lastName, birthDate, birthTime, mailingAddress, married, numberOfKids, favouriteQuote, email, loyaltyPoints);
	}
}
