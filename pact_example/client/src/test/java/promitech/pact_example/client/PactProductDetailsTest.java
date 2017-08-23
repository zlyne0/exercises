package promitech.pact_example.client;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import au.com.dius.pact.consumer.ConsumerPactTest;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;

public class PactProductDetailsTest extends ConsumerPactTest {

    @Override
    protected PactFragment createFragment(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json;charset=UTF-8");
        
        return builder.uponReceiving("a request for product details")
            .path("/product/101")
            .method("GET")
            .willRespondWith()
            .headers(headers)
            .status(200)
            .body("{\"id\":101,\"code\":\"code101\"}")
            .toFragment();
    }

    @Override
    protected String providerName() {
        return "ProductDetailsProviderName";
    }

    @Override
    protected String consumerName() {
        return "ProductDetailsConsumerName";
    }

    @Override
    protected void runTest(String url) throws IOException {
        ProductDetailsClient client = new ProductDetailsClient(url);
        ProductDetails productDetails = client.getProductDetails(101L);
        
        assertEquals(new Long(101L), productDetails.getId());
        assertEquals("code101", productDetails.getCode());
    }


}
