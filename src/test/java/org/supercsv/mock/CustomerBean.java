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

import java.util.Date;

/**
 * Superclass bean to use when testing bean reading/writing.
 * 
 * @author James Bassett
 */
public class CustomerBean extends PersonBean implements Customer {
	
	private String customerNo;
	
	private long loyaltyPoints;
	
	private String mailingAddress;
	
	/**
	 * Default Constructor.
	 */
	public CustomerBean() {
	}
	
	/**
	 * Constructs a CustomerBean.
	 * 
	 * @param customerNo
	 * @param firstName
	 * @param lastName
	 * @param birthDate
	 * @param mailingAddress
	 * @param married
	 * @param numberOfKids
	 * @param favouriteQuote
	 * @param email
	 * @param loyaltyPoints
	 */
	public CustomerBean(final String customerNo, final String firstName, final String lastName, final Date birthDate,
		final String mailingAddress, final Boolean married, final Integer numberOfKids, final String favouriteQuote,
		final String email, final long loyaltyPoints) {
		super(firstName, lastName, birthDate, married, numberOfKids, favouriteQuote, email);
		this.customerNo = customerNo;
		this.loyaltyPoints = loyaltyPoints;
		this.mailingAddress = mailingAddress;
	}
	
	/**
	 * @return the customerNo
	 */
	public String getCustomerNo() {
		return customerNo;
	}
	
	/**
	 * @param customerNo
	 *            the customerNo to set
	 */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	
	/**
	 * @return the loyaltyPoints
	 */
	public long getLoyaltyPoints() {
		return loyaltyPoints;
	}
	
	/**
	 * @param loyaltyPoints
	 *            the loyaltyPoints to set
	 */
	public void setLoyaltyPoints(long loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	
	/**
	 * @return the mailingAddress
	 */
	public String getMailingAddress() {
		return mailingAddress;
	}
	
	/**
	 * @param mailingAddress
	 *            the mailingAddress to set
	 */
	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((customerNo == null) ? 0 : customerNo.hashCode());
		result = prime * result + (int) (loyaltyPoints ^ (loyaltyPoints >>> 32));
		result = prime * result + ((mailingAddress == null) ? 0 : mailingAddress.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(final Object obj) {
		if( this == obj ) {
			return true;
		}
		if( !super.equals(obj) ) {
			return false;
		}
		if( !(obj instanceof CustomerBean) ) {
			return false;
		}
		CustomerBean other = (CustomerBean) obj;
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
	
}
