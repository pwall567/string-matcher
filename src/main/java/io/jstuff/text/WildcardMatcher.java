/*
 * @(#) WildcardMatcher.java
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

import java.util.Objects;

public class WildcardMatcher implements StringMatcher {

    public static final char defaultSingleMatchChar = '?';
    public static final char defaultMultiMatchChar = '*';

    private final String pattern;
    private final char singleMatchChar;
    private final char multiMatchChar;
    private final int patternLength;

    /**
     * Construct a {@code WildcardMatcher} with the specified wildcard characters.
     *
     * @param   pattern             the text pattern
     * @param   singleMatchChar     the character used to represent a single character wildcard match in the pattern
     * @param   multiMatchChar      the character used to represent a multi-character wildcard match in the pattern
     */
    public WildcardMatcher(String pattern, char singleMatchChar, char multiMatchChar) {
        this.pattern = Objects.requireNonNull(pattern, "Pattern must not be null");
        this.singleMatchChar = singleMatchChar;
        this.multiMatchChar = multiMatchChar;
        patternLength = pattern.length();
    }

    /**
     * Construct a {@code WildcardMatcher} with the default wildcard characters.
     *
     * @param   pattern             the text pattern
     */
    public WildcardMatcher(String pattern) {
        this(pattern, defaultSingleMatchChar, defaultMultiMatchChar);
    }

    @Override
    public boolean matches(CharSequence target) {
        Objects.requireNonNull(target, "Target must not be null");
        return matches(target, 0, target.length(), 0);
    }

    private boolean matches(CharSequence target, int targetStart, int targetEnd, int patternStart) {
        int targetIndex = targetStart;
        int patternIndex = patternStart;
        while (patternIndex < patternLength) {
            char patternChar = pattern.charAt(patternIndex++);
            if (patternChar == singleMatchChar) {
                if (targetIndex >= targetEnd)
                    return false;
                char targetChar = target.charAt(targetIndex++);
                if (Character.isHighSurrogate(targetChar) && targetIndex < targetEnd &&
                        Character.isLowSurrogate(target.charAt(targetIndex)))
                    targetIndex++;
            }
            else if (patternChar == multiMatchChar) {
                if (patternIndex == patternLength)
                    return true;
                while (true) {
                    if (targetIndex >= targetEnd)
                        return false;
                    if (matches(target, targetIndex++, targetEnd, patternIndex))
                        return true;
                }
            }
            else {
                if (targetIndex >= targetEnd || target.charAt(targetIndex++) != patternChar)
                    return false;
            }
        }
        return targetIndex == targetEnd;
    }

}
