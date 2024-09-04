package exercises.webclient;

import lombok.Builder;

@Builder
public record Request(
        String firstName,
        String lastName,
        String contractNumber,
        int period
) {
}
