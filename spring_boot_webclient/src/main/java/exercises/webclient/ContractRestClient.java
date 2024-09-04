package exercises.webclient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class ContractRestClient {

    private final String address;

    public Response createCustomer(Request request) {
        int timeout = 30;
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.of(timeout, ChronoUnit.SECONDS))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(timeout))
                        .addHandlerLast(new WriteTimeoutHandler(timeout))
                );
        ClientHttpConnector conn = new ReactorClientHttpConnector(httpClient);

        WebClient client = WebClient.builder()
                .clientConnector(conn)
                .baseUrl(address)
                .filter(new ExchangeFilterFunction3(log))
                .build();

        Function<ClientResponse, Mono<? extends Throwable>> http4xxToException = new Function<ClientResponse, Mono<? extends Throwable>>() {
            @Override
            public Mono<? extends Throwable> apply(ClientResponse clientResponse) {
                return clientResponse.bodyToMono(BadResponse.class)
                        .map(responseObject -> new RuntimeException("400 error code, errorMessage " + responseObject.getErrorMessage()));
            }
        };
        return client.post()
                .uri(uriBuilder -> uriBuilder.path("/contracts/{id}")
                        .build(Map.of("id", "1")))
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, http4xxToException)
                .bodyToMono(Response.class)
                .block();
    }
}
