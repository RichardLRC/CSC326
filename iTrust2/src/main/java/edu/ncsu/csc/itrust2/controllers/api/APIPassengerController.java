package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.persistent.Passenger;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Controller responsible for providing various REST API endpoints for the
 * Passenger model.
 *
 * @author Jiaqi Ye (jye7)
 *
 */
@RestController
public class APIPassengerController extends APIController {

    /**
     * Retrieves and returns a list of all Passenger stored in the system
     *
     * @return list of personnel
     */
    @GetMapping ( BASE_PATH + "/passenger" )
    public List<Passenger> getPassenger () {
        LoggerUtil.log( TransactionType.UPLOAD_PASSENGER_LIST, LoggerUtil.currentUser(),
                "A list of passengers is loaded" );
        return Passenger.getPassenger();
    }

}
