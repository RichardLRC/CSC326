package edu.ncsu.csc.itrust2.models.enums;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Enum for different types of symptom classifications.
 *
 * @author Jiaqi Ye (jye7)
 *
 */
public enum Symptom {
    /**
     * Mild
     */
    M ( "M" ),
    /**
     * Severe
     */
    S ( "S" ),
    /**
     * Critical
     */
    C ( "C" ),
    /**
     * Not infected
     */
    N ( "N" );

    /**
     * Code of the LabStatus
     */
    @Enumerated ( EnumType.STRING )
    private String symptom;

    /**
     * Create a LabStatus from the numerical code.
     *
     * @param code
     *            Code of the LabStatus
     */
    private Symptom ( final String symp ) {
        this.symptom = symp;
    }
}
