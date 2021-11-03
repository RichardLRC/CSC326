/**
 *
 */
package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Step Definitions for calculate R0 class
 *
 * @author junhuasu
 *
 */
public class CalcR0StepDefs extends CucumberTest {

    private final String baseUrl = "http://localhost:8080/iTrust2";

    private void setTextField ( final By byval, final String value ) {
        final WebElement elem = driver.findElement( byval );
        elem.clear();
        elem.sendKeys( value );
    }

    // random HCP has logged in
    @Given ( "QWER has logged in" )
    public void gotoPlotPage () throws Exception {
        attemptLogout();

        driver.get( baseUrl );
        setTextField( By.name( "username" ), "svang" );
        setTextField( By.name( "password" ), "123456" );
        driver.findElement( By.className( "btn" ) ).click();

    }

    /**
     * Go to calculate R0 page
     */
    @When ( "She clicks Calculated R0 from the drop down Patient menu" )
    public void uploadPassengerPage () {
        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('calculatedR0').click();" );
    }

    /**
     * Upload a csv file to the database
     *
     * @param path
     *            given path of the file
     */
    @When ( "She uploads (.+) to the database" )
    public void uploadList ( final String path ) {
        waitForAngular();
        final WebElement upload = driver.findElement( By.name( "fileChoice" ) );
        final File f = new File( path );

        // send the path to file button
        upload.sendKeys( f.getAbsolutePath() );

        // click the "UploadFile" button
        driver.findElement( By.name( "submitCSVFile" ) ).click();
    }

    @Then ( "She checks the result saying success" )
    public void checkSuccessfulResult () {
        waitForAngular();
        try {
            // check success message
            driver.findElement( By.name( "success" ) ).getText().contains( "Submission is successful!" );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    @Then ( "The result shows failed" )
    public void checkFailedResult () {
        waitForAngular();
        try {
            // checks failed message
            driver.findElement( By.name( "failure" ) ).getText().contains( "" );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    @Then ( "She checks the value of R0 and behaviours of population" )
    public void checkR0 () {
        waitForAngular();

        // check the R0 value and its corresponding behavior
        try {
            // System.out.println( driver.findElement( By.id( "r0" ) ).getText()
            // );

            assertTrue( driver.findElement( By.id( "r0" ) ).getText().contains( "The R0 value is" ) );
            assertTrue( driver.findElement( By.id( "pop" ) ).getText().contains( "The behaviors in population is" ) );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

}
