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
package org.supercsv.io.reflection;

import org.supercsv.mock.CustomerStringBean;

/**
 * non-public Bean with String values only - for testing reading/writing without processors.
 * 
 * @author James Bassett
 * @author Pietro Aragona
 * @author Fabian Seifert
 */
class CustomerStringProtectedBean {
	
	private String customerNo;
	private String firstName;
	private String lastName;
	private String birthDate;
	private String birthTime;
	private String mailingAddress;
	private String married;
	private String numberOfKids;
	private String favouriteQuote;
	private String email;
	private String loyaltyPoints;
	
	/**
	 * Default Constructor.
	 */
	protected CustomerStringProtectedBean() {
	}
	
	/**
	 * Constructs a CustomerStringProtectedBean from a CustomerStringBean.
	 * 
	 * @param customerStringBean
	 */
	protected CustomerStringProtectedBean(CustomerStringBean customerStringBean) {
		this.customerNo = customerStringBean.getCustomerNo();
		this.firstName = customerStringBean.getFirstName();
		this.lastName = customerStringBean.getLastName();
		this.birthDate = customerStringBean.getBirthDate();
		this.birthTime = customerStringBean.getBirthTime();
		this.mailingAddress = customerStringBean.getMailingAddress();
		this.married = customerStringBean.getMarried();
		this.numberOfKids = customerStringBean.getNumberOfKids();
		this.favouriteQuote = customerStringBean.getFavouriteQuote();
		this.email = customerStringBean.getEmail();
		this.loyaltyPoints = customerStringBean.getLoyaltyPoints();
	}
	
	/**
	 * @return the customerNo
	 */
	protected String getCustomerNo() {
		return customerNo;
	}
	
	/**
	 * @param customerNo
	 *            the customerNo to set
	 */
	protected void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	
	/**
	 * @return the firstName
	 */
	protected String getFirstName() {
		return firstName;
	}
	
	/**
	 * @param firstName
	 *            the firstName to set
	 */
	protected void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/**
	 * @return the lastName
	 */
	protected String getLastName() {
		return lastName;
	}
	
	/**
	 * @param lastName
	 *            the lastName to set
	 */
	protected void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	/**
	 * @return the birthDate
	 */
	protected String getBirthDate() {
		return birthDate;
	}
	
	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	protected void setBirthTime(String birthTime) {
		this.birthTime = birthTime;
	}
	
	/**
	 * @return the birthDate
	 */
	protected String getBirthTime() {
		return birthTime;
	}
	
	/**
	 * @param birthDate
	 *            the birthDate to set
	 */
	protected void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	/**
	 * @return the mailingAddress
	 */
	protected String getMailingAddress() {
		return mailingAddress;
	}
	
	/**
	 * @param mailingAddress
	 *            the mailingAddress to set
	 */
	protected void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}
	
	/**
	 * @return the married
	 */
	protected String getMarried() {
		return married;
	}
	
	/**
	 * @param married
	 *            the married to set
	 */
	protected void setMarried(String married) {
		this.married = married;
	}
	
	/**
	 * @return the numberOfKids
	 */
	protected String getNumberOfKids() {
		return numberOfKids;
	}
	
	/**
	 * @param numberOfKids
	 *            the numberOfKids to set
	 */
	protected void setNumberOfKids(String numberOfKids) {
		this.numberOfKids = numberOfKids;
	}
	
	/**
	 * @return the favouriteQuote
	 */
	protected String getFavouriteQuote() {
		return favouriteQuote;
	}
	
	/**
	 * @param favouriteQuote
	 *            the favouriteQuote to set
	 */
	protected void setFavouriteQuote(String favouriteQuote) {
		this.favouriteQuote = favouriteQuote;
	}
	
	/**
	 * @return the email
	 */
	protected String getEmail() {
		return email;
	}
	
	/**
	 * @param email
	 *            the email to set
	 */
	protected void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * @return the loyaltyPoints
	 */
	public String getLoyaltyPoints() {
		return loyaltyPoints;
	}
	
	/**
	 * @param loyaltyPoints
	 *            the loyaltyPoints to set
	 */
	public void setLoyaltyPoints(String loyaltyPoints) {
		this.loyaltyPoints = loyaltyPoints;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((birthTime == null) ? 0 : birthTime.hashCode());
		result = prime * result + ((customerNo == null) ? 0 : customerNo.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((favouriteQuote == null) ? 0 : favouriteQuote.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((loyaltyPoints == null) ? 0 : loyaltyPoints.hashCode());
		result = prime * result + ((mailingAddress == null) ? 0 : mailingAddress.hashCode());
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
		if( !(obj instanceof CustomerStringProtectedBean) ) {
			return false;
		}
		CustomerStringProtectedBean other = (CustomerStringProtectedBean) obj;
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
		if( customerNo == null ) {
			if( other.customerNo != null ) {
				return false;
			}
		} else if( !customerNo.equals(other.customerNo) ) {
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
		if( loyaltyPoints == null ) {
			if( other.loyaltyPoints != null ) {
				return false;
			}
		} else if( !loyaltyPoints.equals(other.loyaltyPoints) ) {
			return false;
		}
		if( mailingAddress == null ) {
			if( other.mailingAddress != null ) {
				return false;
			}
		} else if( !mailingAddress.equals(other.mailingAddress) ) {
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
	
}
