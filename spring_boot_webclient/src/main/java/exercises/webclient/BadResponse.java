package exercises.webclient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Builder
@AllArgsConstructor
@Jacksonized
@Getter
public class BadResponse {

    private final String errorCode;
    private final String errorMessage;

}
