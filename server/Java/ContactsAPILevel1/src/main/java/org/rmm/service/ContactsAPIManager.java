package org.rmm.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.rmm.db.Repository;
import org.rmm.model.Contact;
import org.rmm.representation.ContactRepresentation;

import com.sun.xml.internal.ws.util.StringUtils;

/**
 * WebAPI using URI tunneling, Overloading GET. 
 *	GET	/GetContacts
 *	GET	/AddContact?contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}
 *	GET	/GetContactDetails?contactName={contactName}
 *	GET	/UpdateContact?contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}
 *	GET	/CancelContact?contactName={contactName} 
 * @author jvelilla
 * 
 */

public class ContactsAPIManager extends HttpServlet {

	private static final String APPLICATION_JSON = "application/json";
	private static final Logger LOG = Logger
			.getLogger(ContactsAPIManager.class);
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		String qString = req.getQueryString();
		String pathInfo = req.getPathInfo();

		if ("/AddContact".equals(pathInfo)) {
			this.addContact(qString, res);
		} else if ("/GetContacts".equals(pathInfo)) {
			this.getContacts(res);
		} else if ("/GetContactDetails".equals(pathInfo)) {
			this.getContactDetails(qString, res);
		} else if ("/UpdateContact".equals(pathInfo)) {
			this.updateContact(qString, res);
		}else if ("/CancelContact".equals(pathInfo)) {
			this.cancelOrder(qString, res);
		} else {
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	/**
	 * GET	/CancelContact?contactName={contactName} 	
	 * @param qString
	 * @param res
	 */
	private void cancelOrder(String qString, HttpServletResponse res) {
		try {
			String[] param = qString.split("=");
			Repository.INSTANCE.removeContact(param[1]);
			res.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * GET	/UpdateContact?contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}
	 * @param qString
	 * @param res
	 */
	private void updateContact(String qString, HttpServletResponse res) {
		try {
			Contact contact = new Contact();
			Map<String, String> qValues = new HashMap<String, String>(10);

			String[] split = qString.split("&");
			LOG.info(split);
			for (String qparam : split) {
				String[] q = qparam.split("=");
				qValues.put(q[0], q[1]);
			}
			contact.setAddress(qValues.get("contactAddress"));
			contact.setCity(qValues.get("contactCity"));
			contact.setEmail(qValues.get("contactEmail"));
			contact.setName(qValues.get("contactName"));
			contact.setSkype(qValues.get("contactSkype"));
			contact.setState(qValues.get("contactState"));
			contact.setTwitter(qValues.get("contactTwitter"));
			contact.setZip(qValues.get("contactZip"));
			
			res.setContentType(APPLICATION_JSON);
			res.setStatus(HttpServletResponse.SC_OK);
			Repository.INSTANCE.updateContact(contact);
			PrintWriter out = res.getWriter();
			out.print(new ContactRepresentation().asJSON(Repository.INSTANCE.retrieveContact(contact.getName())));
		} catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

	}
	
	/**
	 * GET	/GetContacts
	 * @param res
	 */
	private void getContacts(HttpServletResponse res) {
		try {
			PrintWriter out = res.getWriter();
			Collection<Contact> contacts = Repository.INSTANCE.getContacts();
			res.setContentType(APPLICATION_JSON);
			String response = "[";
			for (Contact contact : contacts) {
				response = response + new ContactRepresentation().asJSON(contact);
			}
			response = response + "]";
			out.print(response);
			res.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * GET	/GetContactDetails?contactName={contactName}
	 * @param qString
	 * @param res
	 */
	private void getContactDetails(String qString, HttpServletResponse res) {
		try {
			String[] param = qString.split("=");
			Contact contact = Repository.INSTANCE.retrieveContact(param[1]);
			res.setContentType(APPLICATION_JSON);
			PrintWriter out = res.getWriter();
			out.print(new ContactRepresentation().asJSON(contact));
			res.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * GET	/AddContact?contactName={contactName}&contactAddress={contactName}&contactCity={contactCity}&contactState={contactState}&contactEmail={contactEmail}&contactTwitter={contactTwitter}&contactSkype={contactSkype}
	 * @param qString
	 * @param res
	 */
	private void addContact(String qString, HttpServletResponse res) {
		try {
			Contact contact = new Contact();
			Map<String, String> qValues = new HashMap<String, String>(10);

			String[] split = qString.split("&");
			LOG.info(split);
			for (String qparam : split) {
				String[] q = qparam.split("=");
				qValues.put(q[0], q[1]);
			}
			contact.setAddress(qValues.get("contactAddress"));
			contact.setCity(qValues.get("contactCity"));
			contact.setEmail(qValues.get("contactEmail"));
			contact.setName(qValues.get("contactName"));
			contact.setSkype(qValues.get("contactSkype"));
			contact.setState(qValues.get("contactState"));
			contact.setTwitter(qValues.get("contactTwitter"));
			contact.setZip(qValues.get("contactZip"));
			
			Repository.INSTANCE.addContact(contact);
			res.setContentType(APPLICATION_JSON);
			PrintWriter out = res.getWriter();
			out.print(new ContactRepresentation().asJSON(Repository.INSTANCE.retrieveContact(contact.getName())));
			res.setStatus(HttpServletResponse.SC_CREATED);
		} catch (Exception e) {
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
