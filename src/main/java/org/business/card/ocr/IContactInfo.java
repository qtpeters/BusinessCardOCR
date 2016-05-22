package org.business.card.ocr;

public interface IContactInfo {
	
	/**
	 * @return The full name of the individual (eg. John Smith, Susan Malick)
	 */
	String getName();
	
	/**
	 * @return The phone number formatted as a sequence of digits with no punctuation
	 */
    String getPhoneNumber();
    
    /**
     * @return The email address of the individual
     */
    String getEmailAddress();

}
