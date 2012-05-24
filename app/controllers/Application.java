package controllers;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import models.Contact;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http.RequestBody;
import service.ContactService;
import views.html.index;

public class Application extends Controller {
  
  public static Result index() {
	  List<Contact> contacts = null;
	try {
		contacts = ContactService.listContacts();
	} catch(SQLException e) {
		e.printStackTrace();
		return internalServerError();
	}
    return ok(index.render(contacts));
  }
  
  public static Result newContact() {
	  return ok(views.html.create.render());
  }
  
  public static Result editContact(Long contactId, String zip) {
	  Contact contact = null;
		try {
			contact = ContactService.findContact(contactId, zip);
		} catch(SQLException e) {
			e.printStackTrace();
			return internalServerError();
		}
	  return ok(views.html.edit.render(contact));
  }
  
  public static Result update(Long contactId, String zip) {
	  Contact contact = new Contact();
	  RequestBody body = request().body();
	  Map<String, String[]> postData = body.asFormUrlEncoded();
	  contact.setId(contactId);
	  contact.setFirstName(postData.get("firstName")[0]);
	  contact.setLastName(postData.get("lastName")[0]);
	  contact.setPhone(postData.get("phone")[0]);
	  contact.setAddress(postData.get("address")[0]);
	  contact.setZip(postData.get("zip")[0]);
	  String origZip = postData.get("origZip")[0];
	  try {
		  ContactService.updateContact(contact, origZip);
	  } catch(SQLException e) {
		  e.printStackTrace();
		  return internalServerError();
	  }
	  
	  return redirect("/");
  }
  
  public static Result insert() {
	  Contact contact = new Contact();
	  RequestBody body = request().body();
	  Map<String, String[]> postData = body.asFormUrlEncoded();
	  contact.setFirstName(postData.get("firstName")[0]);
	  contact.setLastName(postData.get("lastName")[0]);
	  contact.setPhone(postData.get("phone")[0]);
	  contact.setAddress(postData.get("address")[0]);
	  contact.setZip(postData.get("zip")[0]);
		try {
			ContactService.createContact(contact);
		} catch(SQLException e) {
			e.printStackTrace();
			return internalServerError();
		}
	  return redirect("/");
  }
  
}