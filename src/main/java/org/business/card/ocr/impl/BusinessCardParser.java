package org.business.card.ocr.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	private String telephoneRegex = ".*?\\+?((\\s+)?\\(?(\\d{3})\\)?-?\\d{3}-\\d{4})\\b";
	private Pattern telephonePattern;
	
	public BusinessCardParser() {
		telephonePattern = Pattern.compile( telephoneRegex );
	}
	
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
			
			Matcher m = telephonePattern.matcher( line );
			if ( m.matches() ) {
				phone = m.group( 1 );
			}
		}
		
		return new ContactInfo( name, phone, email );
	}
}
