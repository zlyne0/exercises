package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AService {

    private BService bService1;
    private BService bService2;

    public AService(BService bService1, BService bService2) {
        this.bService1 = bService1;
        this.bService2 = bService2;

        System.out.println("A service constructor");
    }

    @PostConstruct
    void postConstruct() {
        this.bService1.setValue("one");
        this.bService2.setValue("two");
    }

    public void method() {
        System.out.println("A service method");
        bService1.method();
        bService2.method();
    }
}

class BAbstract {

    protected String value = "";

    void setValue(String value) {
        this.value = value;
    }

}

@Component
@Scope("prototype")
class BService extends BAbstract {

    private static final AtomicInteger counter = new AtomicInteger(0);

    private final CService cService;

    public BService(CService cService) {
        this.cService = cService;
        counter.incrementAndGet();
        System.out.println("B service constructor, counter " + counter.get());
    }

    public void method() {
        System.out.println("B service method " + counter.get());
        System.out.println("  value: " + value);
        cService.method();
    }


}

@Component
//@Scope("prototype")
class CService {

    private static final AtomicInteger counter = new AtomicInteger(0);

    public CService() {
        counter.incrementAndGet();
        System.out.println("C service constructor, counter " + counter.get());
    }

    public void method() {
        System.out.println("C service method " + counter.get());
    }

}
