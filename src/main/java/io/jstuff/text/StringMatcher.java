/*
 * @(#) StringMatcher.java
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

package io.jstuff.text;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Simple String Matching interface.
 *
 * @author  Peter Wall
 */
@FunctionalInterface
public interface StringMatcher {

    /**
     * Test whether a given {@link CharSequence} (<i>e.g.</i> {@link String}) matches the {@code StringMatcher}.
     *
     * @param   target  the target string
     * @return          {@code true} if the string matches
     */
    boolean matches(CharSequence target);

    /**
     * Create a {@link WildcardMatcher} with the given pattern.
     *
     * @param   pattern the wildcard patterm
     * @return          the {@link WildcardMatcher}
     */
    static WildcardMatcher wildcard(String pattern) {
        return new WildcardMatcher(pattern);
    }

    /**
     * Create a {@link WildcardMatcher} with the given pattern and the specified wildcard characters.
     *
     * @param   pattern             the text pattern
     * @param   singleMatchChar     the character used to represent a single character wildcard match in the pattern
     * @param   multiMatchChar      the character used to represent a multi-character wildcard match in the pattern
     * @return                      the {@link WildcardMatcher}
     */
    static WildcardMatcher wildcard(String pattern, char singleMatchChar, char multiMatchChar) {
        return new WildcardMatcher(pattern, singleMatchChar, multiMatchChar);
    }

    /**
     * Create a {@link SimpleMatcher} with the given comparison string.
     *
     * @param   string  the comparison string
     * @return          the {@link SimpleMatcher}
     */
    static SimpleMatcher simple(String string) {
        return new SimpleMatcher(string);
    }

    /**
     * Create a {@link CaseInsensitiveMatcher} with the given comparison string.
     *
     * @param   string  the comparison string
     * @return          the {@link CaseInsensitiveMatcher}
     */
    static CaseInsensitiveMatcher caseInsensitive(String string) {
        return new CaseInsensitiveMatcher(string);
    }

    /**
     * Create a {@link ContainsMatcher} with the given comparison string.
     *
     * @param   string  the comparison string
     * @return          the {@link ContainsMatcher}
     */
    static ContainsMatcher contains(String string) {
        return new ContainsMatcher(string);
    }

    /**
     * Create a {@link StartsWithMatcher} with the given comparison string.
     *
     * @param   string  the comparison string
     * @return          the {@link ContainsMatcher}
     */
    static StartsWithMatcher startsWith(String string) {
        return new StartsWithMatcher(string);
    }

    /**
     * Create an {@link EndsWithMatcher} with the given comparison string.
     *
     * @param   string  the comparison string
     * @return          the {@link ContainsMatcher}
     */
    static EndsWithMatcher endsWith(String string) {
        return new EndsWithMatcher(string);
    }

    /**
     * Create a {@link PatternMatcher} with the given {@link Pattern}.
     *
     * @param   pattern the {@link Pattern}
     * @return          the {@link PatternMatcher}
     */
    static PatternMatcher pattern(Pattern pattern) {
        return new PatternMatcher(pattern);
    }

    /**
     * Create an {@link AlternateMatcher} with the given set of comparison strings.
     *
     * @param   matchers    the matchers
     * @return              the {@link AlternateMatcher}
     */
    static AlternateMatcher alternate(StringMatcher ... matchers) {
        for (StringMatcher matcher : matchers)
            Objects.requireNonNull(matcher, "Matcher must not be null");
        return new AlternateMatcher(Arrays.copyOf(matchers, matchers.length));
    }

    /**
     * Create an {@link AlternateMatcher} with the given set of comparison strings.
     *
     * @param   strings the comparison strings
     * @return          the {@link AlternateMatcher}
     */
    static AlternateMatcher alternate(String ... strings) {
        int n = strings.length;
        StringMatcher[] matchers = new StringMatcher[n];
        for (int i = 0; i < n; i++)
            matchers[i] = new SimpleMatcher(Objects.requireNonNull(strings[i], "String must not be null"));
        return new AlternateMatcher(matchers);
    }

    /**
     * Create an {@link AlternateMatcher} with the given {@link Collection} of comparison strings.
     *
     * @param   strings the comparison strings
     * @return          the {@link AlternateMatcher}
     */
    static AlternateMatcher alternate(Collection<String> strings) {
        StringMatcher[] matchers = new StringMatcher[strings.size()];
        int i = 0;
        for (String string : strings)
            matchers[i++] = new SimpleMatcher(Objects.requireNonNull(string, "String must not be null"));
        return new AlternateMatcher(matchers);
    }

    /**
     * Compare characters in two {@link CharSequence} objects.  No checking is performed on offsets or length; the
     * caller is expected to have checked that all characters are within the bounds of the {@link CharSequence} objects,
     * and that the objects will not be modified during the comparison in another thread.
     *
     * @param   cs1         the first {@link CharSequence}
     * @param   offset1     the start offset within the first {@link CharSequence}
     * @param   cs2         the second {@link CharSequence}
     * @param   offset2     the start offset within the second {@link CharSequence}
     * @param   count       the count of characters to compare
     * @return              {@code true} if the characters are equal
     */
    static boolean compareCS(CharSequence cs1, int offset1, CharSequence cs2, int offset2, int count) {
        int i = offset1;
        int j = offset2;
        int stopper = offset1 + count;
        while (i < stopper)
            if (cs1.charAt(i++) != cs2.charAt(j++))
                return false;
        return true;
    }

}
