gradle1 description

project a has dependency to lib_b. lib_b has dependency to commons-math3
Project a see classes from transitive dependency commons-math3
because of "api" selector in build.gradle in lib_b

Project "a" will not see commons-math3 when selector will be implementation
very important is
```
plugins {
    id 'java-library'
}
```
