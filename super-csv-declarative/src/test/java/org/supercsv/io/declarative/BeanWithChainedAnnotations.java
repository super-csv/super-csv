package org.supercsv.io.declarative;

import org.supercsv.io.declarative.annotation.ConvertNullTo;
import org.supercsv.io.declarative.annotation.Trim;
import org.supercsv.io.declarative.annotation.Truncate;

public class BeanWithChainedAnnotations {
	@ConvertNullTo("test ")
	@Trim
	private String name;
	
	@Trim
	@Truncate(maxSize = 3)
	private String lastName;
	private int age;
	private double weight;
	
	public BeanWithChainedAnnotations() {
	}
	
	public BeanWithChainedAnnotations(String name, String lastName, int age, double weight) {
		this.name = name;
		this.lastName = lastName;
		this.age = age;
		this.weight = weight;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public int getAge() {
		return age;
	}
	
	public double getWeight() {
		return weight;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(weight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		BeanWithChainedAnnotations other = (BeanWithChainedAnnotations) obj;
		if( age != other.age )
			return false;
		if( lastName == null ) {
			if( other.lastName != null )
				return false;
		} else if( !lastName.equals(other.lastName) )
			return false;
		if( name == null ) {
			if( other.name != null )
				return false;
		} else if( !name.equals(other.name) )
			return false;
		if( Double.doubleToLongBits(weight) != Double.doubleToLongBits(other.weight) )
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "SimpleBeanWithSimpleAnnotations [name=" + name + ", lastName=" + lastName + ", age=" + age
			+ ", weight=" + weight + "]";
	}
	
}
