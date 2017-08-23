package promitech.pact_example;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.jayway.restassured.RestAssured;

import promitech.pact_example.service.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProviderTest {

    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void shouldFetchProductDetails() {

        when()
            .get("/product/{id}", 1)
        .then()
            .statusCode(HttpStatus.SC_OK)
            .body("id", Matchers.is(1))
            .body("code", is("code1"));
    }
    
    
}
