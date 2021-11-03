package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Step definitions for AddHosptial feature
 *
 * @author Orion Qin (xqin3)
 */
public class HCPUploadPassengersStepDefs extends CucumberTest {

    private final String baseUrl = "http://localhost:8080/iTrust2";

    /**
     * Go to Upload passenger page
     */
    @When ( "I click Upload Passengers from the drop down Patient menu" )
    public void uploadPassengerPage () {
        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('uploadPatients').click();" );
    }

    /**
     * Upload a list to the database
     *
     * @param path
     *            the file path to the list
     */
    @When ( "I Upload (.+) to the system" )
    public void uploadList ( final String path ) {
        waitForAngular();
        final WebElement uploadElement = driver.findElement( By.id( "file" ) );

        final File f = new File( path );
        // enter the file path onto the file-selection input field
        uploadElement.sendKeys( f.getAbsolutePath() );

        // click the "UploadFile" button
        driver.findElement( By.name( "submitCSVFile" ) ).click();
    }

    /**
     * Show successful results
     *
     * @param uploaded
     *            the number of successful uploaded passengers
     * @param skipped
     *            the number of skipped passengers
     */
    @Then ( "I check the result saying (\\d+) uploaded and (\\d+) skipped" )
    public void checkSuccessfulResult ( final int uploaded, final int skipped ) {
        waitForAngular();
        try {
            assertEquals( "Submission is successful! " + uploaded + " passenger information successfully uploaded, "
                    + skipped + " skipped.", driver.findElement( By.name( "success" ) ).getText() );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

}
