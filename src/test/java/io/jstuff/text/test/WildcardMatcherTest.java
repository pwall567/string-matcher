/*
 * @(#) WildcardMatcherTest.java
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

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import io.jstuff.text.WildcardMatcher;

public class WildcardMatcherTest {

    @Test
    public void shouldMatchSimpleText() {
        WildcardMatcher matcher = new WildcardMatcher("Fred");
        assertTrue(matcher.matches("Fred"));
        assertFalse(matcher.matches("Free"));
        assertFalse(matcher.matches("Freddy"));
    }

    @Test
    public void shouldMatchTextWithSingleCharacterWildcard() {
        WildcardMatcher matcher = new WildcardMatcher("Fre?");
        assertTrue(matcher.matches("Fred"));
        assertTrue(matcher.matches("Free"));
        assertFalse(matcher.matches("Freddy"));
    }

    @Test
    public void shouldMatchTextWithMultiCharacterWildcardAtEnd() {
        WildcardMatcher matcher = new WildcardMatcher("Fre*");
        assertTrue(matcher.matches("Fred"));
        assertTrue(matcher.matches("Free"));
        assertTrue(matcher.matches("Freddy"));
        assertFalse(matcher.matches("Friend"));
    }

    @Test
    public void shouldMatchTextWithMultiCharacterWildcardAtStart() {
        WildcardMatcher matcher = new WildcardMatcher("*'s dog");
        assertTrue(matcher.matches("Fred's dog"));
        assertTrue(matcher.matches("Freddy's dog"));
        assertTrue(matcher.matches("Joe's dog"));
        assertFalse(matcher.matches("Fred's cat"));
    }

    @Test
    public void shouldMatchTextWithMultiCharacterWildcardInMiddle() {
        WildcardMatcher matcher = new WildcardMatcher("Fre*'s dog");
        assertTrue(matcher.matches("Fred's dog"));
        assertTrue(matcher.matches("Free's dog"));
        assertTrue(matcher.matches("Freddy's dog"));
        assertFalse(matcher.matches("Freddy's cat"));
    }

    @Test
    public void shouldMatchTextWithCustomSingleCharacterWildcard() {
        WildcardMatcher matcher = new WildcardMatcher("Fre%", '%', WildcardMatcher.defaultMultiMatchChar);
        assertTrue(matcher.matches("Fred"));
        assertTrue(matcher.matches("Free"));
        assertFalse(matcher.matches("Freddy"));
    }

    @Test
    public void shouldMatchTextWithCustomSingleCharacterWildcardUsingWith() {
        WildcardMatcher matcher = new WildcardMatcher("Fre%", WildcardMatcher.defaultSingleMatchChar, '%');
        assertTrue(matcher.matches("Fred"));
        assertTrue(matcher.matches("Free"));
        assertTrue(matcher.matches("Freddy"));
        assertFalse(matcher.matches("Friend"));
    }

    @Test
    public void shouldMatchTextWithMultipleMultiCharacterWildcards() {
        WildcardMatcher matcher = new WildcardMatcher("abc*ghi*mno*xyz");
        assertTrue(matcher.matches("abcdefghijklmnopqrstuvwxyz"));
        assertTrue(matcher.matches("abcghimnoxyz"));
        assertTrue(matcher.matches("abcmnoghixyzmnoxyz"));
        assertFalse(matcher.matches("abcdefghijklmno"));
        assertFalse(matcher.matches("abcdefghijklmnoooooo"));
        assertFalse(matcher.matches("abcdefghijkqqqxyz"));
    }

    @Test
    public void shouldMatchTextWithMultipleMultiCharacterWildcardsAtStartAndEnd() {
        WildcardMatcher matcher = new WildcardMatcher("*sex*");
        assertTrue(matcher.matches("sex education"));
        assertTrue(matcher.matches("Essex"));
        assertTrue(matcher.matches("You sexy thing"));
        assertFalse(matcher.matches("s e x y"));
    }

    @Test
    public void shouldMatchTextWithConsecutiveMultiCharacterWildcards() {
        WildcardMatcher matcher = new WildcardMatcher("File**.txt");
        assertTrue(matcher.matches("File.txt"));
        assertTrue(matcher.matches("File1.txt"));
        assertTrue(matcher.matches("File12.txt"));
        assertTrue(matcher.matches("File123.txt"));
    }

    @Test
    public void shouldMatchTextWithCombinationOfSingleAndMultiCharacterWildcards() {
        WildcardMatcher matcher = new WildcardMatcher("File?*.txt");
        assertFalse(matcher.matches("File.txt"));
        assertTrue(matcher.matches("File1.txt"));
        assertTrue(matcher.matches("File12.txt"));
        assertTrue(matcher.matches("File123.txt"));
    }

    @Test
    public void shouldMatchEmptyPattern() {
        WildcardMatcher matcher = new WildcardMatcher("");
        assertTrue(matcher.matches(""));
        assertFalse(matcher.matches("a"));
    }

    @Test
    public void shouldThrowExceptionOnNullPattern() {
        NullPointerException npe = assertThrows(NullPointerException.class, () -> new WildcardMatcher(null));
        assertEquals("Pattern must not be null", npe.getMessage());
    }

    @Test
    public void shouldThrowExceptionOnNullTarget() {
        WildcardMatcher matcher = new WildcardMatcher("test");
        NullPointerException npe = assertThrows(NullPointerException.class, () -> matcher.matches(null));
        assertEquals("Target must not be null", npe.getMessage());
    }

}
