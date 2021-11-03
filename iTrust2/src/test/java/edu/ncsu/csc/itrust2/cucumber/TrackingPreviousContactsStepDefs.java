package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Step definitions for AddHosptial feature
 *
 * @author Orion Qin (xqin3)
 */
public class TrackingPreviousContactsStepDefs extends CucumberTest {

    private final String baseUrl = "http://localhost:8080/iTrust2";

    /**
     * Go to track contact page
     */
    @When ( "I click Tracking Previous Contacts from the drop down Patient menu" )
    public void trackingContactsPage () {
        waitForAngular();
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('trackingContacts').click();" );
    }

    /**
     * Upload a tracking list to the database
     *
     * @param path
     *            the file path to the list
     */
    @When ( "I Upload (.+) tracking list" )
    public void uploadContactList ( final String path ) {
        waitForAngular();
        try {
            final File f = new File( path );
            // enter the file path onto the file-selection input field
            driver.findElement( By.name( "fileChoice" ) ).sendKeys( f.getAbsolutePath() );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

    /**
     * Submit the request after fill in the passenger info
     */
    @When ( "I submit the request" )
    public void submitRequest () {

    }

    /**
     * Fill in the desired passenger contact list info
     *
     * @param id
     *            the passenger id
     * @param date
     *            the date of infection
     * @param depth
     *            the depth of contact
     */
    @When ( "I fill in the (.+), (.+) and (\\d+)" )
    public void fillInPassengerInfo ( final String id, final String date, final int depth ) {
        waitForAngular();
        driver.findElement( By.id( "passengerid" ) ).sendKeys( id );
        driver.findElement( By.id( "date" ) ).sendKeys( date.replace( "/", "" ) );
        driver.findElement( By.id( "inputDepth" ) ).sendKeys( Integer.toString( depth ) );
        waitForAngular();
        driver.findElement( By.name( "submitRequest" ) ).click();

    }

    /**
     * Find the passenger that has contact with the current passenger
     *
     * @param passenger
     *            the id of the contact passenger
     */
    @Then ( "(.+) is found in the form" )
    public void findPassenger ( final String passenger ) {
        waitForAngular();
        assertEquals( "Previous contact list found! The result is shown below!",
                driver.findElement( By.xpath(
                        "/html/body/div/div[1]/div[1]/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[5]" ) )
                        .getText() );
        assertEquals( passenger + ".", driver.findElement( By.xpath(
                "/html/body/div/div[1]/div[1]/div/div/div/div/div/div[2]/div[3]/div/div/div[2]/table/tbody/tr/td[4]" ) )
                .getText() );
    }

    /**
     * If no contact has been found with the passenger
     */
    @Then ( "no contact message is shown" )
    public void noContactMessage () {
        try {
            assertEquals( "No such passenger as first contact. ",
                    driver.findElement( By.xpath(
                            "/html/body/div/div[1]/div[1]/div/div/div/div/div/div[2]/div[2]/div/div/div[2]/div[6]" ) )
                            .getText() );
        }
        catch ( final Exception e ) {
            fail();
        }
    }

}
