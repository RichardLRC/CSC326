/**
 *
 */
package edu.ncsu.csc.itrust2.apitest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test APIPassengerStatisticsController class
 *
 * @author junhuasu
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
@FixMethodOrder ( MethodSorters.NAME_ASCENDING )
public class APIPassengerStatisticsControllerTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests Number Of Severity Code
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "USER", "HCP" } )
    public void testNumOfSeverityCode () throws Exception {
        mvc.perform( get( "/api/v1/getnumOfSeverityCode" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
        mvc.perform( get( "/api/v1/getnumOfSeverityCode/nope" ) ).andExpect( status().is4xxClientError() );

    }

    /**
     * Tests Number Of infected people per day
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "USER", "HCP" } )
    public void testNumOfInfectperDay () throws Exception {
        mvc.perform( get( "/api/v1/getnumOfInfectperDay" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
        mvc.perform( get( "/api/v1/getnumOfInfectperDay/nope" ) ).andExpect( status().is4xxClientError() );

    }

    /**
     * Tests Number Of Severity Code
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "USER", "HCP" } )
    public void testgetnumOfSeverityCodedata () throws Exception {
        mvc.perform( get( "/api/v1/getnumOfSeverityCodedata" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
        mvc.perform( get( "/api/v1/getnumOfSeverityCodedata/nope" ) ).andExpect( status().is4xxClientError() );

    }

    /**
     * Tests Number Of infected people per day
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "USER", "HCP" } )
    public void testgetnumOfInfectperDayDate () throws Exception {
        mvc.perform( get( "/api/v1/getnumOfInfectperDayDate" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
        mvc.perform( get( "/api/v1/getnumOfInfectperDayDate/nope" ) ).andExpect( status().is4xxClientError() );

    }

    /**
     * Tests Number Of infected people per day
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "USER", "HCP" } )
    public void testgetnumOfInfectperDayData () throws Exception {
        mvc.perform( get( "/api/v1/getnumOfInfectperDayData" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );
        mvc.perform( get( "/api/v1/getnumOfInfectperDayData/nope" ) ).andExpect( status().is4xxClientError() );

    }
}
