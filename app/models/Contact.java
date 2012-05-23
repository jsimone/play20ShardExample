package models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Contact {
	
	private Long id;
	private String firstName;
	private String lastName;
	private String phone;
	private String address;
	private String zip;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}

	public static Contact createFromResultSet(ResultSet rs) throws SQLException {
		Contact contact = new Contact();
		contact.setId(rs.getLong("id"));
		contact.setFirstName(rs.getString("firstName"));
		contact.setLastName(rs.getString("lastName"));
		contact.setPhone(rs.getString("phone"));
		contact.setAddress(rs.getString("address"));
		return contact;
	}
	
}
