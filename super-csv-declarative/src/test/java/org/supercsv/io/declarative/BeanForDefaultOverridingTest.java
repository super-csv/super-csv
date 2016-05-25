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
package org.supercsv.io.declarative;

import org.supercsv.io.declarative.annotation.ParseBool;

/**
 * Test class for overriding default processors
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
public class BeanForDefaultOverridingTest {
	@ParseBool(trueValue = "J", falseValue = "N")
	private boolean myBoolProperty;
	
	public BeanForDefaultOverridingTest() {
	}
	
	public BeanForDefaultOverridingTest(boolean myBoolProperty) {
		this.myBoolProperty = myBoolProperty;
	}
	
	public boolean isMyBoolProperty() {
		return myBoolProperty;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (myBoolProperty ? 1231 : 1237);
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( this == obj )
			return true;
		if( obj == null )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		BeanForDefaultOverridingTest other = (BeanForDefaultOverridingTest) obj;
		if( myBoolProperty != other.myBoolProperty )
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "BeanForDefaultOverridingTest [myBoolProperty=" + myBoolProperty + "]";
	}
	
}
