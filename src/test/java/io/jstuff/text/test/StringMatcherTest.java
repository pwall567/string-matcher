/*
 * @(#) StringMatcherTest.java
 *
 * string-matcher  String matching functions
 * Copyright (c) 2025 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.jstuff.text.test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import io.jstuff.text.StringMatcher;

public class StringMatcherTest {

    @Test
    public void shouldCreateWildcardMatcher() {
        StringMatcher matcher = StringMatcher.wildcard("Fre?");
        assertTrue(matcher.matches("Fred"));
        assertTrue(matcher.matches("Free"));
        assertFalse(matcher.matches("Freddy"));
    }

    @Test
    public void shouldCreateSimpleMatcher() {
        StringMatcher matcher = StringMatcher.simple("Fred");
        assertTrue(matcher.matches("Fred"));
        assertFalse(matcher.matches("Free"));
        assertFalse(matcher.matches("Freddy"));
    }

    @Test
    public void shouldCreateCaseInsensitiveMatcher() {
        StringMatcher matcher = StringMatcher.caseInsensitive("Fred");
        assertTrue(matcher.matches("Fred"));
        assertTrue(matcher.matches("fred"));
        assertTrue(matcher.matches("freD"));
        assertTrue(matcher.matches("FRED"));
        assertFalse(matcher.matches("Freddy"));
    }

    @Test
    public void shouldCreatePatternMatcher() {
        StringMatcher matcher = StringMatcher.pattern(Pattern.compile("^File[0-9]{1,3}$"));
        assertTrue(matcher.matches("File1"));
        assertTrue(matcher.matches("File999"));
        assertFalse(matcher.matches("File999XXX"));
        assertFalse(matcher.matches("AFile999"));
    }

    @Test
    public void shouldCreateAlternateMatcher() {
        StringMatcher matcher = StringMatcher.alternate(StringMatcher.wildcard("Fre?"), StringMatcher.simple("Joe"));
        assertTrue(matcher.matches("Fred"));
        assertTrue(matcher.matches("Free"));
        assertTrue(matcher.matches("Joe"));
        assertFalse(matcher.matches("Harry"));
    }

    @Test
    public void shouldCreateAlternateMatcherFromStrings() {
        StringMatcher matcher = StringMatcher.alternate("Fred", "Joe");
        assertTrue(matcher.matches("Fred"));
        assertTrue(matcher.matches("Joe"));
        assertFalse(matcher.matches("Harry"));
    }

    @Test
    public void shouldCreateAlternateMatcherFromList() {
        List<String> list = new ArrayList<>();
        list.add("Fred");
        list.add("Joe");
        StringMatcher matcher = StringMatcher.alternate(list);
        assertTrue(matcher.matches("Fred"));
        assertTrue(matcher.matches("Joe"));
        assertFalse(matcher.matches("Harry"));
    }

    @Test
    public void shouldThrowExceptionCreatingAlternateMatcherWithNull() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> StringMatcher.alternate("abc", null));
        assertEquals("String must not be null", npe.getMessage());
    }

    @Test
    public void shouldThrowExceptionCreatingAlternateMatcherFromListWithNull() {
        List<String> list = new ArrayList<>();
        list.add("abc");
        list.add(null);
        NullPointerException npe = assertThrows(NullPointerException.class, () -> StringMatcher.alternate(list));
        assertEquals("String must not be null", npe.getMessage());
    }

}
