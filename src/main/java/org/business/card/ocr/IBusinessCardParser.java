package org.business.card.ocr;

public interface IBusinessCardParser {
	
	/**
	 * @param document is the pre-parsed OCR input 
	 * @return a correctly formatted set of contact info
	 */
	IContactInfo getContactInfo(String document);
}
