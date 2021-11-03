package edu.ncsu.csc.itrust2.models.persistent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;

import com.google.common.collect.ObjectArrays;

import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.Symptom;

/**
 * Processes Strings of Passenger data and save into data base
 *
 * @author Jiaqi Ye (jye7)
 *
 */
public class PassengerReader {

    /**
     * Process through String of Passenger information and save into data base.
     *
     * @param info
     *            Strings of Passenger information read from CSV file
     * @return the number of successful upload and skipped passenger
     */
    public static String readPatientsFromFile ( final String info ) {
        int saved = 0;
        int duplicate = 0;
        final String[] lines = info.split( "\n" );
        for ( int i = 0; i < lines.length; i++ ) {
            // PASSENGER ID, NAME, SYMPTOM SEVERITY CODE, DATE SYMPTOMS
            // BEGAN

            final Passenger passenger = new Passenger();
            final String delimiters = ", (?=([^\"]*\"[^\"]*\")*[^\"]*$)";
            final String[] inputs = lines[i].split( delimiters );
            if ( inputs.length != 4 ) {
                Passenger.deleteAll( Passenger.class );
                throw new IllegalArgumentException( "Invalid CSV file format." );
            }
            final String id = inputs[0];
            final String fullname = inputs[1].replaceAll( "\"", "" ).replaceAll( ", ", " " );

            final String[] nameTokens = fullname.split( "\\s+", 2 );

            final Symptom symptom = Symptom.valueOf( inputs[2] );

            // eg. '2020/02/04 13:18:03'
            Date date = null;
            final SimpleDateFormat dFormat = new SimpleDateFormat( "yyyy/MM/dd hh:mm:ss" );

            try {
                date = dFormat.parse( inputs[3] );
            }
            catch ( final ParseException e ) {
                Passenger.deleteAll( Passenger.class );
                throw new IllegalArgumentException( "Invalid date format." );
            }

            final String firstname = nameTokens[0].replaceAll( ",", "" ).replaceAll( "\\.", "" );
            String lastname = "NoLast" + i;
            if ( nameTokens.length > 1 ) {
                lastname = nameTokens[1].replaceAll( "\\.", "" );
            }

            if ( Passenger.getById( id ) == null ) {
                final String username = firstname.replaceAll( "[^A-Za-z]+", "" ).toLowerCase();
                final Patient patient = new Patient();
                patient.setFirstName( firstname );
                final User patientUser = new User( username,
                        "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.", Role.ROLE_PATIENT, 1 );
                patientUser.save();
                patient.setSelf( patientUser );
                patient.setLastName( lastname );
                patient.setDateOfBirth( LocalDate.now().minusYears( 40 ) );
                patient.save();
                passenger.setName( fullname );
                passenger.setSymptom( symptom );
                passenger.setDateSymptomBegan( date );
                passenger.setPassengerId( id );
                passenger.setPatient( patient );
                passenger.save();
                saved++;
            }
            else {
                duplicate++;
            }

        }

        final String str = saved + " passenger information successfully uploaded, " + duplicate + " skipped.";
        return str;

    }

    /**
     * Process through String of contact information and track the previous
     * contact history of a specific passenger given its id
     *
     * @param info
     *            Strings of contact information read from CSV file
     * 
     * @param id
     *            the id of the passenger to track
     * @param depth
     *            the level of depth to look into
     * @param date
     *            the symptom began date of the passenger
     * @return the number of successful upload and skipped passenger
     */
    public static String readPreviousContacts ( final String info, final String id, final int depth,
            final String date ) {
        PreviousContactResult.deleteAll( PreviousContactResult.class );
        Date d = null;
        final SimpleDateFormat dFormat = new SimpleDateFormat( "yyyy/MM/dd" );

        try {
            d = dFormat.parse( date );
        }
        catch ( final ParseException e ) {
            throw new IllegalArgumentException( "Invalid date format." );
        }

        final String[] lines = info.split( "\n" );
        final String results[][] = new String[depth][];
        int depthCount = 0;
        final String[] firstPassengers = new String[lines.length];
        for ( int i = 0; i < lines.length; i++ ) {
            final String[] inputs = lines[i].split( "," );
            firstPassengers[i] = inputs[0];
        }
        int firstIdx = -1;
        if ( Arrays.asList( firstPassengers ).contains( id ) ) {
            firstIdx = Arrays.asList( firstPassengers ).indexOf( id );

        }
        else {
            throw new IllegalArgumentException( "No such passenger as first contact." );
        }

        final Passenger p = Passenger.getById( firstPassengers[firstIdx] );
        final String[] firstContacts = lines[firstIdx].split( "," );
        if ( firstContacts.length < 2 ) {
            PreviousContactResult.deleteAll( PreviousContactResult.class );
            final PreviousContactResult result = new PreviousContactResult( p.name, null, p.dateSymptomBegan );
            result.save();
            return "This passenger was not in contact with anyone else.";
        }
        final String[] firstLevelContacts = new String[firstContacts.length];
        int currentCount = 0;
        for ( int i = 1; i < firstContacts.length; i++ ) {
            if ( !Passenger.getById( firstContacts[i] ).getDateSymptomBegan().before( d ) ) {
                firstLevelContacts[currentCount] = firstContacts[i];
                currentCount++;
            }
        }

        // done with n = 1 case
        results[depthCount] = firstLevelContacts;
        depthCount++;

        while ( depthCount != depth ) {

            for ( final String contact : results[depthCount - 1] ) {
                boolean firstIteration = true;
                if ( contact != null && Arrays.asList( firstPassengers ).contains( contact ) ) {
                    final int index = Arrays.asList( firstPassengers ).indexOf( contact );
                    final String[] currentIteration = lines[index].split( "," );
                    final String[] currentLevelContacts = new String[currentIteration.length];
                    for ( int i = 1; i < currentIteration.length; i++ ) {
                        int count = 0;
                        if ( !Passenger.getById( currentIteration[i] ).getDateSymptomBegan().before( d ) ) {
                            currentLevelContacts[count] = currentIteration[i];
                            count++;
                        }
                    }

                    if ( !firstIteration ) {
                        results[depthCount] = ObjectArrays.concat( results[depthCount], currentLevelContacts,
                                String.class );

                    }
                    else {
                        results[depthCount] = currentLevelContacts;
                        firstIteration = false;
                    }

                }

            }
            depthCount++;
        }

        PreviousContactResult.deleteAll( PreviousContactResult.class );
        final PreviousContactResult result = new PreviousContactResult( p.name, results, p.dateSymptomBegan );
        result.save();
        return "Previous contact list found!";
    }

}
