
# Building the software
Install [https://maven.apache.org](https://maven.apache.org)

We’ve created a new smartphone app that enables users to snap a photo of a business card 
and have the information from the card automatically extracted and added to their contact list. 
We need you to write the component that parses the results of the optical character recognition (OCR) 
component in order to extract the name, phone number, and email address from the processed business card image. 
We have provided you with a basic interface specification [1] and a series of example inputs [2] and 
would like you to provide the implementation.

[1] INTERFACE SPECIFICATION

interface IContactInfo
    String getName() : returns the full name of the individual (eg. John Smith, Susan Malick)
        String getPhoneNumber() : returns the phone number formatted as a sequence of digits with no punctuation
            String getEmailAddress() : returns the email address of the individual

            interface IBusinessCardParser
                IContactInfo getContactInfo(String document)
                [2] EXAMPLE INPUT DOCUMENTS AND THEIR EXPECTED RESULTS

<pre>
                Example 1:

                ASYMMETRIK LTD
                Mike Smith
                Senior Software Engineer
                (410)555-1234
                msmith@asymmetrik.com

                ==>

                Name: Mike Smith
                Phone: 4105551234
                Email: msmith@asymmetrik.com

                Example 2:

                Foobar Technologies
                Analytic Developer
                Lisa Haung
                1234 Sentry Road
                Columbia, MD 12345
                Phone: 410-555-1234
                Fax: 410-555-4321
                lisa.haung@foobartech.com

                ==>

                Name: Lisa Haung
                Phone: 4105551234
                Email: lisa.haung@foobartech.com

                Example 3:

                Arthur Wilson
                Software Engineer
                Decision & Security Technologies
                ABC Technologies
                123 North 11th Street
                Suite 229
                Arlington, VA 22209
                Tel: +1 (703) 555-1259
                Fax: +1 (703) 555-1200
                awilson@abctech.com

                ==>

                Name: Arthur Wilson
                Phone: 17035551259
                Email: awilson@abctech.com
</pre>
