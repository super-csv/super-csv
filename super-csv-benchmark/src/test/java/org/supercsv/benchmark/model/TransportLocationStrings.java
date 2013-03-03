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
package org.supercsv.benchmark.model;

/**
 * POJO to use without cell processors.
 * 
 * @author James Bassett
 */
public class TransportLocationStrings {
	
	private String atcoCode;
	private String easting;
	private String northing;
	private String longitude;
	private String latitude;
	private String commonName;
	private String identifier;
	private String direction;
	private String street;
	private String landmark;
	private String natGazId;
	private String natGazLocality;
	private String stopType;
	private String stopTypeAndName;
	private String iconColour;
	
	public String getAtcoCode() {
		return atcoCode;
	}
	
	public void setAtcoCode(String atcoCode) {
		this.atcoCode = atcoCode;
	}
	
	public String getEasting() {
		return easting;
	}
	
	public void setEasting(String easting) {
		this.easting = easting;
	}
	
	public String getNorthing() {
		return northing;
	}
	
	public void setNorthing(String northing) {
		this.northing = northing;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getCommonName() {
		return commonName;
	}
	
	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public String getStreet() {
		return street;
	}
	
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getLandmark() {
		return landmark;
	}
	
	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}
	
	public String getNatGazId() {
		return natGazId;
	}
	
	public void setNatGazId(String natGazId) {
		this.natGazId = natGazId;
	}
	
	public String getNatGazLocality() {
		return natGazLocality;
	}
	
	public void setNatGazLocality(String natGazLocality) {
		this.natGazLocality = natGazLocality;
	}
	
	public String getStopType() {
		return stopType;
	}
	
	public void setStopType(String stopType) {
		this.stopType = stopType;
	}
	
	public String getStopTypeAndName() {
		return stopTypeAndName;
	}
	
	public void setStopTypeAndName(String stopTypeAndName) {
		this.stopTypeAndName = stopTypeAndName;
	}
	
	public String getIconColour() {
		return iconColour;
	}
	
	public void setIconColour(String iconColour) {
		this.iconColour = iconColour;
	}
	
	public String[] toStringArray() {
		return new String[] { atcoCode, easting, northing, longitude, latitude, commonName, identifier, direction,
			street, landmark, natGazId, natGazLocality, stopType, stopTypeAndName, iconColour };
	}
	
}
