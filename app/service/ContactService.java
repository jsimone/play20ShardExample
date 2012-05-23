package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import models.Contact;
import play.db.DB;

public class ContactService {

	public static Connection getConnection() {
		return DB.getConnection("db1");
	}
	
	public static List<Contact> listContacts() throws SQLException {
		List<Contact> contacts = new ArrayList<Contact>();
		Connection conn = null;
		try {
			getConnection();
			String query = "select id, firstName, lastName, phone, address, zip from contacts";
			PreparedStatement stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				contacts.add(Contact.createFromResultSet(rs));
			}
		} finally {
			conn.close();
		}
		
		return contacts;
	}
	
	public static void createContact(Contact contact) throws SQLException {
		Connection conn = null;
		try {
			conn = getConnection();
			String query = "insert into contacts (firstName, lastName, phone, address, zip) values (?,?,?,?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, contact.getFirstName());
			stmt.setString(2, contact.getLastName());
			stmt.setString(3, contact.getPhone());
			stmt.setString(4, contact.getAddress());
			stmt.setString(5, contact.getZip());
			stmt.executeUpdate();
		} finally {
			conn.close();
		}
		
	}
}
