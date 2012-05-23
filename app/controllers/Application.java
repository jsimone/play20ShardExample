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
	try {
		List<Contact> contacts = ContactService.listContacts();
	} catch(SQLException e) {
		e.printStackTrace();
		return internalServerError();
	}
    return ok(index.render("Your new application is ready."));
  }
  
  public static Result insert() {
	  Contact contact = new Contact();
	  RequestBody body = request().body();
	  Map<String, String[]> postData = body.asFormUrlEncoded();
	  contact.setFirstName(postData.get("firstName")[0]);
	  contact.setFirstName(postData.get("lastName")[0]);
	  contact.setFirstName(postData.get("phone")[0]);
	  contact.setFirstName(postData.get("address")[0]);
	  contact.setFirstName(postData.get("zip")[0]);
		try {
			ContactService.createContact(contact);
		} catch(SQLException e) {
			e.printStackTrace();
			return internalServerError();
		}
	  return redirect("/");
  }
  
  public static Result update(Long contactId) {
	  return ok();
  }
  
}