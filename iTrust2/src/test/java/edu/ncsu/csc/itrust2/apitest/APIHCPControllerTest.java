package edu.ncsu.csc.itrust2.apitest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
 * Test for API functionality for interacting with HCP
 *
 * @author Jiaqi Ye (jye)
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
@FixMethodOrder ( MethodSorters.NAME_ASCENDING )
public class APIHCPControllerTest {
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
     * Tests upload Passenger API
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testUploadPassenger () throws Exception {

        final String line1 = "3b9aca00, \"Reuchlin, Jonkheer, J. G.\", M, 2020/02/16 21:26:34\n";
        final String line2 = "3b9aca76, \"Drachstedt, Baron von\", M, 2020/02/04 13:18:03\n";
        final String line3 = "3b9acaec, \"Foreman, B. L.\", S, 2020/02/04 20:13:22";
        final String line4 = "3b9acaec, \"Foreman, B. L.\", S, 2020\n";

        final String file = line1 + line2 + line3;
        final String file2 = line1 + line2 + line4;

        mvc.perform( post( "/api/v1/hcp/upload" ).contentType( MediaType.APPLICATION_JSON ).content( file ) )
                .andExpect( status().isOk() );
        mvc.perform( get( "/api/v1/passenger" ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/hcp/upload" ).contentType( MediaType.APPLICATION_JSON ).content( file2 ) )
                .andExpect( status().is4xxClientError() );

    }

    /**
     * Tests upload Passenger API
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "hcp", roles = { "HCP" } )
    public void testFindPreviousContacts () throws Exception {
        final String line1 = "3b9aca00, \"Reuchlin, Jonkheer, J. G.\", M, 2020/02/16 21:26:34\n";
        final String line2 = "3b9aca76, \"Drachstedt, Baron von\", M, 2020/02/04 13:18:03\n";
        final String line3 = "3b9acaec, \"Foreman, B. L.\", S, 2020/02/04 20:13:22";

        final String file = line1 + line2 + line3;

        mvc.perform( post( "/api/v1/hcp/upload" ).contentType( MediaType.APPLICATION_JSON ).content( file ) )
                .andExpect( status().isOk() );

        mvc.perform( get( "/api/v1/passenger" ) ).andExpect( status().isOk() );

        final String contact1 = "3b9aca00,3b9aca76\n";
        final String contact2 = "3b9aca76,3b9acaec\n";
        final String contact3 = "3b9acaec\n";
        final String contacts = contact1 + contact2 + contact3;

        mvc.perform( post( "/api/v1/hcp/findPreviousContacts/3b9aca76/2020-02-04/1" )
                .contentType( MediaType.APPLICATION_JSON ).content( contacts ) ).andExpect( status().isOk() );

        mvc.perform( get( "/api/v1/hcp/findPreviousContacts/results" ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/hcp/findPreviousContacts/3b9acaec/2020-02-04/1" )
                .contentType( MediaType.APPLICATION_JSON ).content( contacts ) ).andExpect( status().isOk() );

        mvc.perform( get( "/api/v1/hcp/findPreviousContacts/results" ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/hcp/findPreviousContacts/3b9acae4/2020-02-04/1" )
                .contentType( MediaType.APPLICATION_JSON ).content( contacts ) )
                .andExpect( status().is4xxClientError() );

        mvc.perform( get( "/api/v1/hcp/findPreviousContacts/results" ) ).andExpect( status().is4xxClientError() );
    }

}
