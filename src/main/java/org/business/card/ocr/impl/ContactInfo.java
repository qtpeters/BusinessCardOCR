package org.business.card.ocr.impl;

import org.business.card.ocr.IContactInfo;

/**
 * Model object that represents a ContactInfo.
 * It is immutable and has an overriden toString() method
 * for convenience;
 * 
 * @author qtpeters
 */
public class ContactInfo implements IContactInfo {

	private String name;
	private String phoneNumber;
	private String emailAddress;
	
	public ContactInfo( String name, String phoneNumber, String emailAddress ) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}
	
	public String getName() {
		return this.name;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getEmailAddress() {
		return this.emailAddress;
	}
	
	public String toString() {
		StringBuilder sb = new  StringBuilder();
		sb.append( "Name: " ).append( this.getName() ).append( "\n" );
		sb.append( "Phone: " ).append( this.getPhoneNumber() ).append( "\n" );
		sb.append( "Email: " ).append( this.getEmailAddress() ).append( "\n" );
		
		return sb.toString();
	}
}
