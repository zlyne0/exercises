package arch.scenario3;

import com.tngtech.archunit.lang.FailureReport;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.core.JavaPackage;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Zestaw klas {@link MyApplicationModules}, {@link MyViolation} {@link MyViolations} tworzą zestaw pomocniczych klas
 * wspomagających testowanie zalezności pomiedzy modułami, dokładnie zależności cyklicznych.
 * Powstały na bazie orginału ApplicationModules jednak orginał nie ma możliwości/opcji aby wykrywać tylko cykle.
 * Jeśli orginał zacznie taką możliwość dostarczać zestaw klas My* przestanie być potrzebny.
 */
public class MyApplicationModules {

    private final ApplicationModules applicationModules;
    private final List<JavaPackage> rootPackages;
    private Method assertNoCyclesFor;

    public MyApplicationModules(ApplicationModules applicationModules) {
        this.applicationModules = applicationModules;
        this.rootPackages = (List<JavaPackage>)reflectionFieldValue(applicationModules, "rootPackages");
    }

    private Object reflectionFieldValue(Object target, String fieldName) {
        Field field = ReflectionUtils.findField(target.getClass(), fieldName);
        if (field == null) {
            throw new IllegalStateException("Field '" + fieldName + "' not found in " + target.getClass());
        }
        ReflectionUtils.makeAccessible(field);
        return ReflectionUtils.getField(field, target);
    }

    public MyApplicationModules verify() {
        MyViolations violations = detectViolations();
        violations.throwIfPresent();
        return this;
    }

    public MyViolations detectViolations() {
        return rootPackages.stream()
                .map(this::assertNoCyclesFor)
                .flatMap(it -> it.getDetails().stream())
                .collect(MyViolations.toViolations());
    }

    private FailureReport assertNoCyclesFor(JavaPackage rootPackage) {
        if (assertNoCyclesFor == null) {
            assertNoCyclesFor = ReflectionUtils.findMethod(applicationModules.getClass(), "assertNoCyclesFor", JavaPackage.class);
            if (assertNoCyclesFor == null) {
                throw new IllegalArgumentException("Method 'assertNoCyclesFor' not found in " + applicationModules.getClass());
            }
            ReflectionUtils.makeAccessible(assertNoCyclesFor);
        }
        return (FailureReport)ReflectionUtils.invokeMethod(assertNoCyclesFor, applicationModules, rootPackage);
    }
}