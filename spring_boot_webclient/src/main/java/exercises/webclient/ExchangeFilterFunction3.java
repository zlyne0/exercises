package exercises.webclient;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.http.client.reactive.ClientHttpRequestDecorator;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HexFormat;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

// https://stackoverflow.com/a/70068247
public class ExchangeFilterFunction3 implements ExchangeFilterFunction {
    private final Logger logger;

    public ExchangeFilterFunction3(Logger logger) {
        this.logger = logger;
    }

    @Override
    @NonNull
    public Mono<ClientResponse> filter(@NonNull ClientRequest request, @NonNull ExchangeFunction next) {
        if (!logger.isDebugEnabled()) {
            return next.exchange(request);
        }

        String requestId = generateRequestId();

        if (logger.isTraceEnabled()) {
            var message = new StringBuilder();
            message.append("HTTP request start; request-id=")
                    .append(requestId).append('\n')
                    .append(request.method()).append(' ')
                    .append(request.url());
            request.headers().forEach((String name, List<String> values) -> {
                for (String value : values) {
                    message.append('\n').append(name).append(": ").append(value);
                }
            });
            logger.trace(message.toString());
        } else {
            logger.debug("HTTP request; request-id=" + requestId + '\n' +
                    request.method() + ' ' + request.url());
        }

        if (logger.isTraceEnabled()) {
            var bodyInserter = new LoggingBodyInserter(logger, requestId, request.body());
            request = ClientRequest.from(request).body(bodyInserter).build();
        }

        return next.exchange(request).map(new LoggingClientResponseTransformer(logger, requestId));
    }

    private static String generateRequestId() {
        return UUID.randomUUID().toString();
    }

    private static class LoggingBodyInserter implements BodyInserter<Object, ClientHttpRequest> {
        private final Logger logger;
        private final String requestId;
        private final BodyInserter<?, ? super ClientHttpRequest> originalBodyInserter;

        private LoggingBodyInserter(Logger logger, String requestId,
                                    BodyInserter<?, ? super ClientHttpRequest> originalBodyInserter) {
            this.logger = logger;
            this.requestId = requestId;
            this.originalBodyInserter = originalBodyInserter;
        }

        @Override
        @NonNull
        public Mono<Void> insert(@NonNull ClientHttpRequest outputMessage, @NonNull Context context) {
            var loggingOutputMessage = new LoggingClientHttpRequestDecorator(outputMessage, logger, requestId);
            return originalBodyInserter.insert(loggingOutputMessage, context);
        }
    }

    private static class LoggingClientHttpRequestDecorator extends ClientHttpRequestDecorator {
        private final Logger logger;
        private final String requestId;

        public LoggingClientHttpRequestDecorator(ClientHttpRequest delegate, Logger logger, String requestId) {
            super(delegate);
            this.logger = logger;
            this.requestId = requestId;
        }

        @Override
        @NonNull
        public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
            Flux<? extends DataBuffer> loggingBody = Flux.from(body)
                    .doOnNext(this::logDataBuffer)
                    .doOnComplete(this::logComplete)
                    .doOnError(this::logError);
            return super.writeWith(loggingBody);
        }

        @Override
        @NonNull
        public Mono<Void> setComplete() {
            logger.trace("HTTP request end; request-id=" + requestId);
            return super.setComplete();
        }

        private void logDataBuffer(DataBuffer dataBuffer) {
            int readPosition = dataBuffer.readPosition();
            byte[] data = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(data);
            dataBuffer.readPosition(readPosition);
            logger.trace("HTTP request data; request-id=" + requestId + '\n' + bytesToString(data));
        }

        private void logComplete() {
            logger.trace("HTTP request end; request-id=" + requestId);
        }

        private void logError(Throwable exception) {
            logger.trace("HTTP request error; request-id=" + requestId, exception);
        }
    }

    private static class LoggingClientResponseTransformer implements Function<ClientResponse, ClientResponse> {
        private final Logger logger;
        private final String requestId;

        private LoggingClientResponseTransformer(Logger logger, String requestId) {
            this.logger = logger;
            this.requestId = requestId;
        }

        @Override
        public ClientResponse apply(ClientResponse clientResponse) {
            if (logger.isTraceEnabled()) {
                var message = new StringBuilder();
                message.append("HTTP response start; request-id=").append(requestId).append('\n')
                        .append("HTTP ").append(clientResponse.statusCode());
                clientResponse.headers().asHttpHeaders().forEach((String name, List<String> values) -> {
                    for (String value : values) {
                        message.append('\n').append(name).append(": ").append(value);
                    }
                });
                logger.trace(message.toString());
            } else {
                logger.debug("HTTP response; request-id=" + requestId + '\n' +
                        "HTTP " + clientResponse.statusCode());
            }

            return clientResponse.mutate()
                    .body(new ClientResponseBodyTransformer(logger, requestId))
                    .build();
        }
    }

    private static class ClientResponseBodyTransformer implements Function<Flux<DataBuffer>, Flux<DataBuffer>> {
        private final Logger logger;
        private final String requestId;
        private boolean completed = false;

        private ClientResponseBodyTransformer(Logger logger, String requestId) {
            this.logger = logger;
            this.requestId = requestId;
        }

        @Override
        public Flux<DataBuffer> apply(Flux<DataBuffer> body) {
            return body
                    .doOnNext(this::logDataBuffer)
                    .doOnComplete(this::logComplete)
                    .doOnError(this::logError);
        }

        private void logDataBuffer(DataBuffer dataBuffer) {
            int readPosition = dataBuffer.readPosition();
            byte[] data = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(data);
            dataBuffer.readPosition(readPosition);
            logger.trace("HTTP response data; request-id=" + requestId + '\n' + bytesToString(data));
        }

        private void logComplete() {
            if (!completed) {
                logger.trace("HTTP response end; request-id=" + requestId);
                completed = true;
            }
        }

        private void logError(Throwable exception) {
            logger.trace("HTTP response error; request-id=" + requestId, exception);
        }
    }

    private static String bytesToString(byte[] bytes) {
        var string = new StringBuilder(bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            byte b1 = bytes[i];
            if (b1 >= 0) {
                if (32 <= b1 && b1 < 127) { // ordinary ASCII characters
                    string.append((char) b1);
                } else {  // control characters
                    switch (b1) {
                        case '\t' -> string.append("\t");
                        case '\n' -> string.append("\n");
                        case '\r' -> string.append("\r");
                        default -> {
                            string.append("\\x");
                            HexFormat.of().toHexDigits(string, b1);
                        }
                    }
                }
                continue;
            }
            if ((b1 & 0xe0) == 0xc0) { // UTF-8 first byte of 2-bytes sequence
                i++;
                if (i < bytes.length) {
                    byte b2 = bytes[i];
                    if ((b2 & 0xc0) == 0x80) { // UTF-8 second byte of 2-bytes sequence
                        char c = (char) ((b1 & 0x1f) << 6 | b2 & 0x3f);
                        if (Character.isLetter(c)) {
                            string.append(c);
                            continue;
                        }
                    }
                    string.append("\\x");
                    HexFormat.of().toHexDigits(string, b1);
                    string.append("\\x");
                    HexFormat.of().toHexDigits(string, b2);
                    continue;
                }
            }
            string.append("\\x");
            HexFormat.of().toHexDigits(string, b1);
        }
        return string.toString();
    }
}