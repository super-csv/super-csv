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
 * POJO to use with cell processors.
 * 
 * @author James Bassett
 */
public class TransportLocation {
	
	private String atcoCode;
	private Double easting;
	private Double northing;
	private Double longitude;
	private Double latitude;
	private String commonName;
	private String identifier;
	private String direction;
	private String street;
	private String landmark;
	private String natGazId;
	private String natGazLocality;
	private StopType stopType;
	private StopTypeAndName stopTypeAndName;
	private IconColour iconColour;
	
	public String getAtcoCode() {
		return atcoCode;
	}
	
	public void setAtcoCode(String atcoCode) {
		this.atcoCode = atcoCode;
	}
	
	public Double getEasting() {
		return easting;
	}
	
	public void setEasting(Double easting) {
		this.easting = easting;
	}
	
	public Double getNorthing() {
		return northing;
	}
	
	public void setNorthing(Double northing) {
		this.northing = northing;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
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
	
	public StopType getStopType() {
		return stopType;
	}
	
	public void setStopType(StopType stopType) {
		this.stopType = stopType;
	}
	
	public StopTypeAndName getStopTypeAndName() {
		return stopTypeAndName;
	}
	
	public void setStopTypeAndName(StopTypeAndName stopTypeAndName) {
		this.stopTypeAndName = stopTypeAndName;
	}
	
	public IconColour getIconColour() {
		return iconColour;
	}
	
	public void setIconColour(IconColour iconColour) {
		this.iconColour = iconColour;
	}
	
	public Object[] toObjectArray() {
		return new Object[] { atcoCode, easting, northing, longitude, latitude, commonName, identifier, direction,
			street, landmark, natGazId, natGazLocality, stopType, stopTypeAndName, iconColour };
	}
	
	public String[] toStringArray() {
		return new String[] { atcoCode, easting != null ? easting.toString() : null, northing != null ? northing.toString() : null, longitude != null ? longitude.toString() : null, latitude != null ? latitude.toString() : null, commonName, identifier, direction,
			street, landmark, natGazId, natGazLocality, stopType != null ? stopType.toString() : null, stopTypeAndName != null ? stopTypeAndName.toString() : null, iconColour != null ? iconColour.toString() : null };
	}

	@Override
	public String toString() {
		return String
			.format(
				"TransportLocation [atcoCode=%s, easting=%s, northing=%s, longitude=%s, latitude=%s, commonName=%s, identifier=%s, direction=%s, street=%s, landmark=%s, natGazId=%s, natGazLocality=%s, stopType=%s, stopTypeAndName=%s, iconColour=%s]",
				atcoCode, easting, northing, longitude, latitude, commonName, identifier, direction, street, landmark,
				natGazId, natGazLocality, stopType, stopTypeAndName, iconColour);
	}
	
	
}
