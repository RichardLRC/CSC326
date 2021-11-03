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
 * APIPassengerStatisticsController for UC28. It provides two functions,
 * getnumOfSeverityCode and getnumOfInfectperDay.
 *
 * @author richardli
 *
 */
@RestController
public class APIPassengerStatisticsController extends APIController {

    /**
     * Get the number of severity code
     * 
     * @return the map object of the number of the code
     */
    @GetMapping ( BASE_PATH + "getnumOfSeverityCode" )
    public Map<String, Integer> getnumOfSeverityCode () {
        final List<Integer> l = new ArrayList<Integer>();
        final List<String> st = new ArrayList<String>();
        List<Passenger> p = new ArrayList<Passenger>();
        p = Passenger.getPassenger();
        int m = 0;
        int s = 0;
        int c = 0;
        int n = 0;
        for ( int i = 0; i < p.size(); i++ ) {
            if ( p.get( i ).getSymptom().equals( Symptom.M ) ) {
                m++;
            }
            if ( p.get( i ).getSymptom().equals( Symptom.S ) ) {
                s++;
            }
            if ( p.get( i ).getSymptom().equals( Symptom.C ) ) {
                c++;
            }
            if ( p.get( i ).getSymptom().equals( Symptom.N ) ) {
                n++;
            }
        }
        l.add( 0, m );
        l.add( 1, s );
        l.add( 2, c );
        l.add( 3, n );
        st.add( 0, "m" );
        st.add( 1, "s" );
        st.add( 2, "c" );
        st.add( 3, "n" );

        final Map<String, Integer> numOfSeverityCode = new LinkedHashMap<String, Integer>();
        for ( int i = 0; i < l.size(); i++ ) {
            numOfSeverityCode.put( st.get( i ), l.get( i ) );
        }

        LoggerUtil.log( TransactionType.VIEW_SEVERITY_CODE, LoggerUtil.currentUser(),
                "A list of serverity code number is loaded." );

        return numOfSeverityCode;

    }

    /**
     * Get a list of data of severity code
     * 
     * @return the list of data of the severity code
     */
    @GetMapping ( BASE_PATH + "getnumOfSeverityCodedata" )
    public List<Integer> getnumOfSeverityCodedata () {
        final List<Integer> l = new ArrayList<Integer>();
        List<Passenger> p = new ArrayList<Passenger>();
        p = Passenger.getPassenger();
        int m = 0;
        int s = 0;
        int c = 0;
        int n = 0;
        for ( int i = 0; i < p.size(); i++ ) {
            if ( p.get( i ).getSymptom().equals( Symptom.M ) ) {
                m++;
            }
            if ( p.get( i ).getSymptom().equals( Symptom.S ) ) {
                s++;
            }
            if ( p.get( i ).getSymptom().equals( Symptom.C ) ) {
                c++;
            }
            if ( p.get( i ).getSymptom().equals( Symptom.N ) ) {
                n++;
            }
        }
        l.add( 0, m );
        l.add( 1, s );
        l.add( 2, c );
        l.add( 3, n );
        return l;

    }

    /**
     * Get the number of infected during that day
     * 
     * @return the number of infected at the day
     */
    @GetMapping ( BASE_PATH + "getnumOfInfectperDay" )
    public Map<Date, Integer> getnumOfInfectperDay () {

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

        LoggerUtil.log( TransactionType.VIEW_INFECTED_PER_DAY, LoggerUtil.currentUser(),
                "A list of number of infected patients per day is loaded." );

        return numOfInfectperDay;

    }

    /**
     * Get the number of infected per day on the date
     * 
     * @return the list of date
     */
    @GetMapping ( BASE_PATH + "getnumOfInfectperDayDate" )
    public List<Date> getnumOfInfectperDayDate () {

        List<Passenger> p = new ArrayList<Passenger>();
        p = Passenger.getPassenger();
        if ( p.size() == 0 ) {
            final List<Date> kk = new ArrayList<Date>();
            kk.add( new Date() );
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
        return rangeDate;

    }

    /**
     * Get the data of number of infected per day on that date
     * 
     * @return a list of data
     */
    @GetMapping ( BASE_PATH + "getnumOfInfectperDayData" )
    public List<Integer> getnumOfInfectperDayData () {

        List<Passenger> p = new ArrayList<Passenger>();
        p = Passenger.getPassenger();
        if ( p.size() == 0 ) {
            final List<Integer> kk = new ArrayList<Integer>();
            kk.add( 0 );
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
        return numOfInfect;

    }

}
