package arch.scenario3;

import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Zestaw klas {@link MyApplicationModules}, {@link MyViolation} {@link MyViolations} tworzą zestaw pomocniczych klas
 * wspomagających testowanie zalezności pomiedzy modułami, dokładnie zależności cyklicznych.
 * Powstały na bazie orginału ApplicationModules jednak orginał nie ma możliwości/opcji aby wykrywać tylko cykle.
 * Jeśli orginał zacznie taką możliwość dostarczać zestaw klas My* przestanie być potrzebny.
 */
public class MyViolation {

    final String message;

    MyViolation(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public boolean hasMessageContaining(String candidate) {
        Assert.hasText(candidate, "Candidate must not be null or empty!");
        return message.contains(candidate);
    }

    @Override
    public String toString() {
        return message;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj
                || obj instanceof MyViolation that
                && Objects.equals(this.message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(message);
    }
}
