/**
 *
 */
package edu.ncsu.csc.itrust2.controllers.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.enums.Symptom;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Provides REST endpoints that calculates R0 value. HTTP GET is provided to get
 * R0 value
 *
 * @author junhuasu
 *
 */
@RestController
public class APIR0Controller extends APIController {

    /**
     * Helper methods that return infected people per day. It's same method used
     * in APIPassengerStatisticsController.
     *
     * @return returns a map contains data and corresponding infected people
     */
    private Map<Date, Integer> getInfectperDay () {
        List<Passenger> p = new ArrayList<Passenger>();
        p = Passenger.getPassenger();
        if ( p.size() == 0 ) {
            final Map<Date, Integer> kk = new LinkedHashMap<Date, Integer>();
            kk.put( new Date(), 0 );
            return kk;
        }
        for ( int i = p.size() - 1; i >= 0; i-- ) {
            if ( p.get( i ).getSymptom().equals( Symptom.N ) ) {
                p.remove( i );
            }
        }

        final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss.s" );
        // final SimpleDateFormat sdf = new SimpleDateFormat( "yyyy/MM/dd" );
        final List<Date> d = new ArrayList<Date>();
        for ( int i = 0; i < p.size(); i++ ) {
            final String date = p.get( i ).getDateSymptomBegan().toString();
            try {
                d.add( i, sdf.parse( date ) );
            }
            catch ( final ParseException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Collections.sort( d );
        int count = 0;
        final Date firstDate = Collections.min( d );
        final Date lastDate = Collections.max( d );
        final Calendar start = Calendar.getInstance();
        start.setTime( firstDate );
        final Calendar end = Calendar.getInstance();
        end.setTime( lastDate );

        final List<Integer> numOfInfect = new ArrayList<Integer>();

        for ( final Date date = start.getTime(); start.before( end ); start.add( Calendar.DATE, 1 ) ) {
            for ( int i = 0; i < d.size(); i++ ) {
                final Calendar cal1 = Calendar.getInstance();
                final Calendar cal2 = Calendar.getInstance();
                cal1.setTime( d.get( i ) );
                cal2.setTime( date );
                if ( cal1.get( Calendar.YEAR ) == start.get( Calendar.YEAR ) ) {
                    if ( cal1.get( Calendar.MONTH ) == start.get( Calendar.MONTH ) ) {
                        if ( cal1.get( Calendar.DAY_OF_MONTH ) == start.get( Calendar.DAY_OF_MONTH ) ) {
                            count++;
                        }

                    }
                }

            }
            numOfInfect.add( count );
            count = 0;

        }

        final List<Date> rangeDate = new ArrayList<Date>();
        final Calendar start1 = Calendar.getInstance();
        start1.setTime( firstDate );
        final Calendar end1 = Calendar.getInstance();
        end1.setTime( lastDate );

        while ( start1.before( end1 ) ) {
            final Date result = start1.getTime();
            rangeDate.add( result );
            start1.add( Calendar.DATE, 1 );
        }

        Collections.sort( rangeDate );

        final Map<Date, Integer> numOfInfectperDay = new LinkedHashMap<Date, Integer>();
        for ( int i = 0; i < rangeDate.size(); i++ ) {
            numOfInfectperDay.put( rangeDate.get( i ), numOfInfect.get( i ) );
        }

        return numOfInfectperDay;
    }

    /**
     * Calculates R0 based on new infected people divided by base infected
     * people from first day to peak day.
     *
     * @return returns R0 value based on given data sets
     */
    @GetMapping ( BASE_PATH + "R0" )
    public double calculateR0 () {
        final Map<Date, Integer> numOfInfect = getInfectperDay();
        // System.out.println( numOfInfect.values() );
        if ( numOfInfect.keySet().size() <= 1 ) {
            return 0;
        }
        final Iterator<Integer> itr = numOfInfect.values().iterator();
        final int count = calculatePeakDay() - 1;
        double firstInfect = 0.0;
        double newInfect = 0.0;
        double r0 = 0.0;
        double sum = 0.0;
        firstInfect = itr.next();
        newInfect = itr.next();
        for ( int i = 0; i < count; i++ ) {
            sum += firstInfect;
            // System.out.println( " " + i + " " + firstInfect + " " + newInfect
            // + " " + r0 + " " + sum ) ;
            r0 += newInfect / sum;
            firstInfect = newInfect;
            newInfect = itr.next();
        }
        r0 = r0 / count;
        // System.out.println( newInfect );
        // System.out.println( firstInfect );
        // System.out.println( r0 );
        LoggerUtil.log( TransactionType.CALCULATE_R0, LoggerUtil.currentUser(),
                "HCP gets the reproductive number, R0" );
        return r0;
    }

    /**
     * Helper method that calculate the day that most people get infected during
     * one day
     *
     * @return return date of the peak day
     */
    private int calculatePeakDay () {
        final Map<Date, Integer> numOfInfect = getInfectperDay();
        if ( numOfInfect.keySet().size() <= 1 ) {
            return 0;
        }
        final Iterator<Map.Entry<Date, Integer>> itr = numOfInfect.entrySet().iterator();

        int gap = 0;
        int count = 0;
        while ( itr.hasNext() ) {
            final Map.Entry<Date, Integer> entry = itr.next();
            if ( entry.getValue() > gap ) {
                gap = entry.getValue();
                count++;
            }
        }

        return count;

    }
}
