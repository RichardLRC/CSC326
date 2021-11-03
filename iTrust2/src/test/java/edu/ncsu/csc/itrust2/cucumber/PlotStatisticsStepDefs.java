/**
 *
 */
package edu.ncsu.csc.itrust2.cucumber;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

/**
 * Step Definitions for Plot Statistics class
 *
 * @author junhuasu
 *
 */
public class PlotStatisticsStepDefs extends CucumberTest {

    private final String baseUrl = "http://localhost:8080/iTrust2";

    private void setTextField ( final By byval, final String value ) {
        final WebElement elem = driver.findElement( byval );
        elem.clear();
        elem.sendKeys( value );
    }

    // random HCP has logged in
    @Given ( "AAAA has logged in and chosen to plot passenger statistics" )
    public void gotoPlotPage () throws Exception {
        attemptLogout();

        driver.get( baseUrl );
        setTextField( By.name( "username" ), "svang" );
        setTextField( By.name( "password" ), "123456" );
        driver.findElement( By.className( "btn" ) ).click();
        // go to plot statistics page
        ( (JavascriptExecutor) driver ).executeScript( "document.getElementById('PassengerStatistic').click();" );
    }

    @Then ( "Infected passenger list is shown" )
    public void checkList () {
        waitForAngular();

        // check the listed date and infected people
        try {
            assertTrue( driver.findElement( By.id( "Day" ) ).getText().contains( "Jan 28, 2020, 11:27:00 PM" ) );
            assertTrue( driver.findElement( By.id( "Numtotal" ) ).getText().contains( "1" ) );

        }
        catch ( final Exception e ) {
            fail();
        }
    }

}
