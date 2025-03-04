/*
 * @(#) AlternateMatcher.java
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
import java.util.Objects;

public class AlternateMatcher implements StringMatcher {

    private final StringMatcher[] matchers;

    public AlternateMatcher(StringMatcher[] matchers) {
        this.matchers = Objects.requireNonNull(matchers, "Matchers list must not be null");
        for (StringMatcher matcher : matchers)
            Objects.requireNonNull(matcher, "Matcher must not be null");
    }

    @Override
    public boolean matches(CharSequence target) {
        Objects.requireNonNull(target, "Target must not be null");
        for (StringMatcher matcher : matchers)
            if (matcher.matches(target))
                return true;
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AlternateMatcher))
            return false;
        return Arrays.equals(matchers, ((AlternateMatcher)obj).matchers);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(matchers);
    }

}
