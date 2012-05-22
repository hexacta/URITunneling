package org.rmm.db;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.rmm.model.Contact;

public enum Repository {
	INSTANCE;
	
	private Map<String,Contact> repo = new ConcurrentHashMap<String, Contact>(10);
	
	
	public void addContact(final Contact contact){
		this.repo.put(contact.getName(), contact);
	}
	
	public Contact retrieveContact(final String name){
		return this.repo.get(name);
	}
	
	public void removeContact(String name){
		this.repo.remove(name);
	}
	
	public Collection<Contact> getContacts(){
		return this.repo.values();
	}

	public void updateContact(Contact contact) {
		if (this.repo.containsKey(contact.getName())){
			this.repo.put(contact.getName(), contact);
		}
	}

}
