package promitech.pact_example;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.web.bind.annotation.ControllerAdvice;

import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import au.com.dius.pact.provider.spring.target.MockMvcTarget;
import promitech.pact_example.service.ProductDetailsController;

@ControllerAdvice
class ProductDetailsControllerAdvice {
    
}

@RunWith(PactRunner.class)
@Provider("ProductDetailsProviderName")
@PactFolder("..\\client\\target\\pacts\\")
public class ProviderPactTest {

    @InjectMocks
    ProductDetailsController productDetailsController = new ProductDetailsController();
    
    @InjectMocks
    ProductDetailsControllerAdvice productDetailsControllerAdvice = new ProductDetailsControllerAdvice();
    
    @TestTarget
    public final MockMvcTarget target = new MockMvcTarget();    
    
//    @ClassRule //Rule will be applied once: before/after whole contract test suite
//    public static final ClientDriverRule embeddedService = new ClientDriverRule(8332);    
    
//    @TestTarget
//    public final Target target = new HttpTarget(5050);    
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        target.setControllers(productDetailsController);
        target.setControllerAdvice(productDetailsControllerAdvice);
    }
    
    @State({"default", "no-data"}) // Method will be run before testing interactions that require "default" or "no-data" state
    public void toDefaultState() {
        target.setRunTimes(1);  //let's loop through this state a few times for a 3 data variants
    }    
}
