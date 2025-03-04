/*
 * @(#) ContainsMatcher.java
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

public class ContainsMatcher implements StringMatcher {

    private final String string;

    public ContainsMatcher(String string) {
        this.string = Objects.requireNonNull(string, "String must not be null");
    }

    @Override
    public boolean matches(CharSequence target) {
        int count = string.length() - 1;
        if (count < 0)
            return true; // degenerate case; empty string
        int lastIndex = target.length() - string.length();
        if (lastIndex < 0)
            return false;
        int i = 0;
        char firstChar = string.charAt(0);
        while (i <= lastIndex) {
            int j = indexOf(target, firstChar, i, lastIndex);
            if (j < 0)
                return false;
            i = j + 1;
            if (StringMatcher.compareCS(target, i, string, 1, count))
                return true;
        }
        return false;
    }

    private static int indexOf(CharSequence cs, char ch, int fromIndex, int lastIndex) {
        for (int i = fromIndex; i <= lastIndex; i++)
            if (cs.charAt(i) == ch)
                return i;
        return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ContainsMatcher))
            return false;
        return string.equals(((ContainsMatcher)obj).string);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

}
