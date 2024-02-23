Subjective point of view of checkstyle in java.
Rules base on google and sun checkstyle with modification I considered useful or practical.
Rules check in maven build process 


### Maven Configuration 
added checkstyle plugin 

###

There is tradeoff with code identation and closing bracket.

Violation1 and Tradeoff with closing bracket 
```java
class Repository {
    @Query("""
        select e.id 
        from Entity e
        where e.id in (
            select id 
            from Entity2 
            where name = 'z' 
        )
    """)                    <-- violation
    void queryMethod();
}
```
... correct ...
```java
class Repository {
    @Query("""
        select e.id 
        from Entity e
        where e.id in (
            select id 
            from Entity2 
            where name = 'z')""")                    <-- violation
    void queryMethod();
}
```

Violation2
```java
class SomeClass {
 
    public static void method() {
        methodA()
            .methodC()
            .methodD(
                () -> { runMEthodF(); },
                () -> { runMethodE(); }
            );                    // <-- violation
    }
}
```
... correct ...
```java
class SomeClass {
 
    public static void method() {
        methodA()
            .methodC()
            .methodD(
                () -> { runMEthodF(); },
                () -> { runMethodE(); });
    }
}
```

I prefer code in violation 1 and 2 but accept solution to home correct identation formating 
in differetn situations. 