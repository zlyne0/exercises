package exercises.webclient;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContractRestClientTest {

    @RegisterExtension
    static WireMockExtension wireMock = WireMockExtension.newInstance()
            .options(wireMockConfig().dynamicPort())
            .build();

    ContractRestClient restClient;

    @BeforeAll
    static void beforeAll() {
        Logger restClientLogger = (Logger) LoggerFactory.getLogger(ContractRestClient.class);
        restClientLogger.setLevel(Level.TRACE);
    }

    @BeforeEach
    void beforeEach() {
        restClient = new ContractRestClient(wireMock.baseUrl());
    }

    @Test
    void should_invoke_service() {
        // given
        wireMock.stubFor(post("/contracts/1")
                .withRequestBody(equalToJson("""
                        { 
                            "firstName": "Jan",
                            "lastName": "Kowalski",
                            "contractNumber": "234U",
                            "period": 10
                        } """))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                                "id": 10001
                            }"""))
        );

        // when
        Response response = restClient.createCustomer(createRequest().build());

        // then
        assertEquals(10001, response.getId());
    }

    @Test
    void should_handle_4xx_http_error() {
        // given
        wireMock.stubFor(post("/contracts/1")
                .withRequestBody(equalToJson("""
                        { 
                            "firstName": "Jan",
                            "lastName": "Kowalski",
                            "contractNumber": "234U",
                            "period": 10
                        } """))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                                "errorCode": "field1",
                                "errorMessage": ""
                            }"""))
        );

        // when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> restClient.createCustomer(createRequest().build()));
        // then
        assertThat(exception).hasMessageStartingWith("400 error code, errorMessage");
    }

    private Request.RequestBuilder createRequest() {
        return Request.builder()
                .firstName("Jan")
                .lastName("Kowalski")
                .contractNumber("234U")
                .period(10);
    }
}