package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.persistent.PassengerReader;
import edu.ncsu.csc.itrust2.models.persistent.PreviousContactResult;

/**
 * Class that provides REST API endpoints for the HCP model.
 *
 * @author Jiaqi Ye (jye7)
 *
 */
@RestController
@SuppressWarnings ( { "rawtypes", "unchecked" } )
public class APIHCPController extends APIController {

    /**
     * Creates multiple new Passenger objects using PassengerReader and adds
     * them to the data base.
     *
     * @param info
     *            Strings read from CSV file
     * @return a response that is OK if all the entries were added, BAD REQUEST
     *         if the CSV file has incorrect format.
     */
    @PostMapping ( BASE_PATH + "/hcp/upload" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_PATIENT')" )
    public ResponseEntity uploadPassengers ( @RequestBody final String info ) {
        if ( info.length() == 0 ) {
            return new ResponseEntity( errorResponse( "File is empty." ), HttpStatus.BAD_REQUEST );
        }
        String msg = "";
        try {
            msg = PassengerReader.readPatientsFromFile( info );
        }
        catch ( final IllegalArgumentException e ) {
            return new ResponseEntity( errorResponse( "Invalid CSV file format." ), HttpStatus.BAD_REQUEST );
        }

        // Success response
        return new ResponseEntity( successResponse( msg ), HttpStatus.OK );

    }

    /**
     * Finds previous contact information of specific passenger.
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
     * @return a response OK if previous contact information was found,
     *         BAD_REQUEST if error detected while finding the contacts
     */
    @PostMapping ( BASE_PATH + "/hcp/findPreviousContacts/{id}/{date}/{depth}" )
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_PATIENT')" )
    public ResponseEntity findPreviousContacts ( @RequestBody final String info, @PathVariable ( "id" ) final String id,
            @PathVariable ( "depth" ) final Integer depth, @PathVariable ( "date" ) final String date ) {
        if ( info.length() == 0 ) {
            return new ResponseEntity( errorResponse( "File is empty." ), HttpStatus.BAD_REQUEST );
        }

        String msg = "";

        final String d = date.replaceAll( "-", "/" );

        try {
            msg = PassengerReader.readPreviousContacts( info, id, depth, d );
        }
        catch ( final IllegalArgumentException e ) {
            return new ResponseEntity( errorResponse( e.getMessage() ), HttpStatus.BAD_REQUEST );
        }
        // Success response
        return new ResponseEntity( successResponse( msg ), HttpStatus.OK );

    }

    /**
     * Retrieves and returns a list of all previous contact information
     *
     * @return previous contact information
     */
    @GetMapping ( BASE_PATH + "/hcp/findPreviousContacts/results" )
    public ResponseEntity getPreviousContactResults () {
        final List<PreviousContactResult> list = PreviousContactResult.getPreviousContacts();
        if ( list.isEmpty() ) {
            return new ResponseEntity( errorResponse( "No such passenger as first contact." ), HttpStatus.BAD_REQUEST );
        }
        return new ResponseEntity( list, HttpStatus.OK );
    }
}
