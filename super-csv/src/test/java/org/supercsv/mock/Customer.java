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

/**
 * Interface for a customer (for bean reading/writing tests).
 * 
 * @author James Bassett
 */
public interface Customer {
	
	public String getCustomerNo();
	
	public void setCustomerNo(String customerNo);
	
	public long getLoyaltyPoints();
	
	public void setLoyaltyPoints(long loyaltyPoints);
	
	public String getMailingAddress();
	
	public void setMailingAddress(String mailingAddress);
	
}
