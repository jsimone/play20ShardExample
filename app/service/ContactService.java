package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Contact;
import play.db.DB;

public class ContactService {
	
	private static String getShardId(String zip) {
		Pattern p1 = Pattern.compile("[0-4]");
		Pattern p2 = Pattern.compile("[5-9]");
		String startingDigit = zip.substring(0, 1);
		
		if(p1.matcher(startingDigit).matches()) {			
			return "1";
		}
		if(p2.matcher(startingDigit).matches()) {
			return "2";
		}
		
		return null;
	}
	
	private static Connection getConnection(String zip) {
		String shardId = getShardId(zip);
		
		if(shardId != null) {
			return DB.getConnection("db" + shardId);
		} else {
			return null;
		}
	}

	private static boolean sameShard(String zip1, String zip2) {
		if(getShardId(zip1) == null) {
			return false;
		} else {
			return getShardId(zip1).equals(getShardId(zip2));
		}
	}
	
	private static List<Connection> getConnections() {
		List<Connection> connections = new ArrayList<Connection>();
		connections.add(DB.getConnection("db1"));
		connections.add(DB.getConnection("db2"));
		return connections;
	}
	
	private static void closeConnections(List<Connection> connections) throws SQLException {
		for (Connection connection : connections) {
			connection.close();
		}
	}
	
	private static List<Contact> listContacts(Connection conn) throws SQLException {
		List<Contact> contacts = new ArrayList<Contact>();
		String query = "select id, firstName, lastName, phone, address, zip from contacts";
		PreparedStatement stmt = conn.prepareStatement(query);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			contacts.add(Contact.createFromResultSet(rs));
		}
		
		return contacts;
	}
	
	public static List<Contact> listContacts() throws SQLException {
		List<Connection> connections = getConnections();
		List<Contact> contacts = new ArrayList<Contact>();

		try {			
			for (Connection connection : connections) {
				contacts.addAll(listContacts(connection));
			}
		} finally {
			closeConnections(connections);
		}
		
		return contacts;
	}
	
	public static Contact findContact(Long contactId, String zip) throws SQLException {
		Connection conn = null;
		try {
			conn = getConnection(zip);
			String query = "select id, firstName, lastName, phone, address, zip from contacts where id = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setLong(1, contactId);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return Contact.createFromResultSet(rs);
			}
		} finally {
			conn.close();
		}
		
		return null;
	}
	
	public static void createContact(Contact contact) throws SQLException {
		Connection conn = null;
		try {
			conn = getConnection(contact.getZip());
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
	
	public static void deleteContact(Long contactId, String zip) throws SQLException {
		Connection conn = null;
		try {
			conn = getConnection(zip);
			String query = "delete from contacts where id = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setLong(1, contactId);
			stmt.executeUpdate();
		} finally {
			conn.close();
		}		
	}
	
	private static void updateContact(Contact contact, Connection conn) throws SQLException {
		String query = "update contacts set firstName=?, lastName=?, phone=?, address=?, zip=? where id = ?";
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, contact.getFirstName());
		stmt.setString(2, contact.getLastName());
		stmt.setString(3, contact.getPhone());
		stmt.setString(4, contact.getAddress());
		stmt.setString(5, contact.getZip());
		stmt.setLong(6, contact.getId());
		stmt.executeUpdate();
	}
	
	public static void updateContact(Contact contact, String origZip) throws SQLException {
		if(!sameShard(origZip, contact.getZip())) {
			deleteContact(contact.getId(), origZip);
			createContact(contact);
		} else {
			Connection conn = null;
			try {
				conn = getConnection(origZip);
				updateContact(contact, conn);
			} finally {
				conn.close();
			}
		}
	}
}
