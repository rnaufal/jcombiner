# Jcombiner

[![Build Status](https://travis-ci.org/rnaufal/jcombiner.svg?branch=master)](https://travis-ci.org/rnaufal/jcombiner)
[![Coverage Status](https://coveralls.io/repos/github/rnaufal/jcombiner/badge.svg?branch=master)](https://coveralls.io/github/rnaufal/jcombiner?branch=master)

JCombiner is a framework to generate combinations of elements in Java

## Requirements

* JDK 10

## Build

```Shell
./gradlew clean build
```
## Usage

1. Create some class which holds collection of elements, let's say the following `People` class:

```java
public class People {

    private List<String> people;

    public People() {
        this.people = new ArrayList<>();
    }

    public void addPerson(final String name) {
        this.people.add(Objects.requireNonNull(name));
    }
}
```
2. Add the `CombinationProperty` annotation to the collection attribute with the desired combination size, let's say `size=3`:

```java
public class People {

    @CombinationProperty(size = 3)
    private List<String> people;

    public People() {
        this.people = new ArrayList<>();
    }

    public void addPerson(final String name) {
        this.people.add(Objects.requireNonNull(name));
    }
}
```

3. Create the target class to hold the combinations result. The target class should have a `Combinations` field for each mapped `CombinationProperty` attribute from the original object. The `Combinations` class is parameterized and it should have the same type argument from the mapped collection attribute, in this case, `String`:

```java
public class PeopleCombinations {

    private Combinations<String> people;

    @Override
    public String toString() {
        return Objects.toString(people);
    }
}
```
By default, if the `CombinationProperty` annotation has no `name` element defined, the `Combinations` attribute must have the same name from the original field. Otherwise, the `Combinations` field name must match the `name` defined in the `CombinationProperty` annotation. In case none of these situations occur, an exception will be throw by the framework.

4. Get a reference to the [JCombiner](https://github.com/rnaufal/jcombiner/blob/master/api/src/main/java/br/com/rnaufal/jcombiner/api/JCombiner.java) service. The `JCombiner` service is the entry point to build field combinations of an object. This framework uses Java 9 [modules](https://www.oracle.com/corporate/features/understanding-java-9-modules.html), so one way to get a reference to it is to use the [ServiceLoader](https://docs.oracle.com/javase/10/docs/api/java/util/ServiceLoader.html) class to load a specific service:

```java
final ServiceLoader<JCombiner> jCombiners = ServiceLoader.load(JCombiner.class);
```

5. Use the `JCombiner` service to build the combinations for each mapped field from the source object:

```java
public class Main {

    public static void main(String[] args) {
        final var people = new People();
        people.addPerson("John");
        people.addPerson("Paul");
        people.addPerson("Patrick");
        people.addPerson("Joe");
        people.addPerson("Thomas");
        people.addPerson("Emma");

        final ServiceLoader<JCombiner> jCombiners = ServiceLoader.load(JCombiner.class);

        jCombiners.forEach(jCombiner -> {
            final var peopleCombinations = (PeopleCombinations) jCombiner.parseCombinations(people, PeopleCombinations.class);
            System.out.println(peopleCombinations);
        });
    }
}
```

6. The result is the following for the mapped `people` field:

```[[John, Paul, Patrick], [John, Paul, Joe], [John, Paul, Thomas], [John, Paul, Emma], [John, Patrick, Joe], [John, Patrick, Thomas], [John, Patrick, Emma], [John, Joe, Thomas], [John, Joe, Emma], [John, Thomas, Emma], [Paul, Patrick, Joe], [Paul, Patrick, Thomas], [Paul, Patrick, Emma], [Paul, Joe, Thomas], [Paul, Joe, Emma], [Paul, Thomas, Emma], [Patrick, Joe, Thomas], [Patrick, Joe, Emma], [Patrick, Thomas, Emma], [Joe, Thomas, Emma]]```

7. A more concrete example can be found on the [client](https://github.com/rnaufal/jcombiner/tree/master/client) module
