package org.business.card.ocr.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;

import org.business.card.ocr.IBusinessCardParser;
import org.business.card.ocr.IContactInfo;

public class BusinessCardParser implements IBusinessCardParser {

	// LinkedHashSet has O(1) for contains() operation.
	private static final Set<String> firstNames = new LinkedHashSet<>();
	private static final String FIRST_NAME_FILE = "/firstNames.txt";
	
	static {
		
		try ( BufferedReader br = new BufferedReader( 
			new InputStreamReader( 
				BusinessCardParser.class.getResourceAsStream( 
					FIRST_NAME_FILE ) ) ) ) {
			String firstName = "";
			while ( ( firstName = br.readLine() ) != null ) {
				firstNames.add( firstName );
			}
			
		} catch ( IOException ioe ) {
			ioe.printStackTrace();
		}
	}
	
	// nameRegex does not take into account the possibility that 
	// a middle name or middle initial could be present.
	// nameRegex would not work on Latin names because there are four.
	private String nameRegex = "\\b\\w+\\s+\\w+\\b";
	private String emailRegex = "\\b[A-Za-z0-9._%+-]+@.*[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b"; 
	private String telephoneRegex = "^(?:(?:\\+?1\\s*(?:[.-]\\s*)?)?(?:\\(\\s*("
			+ "[2-9]1[02-9]|[2-9][02-8]1|[2-9][02-8][02-9])\\s*\\)|([2-9]1[02-9]"
			+ "|[2-9][02-8]1|[2-9][02-8][02-9]))\\s*(?:[.-]\\s*)?)?([2-9]1[02-9]"
			+ "|[2-9][02-9]1|[2-9][02-9]{2})\\s*(?:[.-]\\s*)?([0-9]{4})(?:\\s*"
			+ "(?:#|x\\.?|ext\\.?|extension)\\s*(\\d+))?$";

	public IContactInfo getContactInfo( String document ) {
		
		String name = "";
		String email = "";
		String phone = "";
		
		for ( String line : document.split( "\\n" ) ) {
			if ( line.matches( nameRegex ) ) {
				String [] nameParts = line.split( "\\s+" );
				if ( firstNames.contains( nameParts[0].toLowerCase() ) ) {
					name = line;
				}
			}
			
			if ( line.matches( emailRegex ) ) {
				email = line;
			}
			
			if ( line.matches( telephoneRegex ) ) {
				phone = line;
			}
		}
		
		return new ContactInfo( name, phone, email );
	}
}
