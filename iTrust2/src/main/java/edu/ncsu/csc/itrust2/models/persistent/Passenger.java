package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.criterion.Criterion;
import org.hibernate.validator.constraints.NotEmpty;

import edu.ncsu.csc.itrust2.models.enums.Symptom;

/**
 * Class for Passengers.
 *
 * @author Jiaqi Ye
 *
 */
@Entity
@Table ( name = "Passengers" )
public class Passenger extends DomainObject<Passenger> {
    @Id
    @NotEmpty
    String  passengerId;

    @Enumerated ( EnumType.STRING )
    Symptom symptom;

    String  name;

    Date    dateSymptomBegan;

    @OneToOne
    @JoinColumn ( name = "patient", columnDefinition = "varchar(100)" )
    Patient patient;

    /** For Hibernate */
    public Passenger () {
    }

    /**
     * Constructor for passenger object
     *
     * @param patient
     *            the patient associated with this passenger
     * @param id
     *            the passenger id
     * @param name
     *            the passenger name
     * @param symptom
     *            the symptom severity code
     * @param dateSymptomBegan
     *            the data symptom began
     */
    public Passenger ( final Patient patient, final String id, final String name, final Symptom symptom,
            final Date dateSymptomBegan ) {
        setPatient( patient );
        setPassengerId( id );
        setName( name );
        setSymptom( symptom );
        setDateSymptomBegan( dateSymptomBegan );
    }

    /**
     * Gets the patient associated with this passenger
     *
     * @return the patient
     */
    public Patient getPatient () {
        return patient;
    }

    /**
     * Sets the patient that associated with this passenger
     *
     * @param patient
     *            the patient
     */
    public void setPatient ( final Patient patient ) {
        this.patient = patient;

    }

    @Override
    public Serializable getId () {
        return passengerId;
    }

    /**
     * Get the passenger id
     *
     * @return the passenger id
     */
    public String getPassengerId () {
        return passengerId;
    }

    /**
     * Sets the passenger id
     *
     * @param passengerId
     *            the id to be set
     */
    public void setPassengerId ( final String passengerId ) {
        this.passengerId = passengerId;
    }

    /**
     * Get the symptom severity code
     *
     * @return Get the symptom severity code
     */
    public Symptom getSymptom () {
        return symptom;
    }

    /**
     * Set the symptom severity code
     *
     * @param symptom
     *            the symptom severity code to be set
     */
    public void setSymptom ( final Symptom symptom ) {
        this.symptom = symptom;
    }

    /**
     * Get the passenger name
     *
     * @return the passenger name
     */
    public String getName () {
        return name;
    }

    /**
     * Set the name of the passenger
     *
     * @param name
     *            the name to be set
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Get the date symptom began
     *
     * @return the date symptom began
     */
    public Date getDateSymptomBegan () {
        return dateSymptomBegan;
    }

    /**
     * Set the date symptom began
     *
     * @param dateSymptomBegan
     *            the date to be set
     */
    public void setDateSymptomBegan ( final Date dateSymptomBegan ) {
        this.dateSymptomBegan = dateSymptomBegan;
    }

    /**
     * Get all patients in the database
     *
     * @SuppressWarnings for Unchecked cast from List<capture#1-of ? extends
     *                   DomainObject> to List<Patient> Because get all just
     *                   returns a list of DomainObjects, the cast is okay.
     *
     * @return all patients in the database
     */
    @SuppressWarnings ( "unchecked" )
    public static List<Passenger> getPassengers () {
        final List<Passenger> passengers = (List<Passenger>) getAll( Passenger.class );
        final List<Passenger> rPass = new ArrayList<Passenger>();
        final Set<User> usernames = new HashSet<User>();
        for ( int i = 0; i < passengers.size(); i++ ) {
            if ( !usernames.contains( passengers.get( i ).getSelf() ) ) {
                usernames.add( (User) passengers.get( i ).getSelf() );
                rPass.add( passengers.get( i ) );
            }
        }
        return rPass;
    }

    private Object getSelf () {
        return this.patient.getSelf();
    }

    /**
     * Gets the list of passengers from data base
     *
     * @return a list of passengers from data base
     */
    @SuppressWarnings ( "unchecked" )
    static public List<Passenger> getPassenger () {
        return (List<Passenger>) getAll( Passenger.class );
    }

    /**
     * Get a specific patient by id
     *
     * @param id
     *            the id of the patient to get
     * @return the patient with the queried id
     */
    public static Passenger getById ( final String id ) {
        try {
            return getWhere( eqList( ID, id ) ).get( 0 );
        }
        catch ( final Exception e ) {
            return null;
        }
    }

    /**
     * Returns a List of Passenger that are selected by the given WHERE clause
     *
     * @param where
     *            List of Criterion to and together and search for records by
     * @return The list of Passenger SELECTed
     */
    @SuppressWarnings ( "unchecked" )
    private static List<Passenger> getWhere ( final List<Criterion> where ) {
        return (List<Passenger>) getWhere( Passenger.class, where );
    }
}
