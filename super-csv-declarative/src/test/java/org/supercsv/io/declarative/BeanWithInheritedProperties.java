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

/**
 * Test class for declarative mapping
 * 
 * @since 2.5
 * @author Dominik Schlosser
 */
public class BeanWithInheritedProperties extends BeanWithoutAnnotations {
	private String note;
	
	public BeanWithInheritedProperties() {
	}
	
	public BeanWithInheritedProperties(String name, String lastName, int age, double weight, String note) {
		super(name, lastName, age, weight);
		this.note = note;
	}
	
	public String getNote() {
		return note;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if( this == obj )
			return true;
		if( !super.equals(obj) )
			return false;
		if( getClass() != obj.getClass() )
			return false;
		BeanWithInheritedProperties other = (BeanWithInheritedProperties) obj;
		if( note == null ) {
			if( other.note != null )
				return false;
		} else if( !note.equals(other.note) )
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "BeanWithInheritedProperties [note=" + note + ", getName()=" + getName() + ", getLastName()="
			+ getLastName() + ", getAge()=" + getAge() + ", getWeight()=" + getWeight() + ", getClass()=" + getClass()
			+ ", toString()=" + super.toString() + "]";
	}
	
}
