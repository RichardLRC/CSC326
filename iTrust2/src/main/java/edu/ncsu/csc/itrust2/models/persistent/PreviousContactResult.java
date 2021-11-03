package edu.ncsu.csc.itrust2.models.persistent;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Class to save previous contact results.
 *
 * @author Jiaqi Ye
 *
 */
@Entity
@Table ( name = "PreviousContactsResult" )
public class PreviousContactResult extends DomainObject<Passenger> {

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long id;

    String       name;

    @Lob
    @Column ( name = "contacts", columnDefinition = "LONGBLOB" )
    String[][]   contacts;

    Date         infectedDate;

    /** For Hibernate */
    public PreviousContactResult () {
    }

    /**
     * Constructs a previous contact result entry
     *
     * @param name
     *            the name of the passenger to track
     * @param contacts
     *            the list of passenger IDs that were in contact with given
     *            passenger
     * @param infectedDate
     *            the infected date of given passenger
     */
    public PreviousContactResult ( final String name, final String[][] contacts, final Date infectedDate ) {
        setName( name );
        setContacts( contacts );
        setInfectedDate( infectedDate );
    }

    /**
     * Gets the name of the passenger
     *
     * @return the name of the passenger
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the name of the passenger
     *
     * @param name
     *            the name of the passenger
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Gets the list of contacts of the passenger
     *
     * @return the list of contacts of the passenger
     */
    public String[][] getContacts () {
        return contacts;
    }

    /**
     * Sets the list of contacts of the passenger
     *
     * @param contacts
     *            the list of contacts of the passenger
     */
    public void setContacts ( final String[][] contacts ) {
        this.contacts = contacts;
    }

    /**
     * Gets the infected date of the passenger
     *
     * @return the infected date of the passenger
     */
    public Date getInfectedDate () {
        return infectedDate;
    }

    /**
     * Sets the infected date of the passenger
     *
     * @param infectedDate
     *            the date to be set
     */
    public void setInfectedDate ( final Date infectedDate ) {
        this.infectedDate = infectedDate;
    }

    @Override
    public Serializable getId () {
        return id;
    }

    /**
     * Gets the result of previous contacts
     *
     * @return the result of previous contacts
     */
    @SuppressWarnings ( "unchecked" )
    static public List<PreviousContactResult> getPreviousContacts () {
        return (List<PreviousContactResult>) getAll( PreviousContactResult.class );
    }

}
