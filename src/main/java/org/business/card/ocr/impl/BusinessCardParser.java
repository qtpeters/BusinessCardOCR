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

/**
 * BusinessCardParser is a simple first pass implementation of IBusinessCardParser.
 * It uses a static text file bundled in the jar to test and identify names. The 
 * entire text file ( ~36k ) is loaded into memory and kept there until the process is terminated.  
 *  
 * @author qtpeters
 */
public class BusinessCardParser implements IBusinessCardParser {

	// LinkedHashSet has O(1) for contains() operation.
	private static final Set<String> firstNames = new LinkedHashSet<>();
	private static final String FIRST_NAME_FILE = "/firstNames.txt";
	
	private static final String NEWLINE = "\\n";
	private static final String ONE_OR_MORE_SPACES = "\\s";
	private static final String ANY_NON_DIGIT = "\\D";
	private static final String NOTHING = "";
	private static final String FAX_ANYWHERE_ANY_CASE = ".*[Ff][Aa][Xx].*";	
	
	// nameRegex does not take into account the possibility that 
	// a middle name or middle initial could be present.
	// nameRegex would not work on Latin names because there are four.
	private static final String NAME_REGEX = "\\b\\w+\\s+\\w+\\b";
	private static final String EMAIL_REGEX = "\\b[A-Za-z0-9._%+-]+@.*[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b"; 
	private static final String TELEPHONE_REGEX = ".*?((\\+1)?\\s*\\(?\\d{3}\\)?(\\s*|-)\\d{3}-\\d{4})";
	
	// Static block loads firstName file into memory whenthe class is loaded.
	static {
		
		// Try-with-resources block opens and automatically
		// closes streams. This block should not fail because the text
		// file is bundled with the jar and place on the classpath on build.
		try ( BufferedReader br = new BufferedReader( 
			new InputStreamReader( 
				BusinessCardParser.class.getResourceAsStream( 
					FIRST_NAME_FILE ) ) ) ) {
			String firstName = "";
			while ( ( firstName = br.readLine() ) != null ) {
				firstNames.add( firstName );
			}
			
		} catch ( IOException ioe ) {
			
			// REALLY shouldn't happen
			ioe.printStackTrace();
		}
	}
	
	// Telephone needs a pattern because the data needs to
	// be captured as well as simply matched.
	private Pattern telephonePattern;
	
	public BusinessCardParser() {
		telephonePattern = Pattern.compile( TELEPHONE_REGEX );
	}
	
	public IContactInfo getContactInfo( String document ) {
		
		String name = "";
		String email = "";
		String phone = "";
		
		// Split the entire document my the newlines 
		for ( String line : document.split( NEWLINE ) ) {
			
			// If the name regex matches, then we need to parse and match 
			// a first name in the list. The list isn't complete, but each 
			// new name that is found can be added. 
			if ( line.matches( NAME_REGEX ) ) {
				String [] nameParts = line.split( ONE_OR_MORE_SPACES );
				if ( firstNames.contains( nameParts[0].toLowerCase() ) ) {
					name = line;
				}
			}
			
			// If the line matches the email regex, then add the email.
			if ( line.matches( EMAIL_REGEX ) ) {
				email = line;
			}
			
			// If the line has "FAX" anywhere in it disregard, otherwise
			// match for phone numbers.  Only the phone number patterns 
			// listed in the example are matched, the regex would have 
			// to be augmented if the phone numbers came in more diverse
			// forms.
			Matcher m = telephonePattern.matcher( line );
			if ( ! line.matches( FAX_ANYWHERE_ANY_CASE ) && m.find() ) {
				
				// Replace any non digit char with nothing. This puts the number
				// in the required form.
				phone = m.group( 1 ).replaceAll( ANY_NON_DIGIT, NOTHING );
			}
		}
		
		return new ContactInfo( name, phone, email );
	}
}
