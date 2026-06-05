package arch.scenario3;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.collectingAndThen;

/**
 * Zestaw klas {@link MyApplicationModules}, {@link MyViolation} {@link MyViolations} tworzą zestaw pomocniczych klas
 * wspomagających testowanie zalezności pomiedzy modułami, dokładnie zależności cyklicznych.
 * Powstały na bazie orginału ApplicationModules jednak orginał nie ma możliwości/opcji aby wykrywać tylko cykle.
 * Jeśli orginał zacznie taką możliwość dostarczać zestaw klas My* przestanie być potrzebny.
 */
public class MyViolations extends RuntimeException {
    private static final long serialVersionUID = 6863781504675034691L;

    public static MyViolations NONE = new MyViolations(Collections.emptyList());

    private final List<MyViolation> violations;

    private MyViolations(List<MyViolation> violations) {
        Assert.notNull(violations, "Exceptions must not be null!");
        this.violations = violations;
    }

    static Collector<String, ?, MyViolations> toViolations() {
        return mapping(MyViolation::new, newViolations());
    }

    @Override
    public String getMessage() {
        return violations.stream() //
                .map(MyViolation::getMessage) //
                .collect(Collectors.joining("\n- ", "- ", ""));
    }

    public List<String> getMessages() {
        return violations.stream() //
                .map(MyViolation::getMessage)
                .toList();
    }

    public boolean hasViolations() {
        return !violations.isEmpty();
    }

    public void throwIfPresent() {
        if (hasViolations()) {
            throw this;
        }
    }

    public MyViolations filter(Predicate<MyViolation> filter) {
        Assert.notNull(filter, "Filter must not be null!");
        return violations.stream()
                .filter(filter)
                .collect(newViolations());
    }

    MyViolations and(MyViolation violation) {
        Assert.notNull(violation, "Violation must not be null!");
        return and(new MyViolations(List.of(violation)));
    }

    MyViolations and(MyViolations that) {
        Assert.notNull(that, "Violations must not be null!");
        return hasViolations() || that.hasViolations()
                ? new MyViolations(unionByMessage(violations, that.violations))
                : NONE;
    }

    MyViolations and(String violation) {
        return and(new MyViolation(violation));
    }

    private List<MyViolation> unionByMessage(List<MyViolation> left, List<MyViolation> right) {
        if (left.isEmpty()) {
            return right;
        }
        if (right.isEmpty()) {
            return left;
        }
        var result = new ArrayList<>(left);
        var messages = left.stream().map(MyViolation::getMessage).toList();
        right.stream()
                .filter(it -> !messages.contains(it.message))
                .forEach(result::add);
        return result.size() == left.size() ? left : result;
    }

    private static Collector<MyViolation, ?, MyViolations> newViolations() {
        return collectingAndThen(Collectors.toList(), MyViolations::new);
    }
}