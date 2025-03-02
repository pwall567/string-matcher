# string-matcher

[![Build Status](https://github.com/pwall567/string-matcher/actions/workflows/build.yml/badge.svg)](https://github.com/pwall567/string-matcher/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/io.jstuff/string-matcher?label=Maven%20Central)](https://central.sonatype.com/artifact/io.jstuff/string-matcher)

String matching functions

## Background

There is a frequent requirement to match strings, either against a constant value or against a pattern involving
wildcard characters or regular expressions.
The `string-matcher` library allows string comparisons to be defined in a number of ways, from simple matching for
equality, case-insensitive matching, wildcard matching, regular expression matching or any combination of those forms.

## Quick Start

As an example, suppose you want to filter a set of files by name.
The function could look something like this:
```java
    public void processFiles(List<File> list, StringMatcher filter) {
        for (File file : list) {
            if (filter.matches(file.getName())) {
                // process file
            }
        }
    }
```

The caller could supply, for example, a wildcard pattern:
```java
        processFiles(fileList, SimpleMatcher.wildcard("File*.txt"));
```
Or a list of strings:
```java
        processFiles(fileList, SimpleMatcher.alternate("File1.txt", "File2.txt"));
```
Or even a combination or the two:
```java
        processFiles(fileList, SimpleMatcher.alternate(
                SimpleMatcher.wildcard("File*.txt"),
                SimpleMatcher.simple("README.txt")
        ));
```

## Reference

### `StringMatcher`

The `StringMatcher` interface specifies a single function:

- boolean matches(CharSequence target)

This does exactly what would be expected, it returns true if the supplied string (specified as a `CharSequence`, so a
`String`, a `StringBuilder` or any other class that implements `CharSequence` may be used) matches the conditions of the
`StringMatcher`.

The interface also specifies a number of static functions to create the various types of implementing class:

- `static SimpleMatcher simple(String string)` (creates a [`SimpleMatcher`](#simplematcher))
- `static CaseInsensitiveMatcher caseInsensitive(String string)` (creates a
  [`CaseInsensitiveMatcher`](#caseinsensitivematcher))
- `static WildcardMatcher wildcard(String pattern)` (creates a [`WildcardMatcher`](#wildcardmatcher))
- `static PatternMatcher pattern(Pattern pattern)` (creates a [`PatternMatcher`](#patternmatcher))
- `static AlternateMatcher alternate(StringMatcher ... matchers)` (creates an [`AlternateMatcher`](#alternatematcher))
- `static AlternateMatcher alternate(String ... strings)` (creates an [`AlternateMatcher`](#alternatematcher) with a
  `SimpleMatcher` for each string)

### `SimpleMatcher`

This is the simplest form of `StringMatcher`; it performs a comparison with a given `String`.
```java
        StringMatcher matcher = new SimpleMatcher("Exact match");
```

The `SimpleMatcher` may also be created by `StringMatcher.simple(string)`.

### `CaseInsensitiveMatcher`

This is the same as `SimpleMatcher` except that it performs a case-insensitive comparison.
```java
        StringMatcher matcher = new CaseInsensitiveMatcher("test");
```

The `CaseInsensitiveMatcher` may also be created by `StringMatcher.caseInsensitive(string)`.

### `WildcardMatcher`

This form of `StringMatcher` performs a standard &ldquo;wildcard&rdqio; match, where a &ldquo;`?`&rdquo; will match any
single character, and a &ldquo;`*`&rdquo; will match zero or more characters.
```java
        StringMatcher matcher = new WildcardMatcher("File*.txt");
```

It is also possible to specify alternative characters to be used in place of the standard matching characters.
For example, to create a `WildcardMatcher` that uses the SQL wildcard characters:
```java
        StringMatcher matcher = new WildcardMatcher("File_.%", '_', '%');
```

The `WildcardMatcher` may also be created by `StringMatcher.wildcard(pattern)`.

### `PatternMatcher`

This form of `StringMatcher` brings the full power of regular expression to the matching function:
```java
        StringMatcher matcher = new PatternMatcher(Pattern.compile("^File[0-9]{1,3}$"));
```

The `PatternMatcher` may also be created by `StringMatcher.pattern(pattern)`.

### `AlternateMatcher`

The `AlternateMatcher` allows a set of alternate matchers to be specified:
```java
        StringMatcher exactMatcher = new SimpleMatcher("README.txt");
        StringMatcher fileMatcher = new WildcardMatcher("File*.txt");
        StringMatcher matcher = new AlternateMatcher(exactMatcher, fileMatcher);
```

The `AlternateMatcher` may also be created by `StringMatcher.alternate(matcher, matcher)` (supplying a set of
`StringMatcher`) or `StringMatcher.alternate(string, string)` (which will convert each string to a `SimpleMatcher`).

## Dependency Specification

The latest version of the library is 1.0, and it may be obtained from the Maven Central repository.

### Maven
```xml
    <dependency>
      <groupId>io.jstuff</groupId>
      <artifactId>string-matcher</artifactId>
      <version>1.0</version>
    </dependency>
```
### Gradle
```groovy
    implementation 'io.jstuff:string-matcher:1.0'
```
### Gradle (kts)
```kotlin
    implementation("io.jstuff:string-matcher:1.0")
```

Peter Wall

2025-03-02
