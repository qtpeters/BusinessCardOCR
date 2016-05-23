package org.business.card.ocr.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.business.card.ocr.IBusinessCardParser;
import org.business.card.ocr.IContactInfo;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BusinessCardParserTest {

	private static final String TEST_FILE = "/testData.txt";
	private static final String DELIMITER = ""; 

	private IBusinessCardParser parser;
	
	private List<String> preProc = new ArrayList<>();
	private List<String> postProc = new ArrayList<>();
	
	/**
	 * setUp() reads in the example cases from a text file, parses them and 
	 * stores the test data in lists for the unit test. 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		parser = new BusinessCardParser();
		
		boolean isPreProc = true;
		
		InputStream is = BusinessCardParserTest.class.getResourceAsStream( TEST_FILE );
		try ( BufferedReader reader = new BufferedReader( new InputStreamReader( is ) ) ) {
			
			StringBuffer sb = new StringBuffer();
			String line = "";
			while ( ( line = reader.readLine() ) != null ) {
				
				if ( line.equals( DELIMITER ) ) {
					
					if ( isPreProc ) {
						preProc.add( sb.toString() );
					} else {
						postProc.add( sb.toString() );
					}
					
					isPreProc = isPreProc ? false : true;
					sb = new StringBuffer();
					
				} else {					
					sb.append( line ).append( '\n' );
				}
			}
		} 
	}
	
	/**
	 * getContactInfoTest uses the data stored in setUp() to 
	 * perform an automated test to verify the production code
	 * produces the correct output.
	 */
	@Test
	public void getContactInfoTest() {
		
		for ( int i=0; i<preProc.size(); i++ ) {
			String preProcDocument = preProc.get( i );
			String postProcDocumentFromFile = postProc.get( i );
			
			IContactInfo contactInfo = parser.getContactInfo( preProcDocument );
			String postProcDocument = contactInfo.toString();
			assertEquals( postProcDocument, postProcDocumentFromFile );
		}
	}
}
