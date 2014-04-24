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
 * A bean to use when testing reflection.
 * 
 * @author James Bassett
 */
public class ReflectionBean {
	
	private String name;
	private Number favouriteNumber;
	private Number overloaded;
	private boolean primitiveBoolean;
	private int primitiveInt;
	private short primitiveShort;
	private long primitiveLong;
	private double primitiveDouble;
	private float primitiveFloat;
	private char primitiveChar;
	private byte primitiveByte;
	private Boolean booleanWrapper;
	private Integer integerWrapper;
	private Short shortWrapper;
	private Long longWrapper;
	private Double doubleWrapper;
	private Float floatWrapper;
	private Character charWrapper;
	private Byte byteWrapper;
	private Boolean booleanWrapper2;
	private String URL;
	private String iPad;
	
	public String getName() {
		
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Number getFavouriteNumber() {
		return favouriteNumber;
	}
	
	public void setFavouriteNumber(Number favouriteNumber) {
		this.favouriteNumber = favouriteNumber;
	}
	
	public Number getOverloaded() {
		return overloaded;
	}
	
	public void setOverloaded(Integer overloaded) {
		throw new RuntimeException("this overloaded setter shouldn't be invoked!");
	}
	
	public void setOverloaded(Number overloaded) {
		this.overloaded = overloaded;
	}
	
	public int getPrimitiveInt() {
		return primitiveInt;
	}
	
	public void setPrimitiveInt(int primitiveInt) {
		this.primitiveInt = primitiveInt;
	}
	
	public short getPrimitiveShort() {
		return primitiveShort;
	}
	
	public void setPrimitiveShort(short primitiveShort) {
		this.primitiveShort = primitiveShort;
	}
	
	public long getPrimitiveLong() {
		return primitiveLong;
	}
	
	public void setPrimitiveLong(long primitiveLong) {
		this.primitiveLong = primitiveLong;
	}
	
	public double getPrimitiveDouble() {
		return primitiveDouble;
	}
	
	public void setPrimitiveDouble(double primitiveDouble) {
		this.primitiveDouble = primitiveDouble;
	}
	
	public float getPrimitiveFloat() {
		return primitiveFloat;
	}
	
	public void setPrimitiveFloat(float primitiveFloat) {
		this.primitiveFloat = primitiveFloat;
	}
	
	public char getPrimitiveChar() {
		return primitiveChar;
	}
	
	public void setPrimitiveChar(char primitiveChar) {
		this.primitiveChar = primitiveChar;
	}
	
	public byte getPrimitiveByte() {
		return primitiveByte;
	}
	
	public void setPrimitiveByte(byte primitiveByte) {
		this.primitiveByte = primitiveByte;
	}
	
	public Integer getIntegerWrapper() {
		return integerWrapper;
	}
	
	public void setIntegerWrapper(Integer integerWrapper) {
		this.integerWrapper = integerWrapper;
	}
	
	public Short getShortWrapper() {
		return shortWrapper;
	}
	
	public void setShortWrapper(Short shortWrapper) {
		this.shortWrapper = shortWrapper;
	}
	
	public Long getLongWrapper() {
		return longWrapper;
	}
	
	public void setLongWrapper(Long longWrapper) {
		this.longWrapper = longWrapper;
	}
	
	public Double getDoubleWrapper() {
		return doubleWrapper;
	}
	
	public void setDoubleWrapper(Double doubleWrapper) {
		this.doubleWrapper = doubleWrapper;
	}
	
	public Float getFloatWrapper() {
		return floatWrapper;
	}
	
	public void setFloatWrapper(Float floatWrapper) {
		this.floatWrapper = floatWrapper;
	}
	
	public Character getCharWrapper() {
		return charWrapper;
	}
	
	public void setCharWrapper(Character charWrapper) {
		this.charWrapper = charWrapper;
	}
	
	public Byte getByteWrapper() {
		return byteWrapper;
	}
	
	public void setByteWrapper(Byte byteWrapper) {
		this.byteWrapper = byteWrapper;
	}
	
	public void getPrimitiveBoolean() {
		throw new RuntimeException("this method isn't a getter (no return type) and should never be invoked");
	}
	
	public boolean getPrimitiveBoolean(boolean dummy) {
		throw new RuntimeException("this method isn't a getter (it has a param) and should never be invoked");
	}
	
	public boolean isPrimitiveBoolean() {
		return primitiveBoolean;
	}
	
	public void setPrimitiveBoolean(boolean primitiveBoolean) {
		this.primitiveBoolean = primitiveBoolean;
	}
	
	public void setPrimitiveBoolean() {
		throw new RuntimeException("this method isn't a setter (no param) and should never be invoked");
	}
	
	public Boolean getBooleanWrapper() {
		return booleanWrapper;
	}
	
	public void setBooleanWrapper(Boolean booleanWrapper) {
		this.booleanWrapper = booleanWrapper;
	}
	
	public Boolean isBooleanWrapper2() {
		return booleanWrapper2;
	}
	
	public void setBooleanWrapper2(Boolean booleanWrapper2) {
		this.booleanWrapper2 = booleanWrapper2;
	}
	
	public String isInvalid() {
		throw new RuntimeException("this method isn't a getter and should never be invoked");
	}
	
	public String getURL() {
		return URL;
	}

	public void setURL(String URL) {
		this.URL = URL;
	}
	
	public String getiPad() {
		return iPad;
	}
	
	public void setiPad(String iPad) {
		this.iPad = iPad;
	}
	
}
