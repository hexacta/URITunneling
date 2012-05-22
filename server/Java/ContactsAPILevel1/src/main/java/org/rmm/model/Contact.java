package org.rmm.model;

public class Contact {

	private String name;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String email;
	private String twitter;
	private String skype;

	public String getName() {
		return this.name;
	}

	public String getAddress() {
		return this.address;
	}

	public String getCity() {
		return this.city;
	}

	public String getState() {
		return this.state;
	}

	public String getZip() {
		return this.zip;
	}

	public String getEmail() {
		return this.email;
	}

	public String getTwitter() {
		return this.twitter;
	}

	public String getSkype() {
		return this.skype;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public void setCity(final String city) {
		this.city = city;
	}

	public void setState(final String state) {
		this.state = state;
	}

	public void setZip(final String zip) {
		this.zip = zip;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setTwitter(final String twitter) {
		this.twitter = twitter;
	}

	public void setSkype(final String skype) {
		this.skype = skype;
	}

	@Override
	public String toString() {
		return "Contact [name=" + name + ", address=" + address + ", city="
				+ city + ", state=" + state + ", zip=" + zip + ", email="
				+ email + ", twitter=" + twitter + ", skype=" + skype + "]";
	}

}
