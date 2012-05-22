package org.rmm.representation;

import org.rmm.model.Contact;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ContactRepresentation {

	public String asJSON(final Contact contact) {
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("contact", Contact.class);

		return xstream.toXML(contact);
	}
	
	public String asXML(final Contact contact) {
		XStream xstream = new XStream(new DomDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		xstream.alias("contact", Contact.class);

		return xstream.toXML(contact);
	}
}