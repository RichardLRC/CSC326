package edu.ncsu.csc.itrust2.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.models.persistent.PassengerReader;
import edu.ncsu.csc.itrust2.models.persistent.PreviousContactResult;

/**
 * Unit test for the PatientReader class
 *
 * @author Jiaqi Ye (jye7)
 *
 */
public class PassengerReaderTest {

    /**
     * Tests to see if a valid file of passenger data will be store into
     * database correctly
     */
    @Test
    public void testValidPassengerDataUpload () {
        Passenger.deleteAll( Passenger.class );
        assertEquals( Passenger.getPassenger().size(), 0 );
        final String line1 = "3b9aca00, \"Reuchlin, Jonkheer, J. G.\", M, 2020/02/16 21:26:34\n";
        final String line2 = "3b9aca76, \"Drachstedt, Baron von\", M, 2020/02/04 13:18:03\n";
        final String line3 = "3b9acaec, \"Foreman, B. L.\", S, 2020/02/04 20:13:22";

        final String file = line1 + line2 + line3;
        String msg = "";
        try {
            msg = PassengerReader.readPatientsFromFile( file );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Invalid CSV file format", e.getMessage() );
        }

        assertEquals( "3 passenger information successfully uploaded, 0 skipped.", msg );

        assertEquals( Passenger.getPassenger().size(), 3 );

        Passenger.deleteAll( Passenger.class );
        assertEquals( Passenger.getPassenger().size(), 0 );
    }

    /**
     * Test to see if a invalid file of passenger data will output error
     * response
     */
    @Test
    public void testInvalidPassengerDataUpload () {
        Passenger.deleteAll( Passenger.class );
        assertEquals( Passenger.getPassenger().size(), 0 );

        final String line1 = "3b9aca00, \"Reuchlin, Jonkheer, J. G.\", M, 2020/02/16 21:26:34\n";
        final String line2 = "3b9aca76, \"Drachstedt, Baron von\", M, 2020/02/04 13:18:03\n";
        final String line3 = "3b9acaec, \"Foreman, B. L.\", S";
        final String line4 = "3b9acaec, \"Foreman, B. L.\", S, 2020\n";

        // Test invalid CSV format
        final String file = line1 + line2 + line3;
        String msg = "";
        try {
            msg = PassengerReader.readPatientsFromFile( file );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Invalid CSV file format.", e.getMessage() );
        }

        assertEquals( "", msg );
        assertEquals( Passenger.getPassenger().size(), 0 );

        Passenger.deleteAll( Passenger.class );
        assertEquals( Passenger.getPassenger().size(), 0 );

        // Test invalid date format
        final String file2 = line1 + line2 + line4;

        String msg2 = "";
        try {
            msg2 = PassengerReader.readPatientsFromFile( file2 );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Invalid date format.", e.getMessage() );
        }

        assertEquals( "", msg2 );
        assertEquals( Passenger.getPassenger().size(), 0 );

        Passenger.deleteAll( Passenger.class );
        assertEquals( Passenger.getPassenger().size(), 0 );

    }

    /**
     * Tests to see if a valid file of passenger data will be store into
     * database correctly
     */
    @Test
    public void testValidPreviousContactSearch () {
        Passenger.deleteAll( Passenger.class );
        assertEquals( Passenger.getPassenger().size(), 0 );
        final String line1 = "3b9aca00, \"Reuchlin, Jonkheer, J. G.\", M, 2020/02/16 21:26:34\n";
        final String line2 = "3b9aca76, \"Drachstedt, Baron von\", M, 2020/02/04 13:18:03\n";
        final String line3 = "3b9acaec, \"Foreman, B. L.\", S, 2020/02/04 20:13:22";

        final String file = line1 + line2 + line3;
        String msg = "";
        try {
            msg = PassengerReader.readPatientsFromFile( file );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "Invalid CSV file format", e.getMessage() );
        }

        assertEquals( "3 passenger information successfully uploaded, 0 skipped.", msg );
        assertEquals( Passenger.getPassenger().size(), 3 );

        PreviousContactResult.deleteAll( PreviousContactResult.class );
        assertEquals( PreviousContactResult.getPreviousContacts().size(), 0 );

        final String contact1 = "3b9aca00,3b9aca76\n";
        final String contact2 = "3b9aca76,3b9acaec\n";
        final String contact3 = "3b9acaec\n";
        final String contacts = contact1 + contact2 + contact3;

        // valid case with contacts
        String cmsg = "";
        try {
            cmsg = PassengerReader.readPreviousContacts( contacts, "3b9aca76", 1, "2020/02/04" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "No such passenger as first contact.", e.getMessage() );
        }

        assertEquals( "Previous contact list found!", cmsg );

        assertEquals( PreviousContactResult.getPreviousContacts().size(), 1 );

        PreviousContactResult.deleteAll( PreviousContactResult.class );
        assertEquals( PreviousContactResult.getPreviousContacts().size(), 0 );

        // valid case with no contacts
        try {
            cmsg = PassengerReader.readPreviousContacts( contacts, "3b9acaec", 1, "2020/02/04" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "No such passenger as first contact.", e.getMessage() );
        }

        assertEquals( "This passenger was not in contact with anyone else.", cmsg );

        assertEquals( PreviousContactResult.getPreviousContacts().size(), 1 );

        PreviousContactResult.deleteAll( PreviousContactResult.class );
        assertEquals( PreviousContactResult.getPreviousContacts().size(), 0 );

        // invalid case no such passenger
        try {
            cmsg = PassengerReader.readPreviousContacts( contacts, "3b9acae1", 1, "2020/02/04" );
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( "No such passenger as first contact.", e.getMessage() );
        }

        assertEquals( PreviousContactResult.getPreviousContacts().size(), 0 );

        PreviousContactResult.deleteAll( PreviousContactResult.class );
        assertEquals( PreviousContactResult.getPreviousContacts().size(), 0 );

        Passenger.deleteAll( Passenger.class );
        assertEquals( Passenger.getPassenger().size(), 0 );

    }

}
