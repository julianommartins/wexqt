package com.ibm.services.tools.wexws.domain;

public class Location {
	private String IOT; // North America
	private String IMT; // United States
	private String country; // United States
	private String state; // Texas
	private String city; // Austin
	public String getIOT() {
		return IOT;
	}
	public void setIOT(String iOT) {
		IOT = iOT;
	}
	public String getIMT() {
		return IMT;
	}
	public void setIMT(String iMT) {
		IMT = iMT;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public Location(String iOT, String iMT, String country, String state, String city) {
		super();
		IOT = iOT;
		IMT = iMT;
		this.country = country;
		this.state = state;
		this.city = city;
	}
	
	public Location(String iOT, String iMT, String country, String city) {
		super();
		IOT = iOT;
		IMT = iMT;
		this.country = country;
		this.city = city;
	}
	
	public Location() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IMT == null) ? 0 : IMT.hashCode());
		result = prime * result + ((IOT == null) ? 0 : IOT.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (IMT == null) {
			if (other.IMT != null)
				return false;
		} else if (!IMT.equals(other.IMT))
			return false;
		if (IOT == null) {
			if (other.IOT != null)
				return false;
		} else if (!IOT.equals(other.IOT))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
}
